package com.pab.jmxmonitor.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class XymonFileOutputter implements Outputter {
	/* (non-Javadoc)
	 * @see com.pab.jmxmonitor.output.Outputter#writeResults(java.util.Collection)
	 */
	@Override
	public void writeResults(Collection<MonitorResult> collection) {
		for (MonitorResult res: collection) {
			try {
				File f = new File(res.getMonitorKey() + ".result");
				FileWriter w = new FileWriter(f);
				w.append(res.bbResult());
				w.close();
			}
			catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	
}
