/**
 * 
 */
package com.pab.jmxmonitor;

import com.pab.jmxmonitor.Main.Status;

public class Recent  {

	public Status test(Object o) {
		long now = System.currentTimeMillis();
		if (o == null)
			return Status.RED;
		if (!(o instanceof Long))
			return Status.RED;
		return now - (Long) o < 5*1000*60 ? Status.GREEN : Status.RED;
	}

}