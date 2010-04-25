package com.pab.jmxmonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeMBeanException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

/**
 * Generic JMX Query stuff.
 * Developed for the xymon framework.  
 * This program is self contained - we just read in a file containing the hosts to talk to, then 
 * for each item read the object off the jmx service located on the host.
 * The results are reported in xymon format (see below).
 */

public class Main {
	private static Logger LOG = Logger.getLogger(Main.class.getName());
	private Map<JmxId, MBeanConn> regMap = new HashMap<JmxId, MBeanConn>();
	private MonitorDao monitorDao = new MonitorDao();
	private List<Exception> badExceptions = new LinkedList<Exception>();
	private ValidationParser parser = new ValidationParser(null);
	
	static public void main(String[] args) {
		(new Main()).run(args);
	}

	public Main() {
	}
	
	
	void run(String[] args) {
		String fname = args[0];
		List<Monitorable> items = monitorDao.getMonitoredItems(fname);
		Map<String, MonitorResult> results = new HashMap<String, MonitorResult>();
		List<Monitorable> badItems = new LinkedList<Monitorable>();
		
		for (Monitorable m : items) {
			Object o = readAttribute(m);
			LOG.fine("Value of " + m.getAttributeName() + " = " + o);
			String key = m.getMonitorKey();
			MonitorResult res = results.get(key);
			if (res == null) {
				res = m.createResultObject();
				results.put(key, res);
			}
			Status status = testValidator(m, o);
			res.addResult(status, m.getNickname(), o == null ? null : o.toString());
			if (o == null)
				badItems.add(m);
		}
		
		if (badItems != null) {
			reportBadMbeanServers(badItems);
		}

		try {
			closeMbscs();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (MonitorResult res: results.values()) {
			try {
				File f = new File(res.getMonitorKey() + ".result");
				FileWriter w = new FileWriter(f);
				w.append(res.bbResult());
				w.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				continue;
			}
				
		}
	}

	Status testValidator(Monitorable m, Object o) {
		String s = m.validator;
		if (s == null || s.trim().length() == 0)
			return new Status(StatusCode.GREEN, null);
		ANTLRStringStream inp = new ANTLRStringStream(s);
		Javalex lex = new Javalex(inp);
		parser.setTokenStream(new CommonTokenStream(lex));
		try {
			Validator v =  parser.whole();
			return v.test(o);
		} catch (RecognitionException e) {
			return new Status(StatusCode.RED, "Parse error in expression: [" + s +"] "+ e);
		}
	}
	
	// Report errors in a kinda sensible way;
	// When we can't create a connection, report it as being down.
	// If the connection exists, then the lookup is probably wrong, so
	// dump all the objects on the server.  Hopefully a quick look at the log will
	// yield a clue as to the problem.
	void reportBadMbeanServers(List<Monitorable> monitorables) {
		Set<JmxId> urls = new HashSet<JmxId>();
		for (Monitorable m: monitorables) {
			JmxId url = m.getJmxId();
			urls.add(url);
		}
		
		for (JmxId url: urls) {
			try {
				MBeanServerConnection conn = lookupMbsc(url);
				if (conn == null) {
					LOG.warning("Connection is down: " + url);
					continue;
				}
				
				Set<ObjectName> names = conn.queryNames(null, null);
				for (ObjectName name: names) {
					LOG.info("Reg: " + url + " " + name);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	static class JmxId {
		String url;
		String service;
		String user;
		String password;
		
		JmxId(String url, String user, String password, String service) {
			this.url = url;
			this.user = user;
			this.password = password;
			this.service = service;
		}
		
		public String toString() {
			return url + ((user == null) ? "" : ("[" + user+ "/"+password + "]"));
		}
		
		public boolean equals(Object o) {
			if (!(o instanceof JmxId))
				return false;
			JmxId oj = (JmxId) o;
			return url.equals(oj.url) 
				&& checkedEqual(user, oj.user) && checkedEqual(password, oj.password)
				&& checkedEqual(service, oj.service);
		}
		
		public int hashCode() {
			return 67 * url.hashCode() + 31 * checkedHashcode(user) + checkedHashcode(password); 
		}
		
		private int checkedHashcode(Object s1) {
			return s1 == null ? 30 : s1.hashCode();
		}
		private boolean checkedEqual(Object s1, Object s2) {
			if (s1 == null && s2 != null)
				return false;
			if (s1 == null)
				return true;
			return s1.equals(s2);
		}
		
	}
	
	/**
	 * Status ordering
	 * @param s1
	 * @param s2
	 * @return the larger of s1 and s2 (green &lt; yellow &lt; red).
	 */
	static StatusCode max(StatusCode s1, StatusCode s2) {
		if (s1.priority > s2.priority)
			return s1;
		else 
			return s2;
	}
	
	/**
	 * Represents a result that can be passed to xymon.
	 * xymon messages are formated as:
	 * status "hostname"."subsystem" "status" "date"
	 * "item": "value"
	 * ...
	 * where quoted values are computed.
	 * hostname represents the host - it should be listed in bb-hosts.
	 * subsystem is the category of the value - eg. JBoss, Http, etc.
	 * item is the name of a quantity that we want to report
	 * value is the value of the quantity.
	 * 
	 * A result is built up from tests supplied in config files.
	 * Each test will add an item/value pair, and may update the 
	 * status if the test value is worse than the current status.
	 * 
	 */
	static class MonitorResult {
		private String hostName;
		private String subsystem;
		private List<String> texts;
		private StatusCode status;
		private List<String> errors;
		
		MonitorResult(String host, String subsystem) {
			this.hostName = host;
			this.subsystem = subsystem;
			this.texts = new LinkedList<String>();
			this.status = StatusCode.GREEN;
			this.errors = new LinkedList<String>();
		}
		
		public String getMonitorKey() {
			return hostName + "-" + subsystem;
		}

		/**
		 * Return output in a form suitable for BB
		 * @return
		 */
		public String bbResult() {
			String txt = "status " + hostName + "." + subsystem + " " + status + " " + new Date() + "\n";
			for (String s: texts) {
				txt += s + "\n";
			}
			if (!errors.isEmpty())
				txt += "\n";
			for (String msg: errors) {
				txt += msg + "\n";
			}
			
			return txt;
		}
		
		void addResult(Status status, String attrName, String value) {
			this.status = max(this.status, status.getCode());
			texts.add(attrName + ": " + value);
			if (status.getErrorMessage() != null)
				errors.add("Error (" + attrName + ") - " + status.getErrorMessage());
		}
	}
	
	// JConsole is a bit vague on how to search for items.
	// Look in this order (name, service, type).
	private Object readAttribute(Monitorable m) {
		if (m.getDomain().isEmpty())
			return readAttributeByObjName(m.getMBeanName().replace('+', ','), m);
		if (m.getMBeanName().contains("="))
			return readAttributeByDescriptor(m);
		Object o = readAttributeByCategory("name", m);
		if (o != null)
			return o;
		o = readAttributeByCategory("service", m);
		if (o != null)
			return o;
		o = readAttributeByCategory("type", m);
		return o;
	}

	private Object readAttributeByDescriptor(Monitorable m) {
		int idx = m.getMBeanName().indexOf("=");
		String lhs = m.getMBeanName().substring(0, idx);
		String rhs = m.getMBeanName().substring(idx+1);
		return readAttributeByCategory(lhs, rhs, m);
	}

	private Object readAttributeByCategory(String cat, Monitorable m) {
		return readAttributeByCategory(cat, m.getMBeanName(), m);
	}
	
	private Object readAttributeByCategory(String cat, String id, Monitorable m) {
		return readAttributeByObjName(m.getDomain() + ":"+cat+"="+id, m);
	}

	private Object readAttributeByObjName(String objNameString, Monitorable m) {
		ObjectName objName;
		try {
			objName = new ObjectName(objNameString);
		} catch (MalformedObjectNameException e) {
			LOG.warning("objName malformed: " + objNameString);
			return null;
		}
		return readAttributeByObjName(objName, m);
	}
	
	private Object readAttributeByObjName(ObjectName objName, Monitorable m) {
		try {
			MBeanServerConnection mbsc = lookupMbsc(m.getJmxId());
			Object o = mbsc.getAttribute(objName, m.getAttributeName());
			LOG.fine("Value of: " + objName + " = " + o);
			if (m.getAttributeKey() != null) {
				if (!(o instanceof CompositeDataSupport))
					return o;
				CompositeDataSupport compData = (CompositeDataSupport) o;
				return compData.get(m.getAttributeKey());
			}
		
			return o;
		}
		catch (AttributeNotFoundException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
		} catch (MBeanException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (RuntimeMBeanException e) {
			badExceptions.add(e);
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	MBeanServerConnection lookupMbsc(JmxId url) throws IOException {
		if (regMap.get(url) != null) {
			return regMap.get(url).mbsc;
		}
		
		MBeanConn conn = connectMbsc(url);
		regMap.put(url, conn);
		return conn.mbsc;
	}
	
	void closeMbscs() throws IOException {
		for (MBeanConn mc: regMap.values()) {
			mc.c.close();
		}
	}
	
	static class MBeanConn {
		JMXConnector c;
		MBeanServerConnection mbsc;

		public MBeanConn(JMXConnector conn, MBeanServerConnection mbsc2) {
			this.c = conn;
			this.mbsc = mbsc2;
		}
}
	
	MBeanConn connectMbsc(JmxId id) throws IOException  {
		JMXServiceURL surl = new JMXServiceURL(id.url);

        Map<String, String[]> env = new HashMap<String, String[]>();
        if (id.user != null) {
        	env.put(JMXConnector.CREDENTIALS,
        			new String[] {id.user, id.password});
        }
        
        JMXConnector conn =  JMXConnectorFactory.connect(surl, env);
		MBeanServerConnection mbsc = conn.getMBeanServerConnection();
				
		return new MBeanConn(conn, mbsc);
	}
	
	/**
	 * Test DAO object
	 * @author pabroadbery
	 *
	 */
	class OneMonitorDao {
		List<Monitorable> getMonitoredItems(String id) {
			Monitorable m = new Monitorable();
			m.hostName = "loncfdapp-zq1";
			m.port = "1099";
			m.domainName = "PULSE_loncfdapp-zq1";
			m.mbeanName = "ActivateSystems";
			m.attrName = "Uptime";
			
			return Collections.singletonList(m);
		}
	}

	/**
	 * DAO loading from file.  
	 * @author pabroadbery
	 *
	 */
	class MonitorDao {
		List<Monitorable> getMonitoredItems(String fileName) {
			try {
				File f = new File(fileName);
				BufferedReader rdr = new BufferedReader(new FileReader(f));
				List<Monitorable> l = new LinkedList<Monitorable>();
				String line;
				while ( (line = rdr.readLine()) != null) {
					if (line.startsWith("#"))
						continue;
					
					String[] args = line.split(",");
					if (args.length < 5)
						continue;
					List<String> fmtArgs = new ArrayList<String>(args.length);
					for (int i=0; i<args.length; i++) {
						fmtArgs.add(args[i].trim());
					}
					Monitorable m = new Monitorable(fmtArgs);
					l.add(m);
				}
				return l;
			}
			catch (IOException e) {
				return Collections.emptyList();
			}
		}
	}

	class Monitorable {
		String hostName;
		String logicalHostName;
		String subsystem;
		String nickname;
		String port; // really a number, but we don't care
		String domainName;
		String mbeanName;
		String attrName;
		String validator;
		
		Monitorable() {}
		
		public String getNickname() {
			return nickname;
		}

		Monitorable(List<String> args) {
			hostName = args.get(0);
			subsystem = args.get(1);
			nickname = args.get(2);
			port = args.get(3);
			domainName = args.get(4);
			mbeanName = args.get(5);
			attrName = args.get(6);
			// Later stuff is optional..
			if (hostName.indexOf("@") == -1) {
				logicalHostName = hostName;
			}
			else {
				logicalHostName = hostName.substring(0, hostName.indexOf("@"));
				hostName = hostName.substring(hostName.indexOf("@") + 1);
			}
			if (args.size() <= 7)
				validator = "";
			else {
				validator = args.get(7);
				for (int i=8; i<args.size(); i++) {
					validator = validator + "," + args.get(i);
				}
			}
		}
	
		public String toString() {
			return "(Monitor: " + getJmxId() 
						+ " " + domainName + ", name=" + mbeanName + " attr="+attrName + ")";  
		}
		
		JmxId getJmxId() {
			String p = this.port;
			String userName = null;
			String password = null;
			String service = "jmxrmi";

			int idx = p.indexOf('%');
			if (idx != -1) {
				userName = p.substring(0, idx);
				int atpos = p.indexOf('@');
				if (idx == -1)
					return null;
				password = p.substring(idx+1, atpos);
				p = p.substring(atpos+1); 
			}
			int slashIdx = p.indexOf("/");
			if (slashIdx > 0) {
				service = p.substring(slashIdx+1);
				p = p.substring(0, slashIdx);
			}
			String url = "service:jmx:rmi:///jndi/rmi://" + hostName + ":" + p	+ "/" + service;
			return new JmxId(url, userName, password, service);
		}

		public String validatorName() {
			return validator;
		}

		public MonitorResult createResultObject() {
			return new MonitorResult(logicalHostName, subsystem);
		}

		public String getMonitorKey() {
			return logicalHostName + "-" + subsystem;
		}

		String getDomain() {
			return domainName;
		}
		
		String getMBeanName() {
			return mbeanName;
		}

		String getAttributeName() {
			int idx = attrName.indexOf('.');
			if (idx == -1)
				return attrName;
			return attrName.substring(0, idx);
		}
		
		public String getAttributeKey() {
			int idx = attrName.indexOf('.');
			if (idx == -1)
				return null;
			return attrName.substring(idx+1);
		}

	}
	
}
