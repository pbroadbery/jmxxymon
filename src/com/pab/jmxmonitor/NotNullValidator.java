package com.pab.jmxmonitor;

import com.pab.jmxmonitor.Main.Status;

public class NotNullValidator implements Validator {

	@Override
	public Status test(Object o) {
		return o == null ? Status.RED: Status.GREEN;
	}

}
