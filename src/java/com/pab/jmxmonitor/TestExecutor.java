package com.pab.jmxmonitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.pab.jmxmonitor.input.JmxId;
import com.pab.jmxmonitor.input.Monitorable;
import com.pab.jmxmonitor.output.MonitorResult;
import com.pab.jmxmonitor.output.StatusLine;
import com.pab.jmxmonitor.util.HashMultiMap;
import com.pab.jmxmonitor.util.MultiMap;

public class TestExecutor implements ITestExecutor {
	private static Logger LOG = Logger.getLogger(Main.class.getName());
	private static Tester tester = new Tester();
	private ExecutorService pool = Executors.newCachedThreadPool();
	
	TestExecutor(ExecutorService svc) {
		this.pool = svc;
	}
	
	TestExecutor() {
	}
	
	/* (non-Javadoc)
	 * @see com.pab.jmxmonitor.ITestExecutor#executeTests(java.util.Collection)
	 */
	@Override
	public Collection<MonitorResult> executeTests(Collection<Monitorable> tests) {
		LOG.fine("Executing " + tests.size() + " tests");
		// 1. Filter tests by connection
		MultiMap<JmxId, Monitorable> tasks = collectTasks(tests);
		// 2. Create Future (or something) to return test statuses
		Collection<MonitorResult> results = executeTestsInPool(tasks);
		// 3. Put statuses into collection structure
		
		pool.shutdown();
		
		return results;
	}

	private Collection<MonitorResult> executeTestsInPool(MultiMap<JmxId, Monitorable> tasks) {
		List<Future<Collection<MonitorResult>>> l = new LinkedList<Future<Collection<MonitorResult>>>();
		for (final Map.Entry<JmxId, List<Monitorable>> ent: tasks.entrySet()) {
			Callable<Collection<MonitorResult>> task = new Callable<Collection<MonitorResult>>() {
				public Collection<MonitorResult> call() throws Exception {
					return evaluateTests(ent.getKey(), ent.getValue());
				}};
			LOG.fine("Starting " + ent.getKey());

			Future<Collection<MonitorResult>> f = pool.submit(task);
			
			l.add(f);
		}
		
		Collection<MonitorResult> results = flattenCollection(collectValues(l));
		
		return results;
	}
	
	
	private static <T> Collection<T> collectValues(Collection<Future<T>> list) {
		List<T> r = new ArrayList<T>(list.size());
		for (Future<T> future: list) {
			try {
				T value = future.get();
				r.add(value);
				// FIXME: What to do on error?  -- replace future with FutureResult, stash exn
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return r;
		
	}

	private <T> Collection<T> flattenCollection(Collection<Collection<T>> ll) {
		List<T> r = new LinkedList<T>();
		for (Collection<T> c: ll) {
			r.addAll(c);
		}
		return r;
	}
	
	
	private MultiMap<JmxId, Monitorable> collectTasks(Collection<Monitorable> tests) {
		MultiMap<JmxId, Monitorable> m = createTaskMap();
		for (Monitorable test: tests) {
			addTask(m, test.getJmxId(), test);
		}
		return m;
	}

	private void addTask(MultiMap<JmxId, Monitorable> m, JmxId jmxId, Monitorable test) {
		m.add(jmxId, test);
	}

	private MultiMap<JmxId, Monitorable> createTaskMap() {
		return new HashMultiMap<JmxId, Monitorable>();
	}
	
	Collection<MonitorResult> evaluateTests(JmxId id, List<Monitorable> tests) {
		Map<String, MonitorResult> results = new HashMap<String, MonitorResult>();
		MBeanConn conn = createMBeanConn(id);
		
		for (Monitorable m: tests) {
			StatusLine status = evaluateTest(conn, m);
			MonitorResult r = results.get(m.getMonitorKey());
			if (r == null) {
				r = createResultObject(m);
				results.put(m.getMonitorKey(), r);
			}
			r.addResult(status, m.getNickname());
		}
		conn.close();
		return results.values();
		
	}
	
	MBeanConn createMBeanConn(JmxId id) {
		try {
			return createMBeanConnInner(id);
		}
		catch (IOException e) {
			return new MBeanConn(e);
		}
	}
	
	MBeanConn createMBeanConnInner(JmxId id) throws IOException  {
		JMXServiceURL surl = new JMXServiceURL(id.getUrl());

        Map<String, String[]> env = new HashMap<String, String[]>();
        if (id.getUser() != null) {
        	env.put(JMXConnector.CREDENTIALS,
        			new String[] {id.getUser(), id.getPassword()});
        }
        
        JMXConnector conn =  JMXConnectorFactory.connect(surl, env);
		MBeanServerConnection mbsc = conn.getMBeanServerConnection();
				
		return new MBeanConn(id.getUrl(), conn, mbsc);
	}

	public MonitorResult createResultObject(Monitorable m) {
		return new MonitorResult(m.getLogicalHostName(), m.getSubsystem());
	}

	StatusLine evaluateTest(MBeanConn conn, Monitorable m) {
		Object o = conn.readAttribute(m);
		LOG.fine("Value of " + m.getAttributeName() + " = " + o);
		o = tester.evaluateValue(m, o);
		StatusLine status = tester.testValidator(m, o);
		LOG.fine("Status is" + status);
		return status;
	}

}
