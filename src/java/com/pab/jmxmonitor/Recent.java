/**
 * 
 */
package com.pab.jmxmonitor;

import com.pab.jmxmonitor.output.Status;
import com.pab.jmxmonitor.output.StatusCode;


public class Recent  {

	public Status test(Object o) {
		long now = System.currentTimeMillis();
		if (o == null)
			return new Status(StatusCode.RED, "No report");
		if (!(o instanceof Long))
			return new Status(StatusCode.RED, "Unexpected type");;
		return now - (Long) o < 5*1000*60  
				? new Status(StatusCode.GREEN)
				: new Status(StatusCode.RED, "Too old");
	}

}