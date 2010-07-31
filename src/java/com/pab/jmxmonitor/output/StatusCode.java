package com.pab.jmxmonitor.output;

/**
 * Quick and dirty type representing status
 *
 */
public enum StatusCode { GREEN("green",0), YELLOW("yellow",1), RED("red",2);
	private String name;
	int priority;
	
	StatusCode(String s, int priority) {
		this.name = s;
		this.priority = priority;
	}
	
	public String toString() {
		return name;
	}

	public StatusCode and(StatusCode o) {
		int priority = Math.max(o.priority, this.priority);
		
		return statusFor(priority);	
	}

	public StatusCode or(StatusCode o) {
		int priority = Math.min(o.priority, this.priority);
		
		return statusFor(priority);	
	}

	private static StatusCode statusFor(int priority) {
		switch (priority) {
		case 0: return GREEN;
		case 1: return YELLOW;
		case 2: return RED;
		default:
			throw new RuntimeException();
		}
	}

}