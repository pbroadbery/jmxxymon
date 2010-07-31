/**
 * 
 */
package com.pab.jmxmonitor.output;


public class Status {
	private StatusCode code;
	private String errorMessage;
	private String value;
	
	public Status(StatusCode code) {
		this(code, null);
	}

	public Status(StatusCode code, String errorMessage) {
		this.code = code;
		this.errorMessage = errorMessage;
	}
	
	public StatusCode getCode() {
		return code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Status or(Status test) {
		return new Status(this.getCode().or(test.getCode()), safeConcat(errorMessage, test.getErrorMessage())); 
	}

	private String safeConcat(String s1, String s2) {
		if (s1 == null)
			return s2;
		if (s2 == null)
			return s1;
		return s1 + ", " + s2;
	}

	public Status and(Status test) {
		return new Status(this.getCode().and(test.getCode()), safeConcat(errorMessage, test.getErrorMessage())); 
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}