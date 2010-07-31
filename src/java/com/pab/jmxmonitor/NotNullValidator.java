package com.pab.jmxmonitor;

import com.pab.jmxmonitor.output.Status;
import com.pab.jmxmonitor.output.StatusCode;


public class NotNullValidator implements Validator {

	@Override
	public Status test(Object o) {
		return o == null ? 
				new Status(StatusCode.RED, null): 
				new Status(StatusCode.GREEN, null);
	}

}
