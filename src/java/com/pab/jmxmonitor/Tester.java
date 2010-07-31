package com.pab.jmxmonitor;

import javax.management.openmbean.CompositeDataSupport;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import com.pab.jmxmonitor.input.Monitorable;
import com.pab.jmxmonitor.output.Status;
import com.pab.jmxmonitor.output.StatusCode;
import com.pab.jmxmonitor.output.StatusLine;

public class Tester {
	private ValidationParser parser = new ValidationParser(null);
	
	Object evaluateValue(Monitorable m, Object o) {
		if (m.getAttributeKey() != null) {
			if (!(o instanceof CompositeDataSupport))
				return o;
			CompositeDataSupport compData = (CompositeDataSupport) o;
			return compData.get(m.getAttributeKey());
		}
		return o;
	}
	
	StatusLine testValidator(Monitorable m, Object o) {
		String s = m.getValidator();
		if (s == null || s.trim().length() == 0)
			return new StatusLine(new Status(StatusCode.GREEN, null), valueAsText(o));
		return testWithGrammar(o, s);
	}

	private String valueAsText(Object o) {
		if (o == null)
			return "null";
		try {
			return o.toString();
		}
		catch (RuntimeException e) {
			return "<exception: " + e.getMessage() + " raised when converting to string>";
		}
	}

	private StatusLine testWithGrammar(Object o, String test) {
		ANTLRStringStream inp = new ANTLRStringStream(test);
		Javalex lex = new Javalex(inp);
		parser.setTokenStream(new CommonTokenStream(lex));
		try {
			Validator v =  parser.whole();
			return new StatusLine(v.test(o), valueAsText(o));
		} catch (RecognitionException e) {
			return new StatusLine(new Status(StatusCode.RED, "Parse error in expression: [" + test +"] "+ e), valueAsText(o));
		}
	}


}
