package com.pab.jmxmonitor.output;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
public class MonitorResult {
	private String hostName;
	private String subsystem;
	private List<String> texts;
	private StatusCode status;
	private List<String> errors;
	
	public MonitorResult(String host, String subsystem) {
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
	
	public void addResult(StatusLine statusLine, String attrName) {
		this.status = max(status, statusLine.getStatus().getCode());
		texts.add(attrName + ": " + statusLine.getValue());
		if (statusLine.getStatus().getErrorMessage() != null)
			errors.add("Error (" + attrName + ") - " + statusLine.getStatus().getErrorMessage());
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
	

}
