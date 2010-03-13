/**
 * 
 */
package com.pab.jmxmonitor;

import java.util.List;

import com.pab.jmxmonitor.Main.Status;

public interface Validator {
	Status test(Object o);
	interface Constructor {
		Validator construct(List<Object> params);
	}
}