/**
 * 
 */
package com.pab.jmxmonitor;

import java.util.List;

import com.pab.jmxmonitor.output.Status;


public interface Validator {
	Status test(Object o);
	interface Constructor {
		Validator construct(List<Object> params);
	}
}