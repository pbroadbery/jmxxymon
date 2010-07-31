package com.pab.jmxmonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.pab.jmxmonitor.input.JmxId;
import com.pab.jmxmonitor.input.Monitorable;
import com.pab.jmxmonitor.output.MonitorResult;
import com.pab.jmxmonitor.output.Outputter;
import com.pab.jmxmonitor.output.Status;
import com.pab.jmxmonitor.output.StatusLine;
import com.pab.jmxmonitor.output.XymonFileOutputter;

/**
 * Generic JMX Query stuff.
 * Developed for the xymon framework.  
 * This program is self contained - we just read in a file containing the hosts to talk to, then 
 * for each item read the object off the jmx service located on the host.
 * The results are reported in xymon format (see below).
 */

public class Main {
	@SuppressWarnings("unused")
	private static Logger LOG = Logger.getLogger(Main.class.getName());
	private MonitorDao monitorDao = new MonitorDao();

	static public void main(String[] args) {
		(new Main()).run(args);
	}

	public Main() {
	}
	
	
	void run(String[] args) {
		String fname = args[0];
		List<Monitorable> items = monitorDao.getMonitoredItems(fname);
		ITestExecutor executor = new TestExecutor();
		
		Collection<MonitorResult> results = executor.executeTests(items);

		Outputter o = new XymonFileOutputter();
		o.writeResults(results);
	}

	
	/**
	 * DAO loading from file.  
	 * @author pabroadbery
	 *
	 */
	class MonitorDao {
		List<Monitorable> getMonitoredItems(String fileName) {
			try {
				File f = new File(fileName);
				BufferedReader rdr = new BufferedReader(new FileReader(f));
				List<Monitorable> l = new LinkedList<Monitorable>();
				String line;
				while ( (line = rdr.readLine()) != null) {
					if (line.startsWith("#"))
						continue;
					
					String[] args = line.split(",");
					if (args.length < 5)
						continue;
					List<String> fmtArgs = new ArrayList<String>(args.length);
					for (int i=0; i<args.length; i++) {
						fmtArgs.add(args[i].trim());
					}
					Monitorable m = new Monitorable(fmtArgs);
					l.add(m);
				}
				return l;
			}
			catch (IOException e) {
				return Collections.emptyList();
			}
		}
	}
	
}
