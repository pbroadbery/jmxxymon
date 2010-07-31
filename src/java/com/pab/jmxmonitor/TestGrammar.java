package com.pab.jmxmonitor;

import java.io.IOException;
import java.io.StringBufferInputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

@SuppressWarnings("deprecation")
public class TestGrammar {

	public static void main(String[] args) {
		for (int i=0; i<args.length; i++) {
			p(args[i]);
		}
	}
	
	static void p(String s) {
		CharStream input;
		
		try {
			System.out.println("Parsing: " + s);
			input = new ANTLRInputStream(new StringBufferInputStream(s));
			Javalex l = new Javalex(input);
			ValidationParser p = new ValidationParser(new CommonTokenStream(l));
			@SuppressWarnings("unused")
			Validator v = p.whole();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
