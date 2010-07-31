package com.pab.jmxmonitor;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.pab.jmxmonitor.input.JmxId;
import com.pab.jmxmonitor.input.Monitorable;
import com.pab.jmxmonitor.output.MonitorResult;
import com.pab.jmxmonitor.output.StatusLine;

public class SimpleTestExecutor implements ITestExecutor {
	private static Logger LOG = Logger.getLogger(Main.class.getName());
	private JmxConnectionPool connPool = new JmxConnectionPool();
	private Tester tester = new Tester();
	
	@Override
	public Collection<MonitorResult> executeTests(Collection<Monitorable> items) {
		List<Monitorable> badItems = new LinkedList<Monitorable>();
		Map<String, MonitorResult> results = new HashMap<String, MonitorResult>();

		for (Monitorable m : items) {
			MBeanConn conn = connPool.lookupMbsc(m.getJmxId());
			Object o = conn.readAttribute(m);
			o = tester.evaluateValue(m, o);
			LOG.fine("Value of " + m.getAttributeName() + " = " + o);
			String key = m.getMonitorKey();
			MonitorResult res = results.get(key);
			if (res == null) {
				res = createResultObject(m);
				results.put(key, res);
			}
			StatusLine status = tester.testValidator(m, o);
			res.addResult(status, m.getNickname());
			if (o == null)
				badItems.add(m);
		}
		
		if (badItems != null) {
			reportBadMbeanServers(badItems);
		}
		
		return results.values();
	}

	public MonitorResult createResultObject(Monitorable m) {
		return new MonitorResult(m.getLogicalHostName(), m.getSubsystem());
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
			MBeanConn connInfo = connPool.lookupConnInfo(url);
			if (connInfo.isBad())
				LOG.warning("Bad connection: " + url + " " + connInfo.getError().getMessage());
		}
		for (JmxId url: urls) {
			try {
				MBeanConn conn = connPool.lookupMbsc(url);
				if (conn.isBad()) {
					continue;
				}
				
				Set<ObjectName> names = conn.getMbsc().queryNames(null, null);
				for (ObjectName name: names) {
					LOG.info("Reg: " + url + " " + name);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	static class JmxConnectionPool {
		private Map<JmxId, MBeanConn> regMap = new HashMap<JmxId, MBeanConn>();
		
		MBeanConn lookupMbsc(JmxId url) {
			if (regMap.get(url) != null) {
				regMap.get(url);
			}
			
			MBeanConn conn;
			try {
				conn = connectMbsc(url);
				regMap.put(url, conn);
				return conn;
			} catch (IOException e) {
				MBeanConn badConn = new MBeanConn(e);
				regMap.put(url, badConn);
				return badConn;
			}
			
		}

		public MBeanConn lookupConnInfo(JmxId id) {
			return regMap.get(id);
		}

		Collection<MBeanConn> getBadConnections() {
			LinkedList<MBeanConn> badConnList = new LinkedList<MBeanConn>();
			for (MBeanConn c: regMap.values()) {
				if (c.isBad())
					badConnList.add(c);
			}
			return badConnList;
		}
		
		MBeanConn connectMbsc(JmxId id) throws IOException  {
			JMXServiceURL surl = new JMXServiceURL(id.getUrl());

	        Map<String, String[]> env = new HashMap<String, String[]>();
	        if (id.getUser() != null) {
	        	env.put(JMXConnector.CREDENTIALS,
	        			new String[] {id.getUser(), id.getPassword()});
	        }
	        
	        JMXConnector conn =  JMXConnectorFactory.connect(surl, env);
			MBeanServerConnection mbsc = conn.getMBeanServerConnection();
					
			return new MBeanConn(id.getUrl(), conn, mbsc);
		}

		void closeMbscs() throws IOException {
			for (MBeanConn mc: regMap.values()) {
					mc.close();
			}
		}
	}
	

	
}

