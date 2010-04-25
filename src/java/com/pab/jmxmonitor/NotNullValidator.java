package com.pab.jmxmonitor;


public class NotNullValidator implements Validator {

	@Override
	public Status test(Object o) {
		return o == null ? 
				new Status(StatusCode.RED, null): 
				new Status(StatusCode.GREEN, null);
	}

}
