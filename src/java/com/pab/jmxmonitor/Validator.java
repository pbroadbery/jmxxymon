/**
 * 
 */
package com.pab.jmxmonitor;

import java.util.List;


public interface Validator {
	Status test(Object o);
	interface Constructor {
		Validator construct(List<Object> params);
	}
}