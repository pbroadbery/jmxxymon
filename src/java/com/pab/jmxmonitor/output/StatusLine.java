package com.pab.jmxmonitor.output;

public class StatusLine {
	private Status status;
	private String value;
	
	
	public StatusLine(Status status, String value) {
		this.status = status;
		this.value = value;
	}
	
	public Status getStatus() {
		return status;
	}
	public String getValue() {
		return value;
	}
}
	