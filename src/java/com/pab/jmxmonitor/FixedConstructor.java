package com.pab.jmxmonitor;

import java.util.List;

import com.pab.jmxmonitor.Validator.Constructor;

public class FixedConstructor implements Constructor {
	Validator v;
	FixedConstructor(Validator v) {
		this.v = v;
	}

	
	@Override
	public Validator construct(List<Object> params) {
		return v;
	}

}
