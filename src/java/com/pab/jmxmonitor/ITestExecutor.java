package com.pab.jmxmonitor;

import java.util.Collection;

import com.pab.jmxmonitor.input.Monitorable;
import com.pab.jmxmonitor.output.MonitorResult;

public interface ITestExecutor {

	public abstract Collection<MonitorResult> executeTests(Collection<Monitorable> tests);

}