package com.pab.jmxmonitor.input;

import java.util.List;

import com.pab.jmxmonitor.Main;

public class Monitorable {
	String hostName;
	String logicalHostName;
	String subsystem;
	String nickname;
	String port; // really a number, but we don't care
	String domainName;
	String mbeanName;
	String attrName;
	String validator;
	
	public Monitorable() {}
	
	public String getNickname() {
		return nickname;
	}

	// FIXME: Use list of field names
	public Monitorable(List<String> args) {
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
	
	public JmxId getJmxId() {
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

	public String getMonitorKey() {
		return logicalHostName + "-" + subsystem;
	}

	public String getDomain() {
		return domainName;
	}
	
	public String getMBeanName() {
		return mbeanName;
	}

	public String getAttributeName() {
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

	public String getSubsystem() {
		return subsystem;
	}

	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	public String getLogicalHostName() {
		return logicalHostName;
	}

	public void setLogicalHostName(String logicalHostName) {
		this.logicalHostName = logicalHostName;
	}

	public String getValidator() {
		return this.validator;
	}

}