package com.pab.jmxmonitor;

import java.io.IOException;
import java.util.List;
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

import com.pab.jmxmonitor.input.Monitorable;

class MBeanConn {
	private static Logger LOG = Logger.getLogger(Main.class.getName());
	private MBeanServerConnection mbsc;
	private IOException e;
	private List<RuntimeMBeanException> badExceptions;
	private JMXConnector conn;
	private String name;
	
	public MBeanConn(String name, JMXConnector conn, MBeanServerConnection mbsc2) {
		this.mbsc = mbsc2;
		this.conn = conn;
	}
	
	public MBeanServerConnection getMbsc() {
		return mbsc;
	}

	public MBeanConn(IOException e) {
		this.e = e;
	}

	boolean isBad() {
		return e != null;
	}
	
	
	// JConsole is a bit vague on how to search for items.
	// Look in this order (name, service, type).
	public Object readAttribute(Monitorable m) {
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
			if (mbsc == null)
				return null;
			Object o = mbsc.getAttribute(objName, m.getAttributeName());
			LOG.fine("Value of: " + objName + " = " + o);
		
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

	public Throwable getError() {
		return e;
	}

	public void close() {
		if (isBad())
			return;
		try {
			conn.close();
		} catch (IOException e) {
			LOG.severe("Failed to close connection for: " + this.name);
		}
	}


}