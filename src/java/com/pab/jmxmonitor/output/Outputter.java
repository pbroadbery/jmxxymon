package com.pab.jmxmonitor.output;

import java.util.Collection;

public interface Outputter {

	public abstract void writeResults(Collection<MonitorResult> collection);

}