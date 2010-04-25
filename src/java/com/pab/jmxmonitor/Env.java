package com.pab.jmxmonitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Env {

	public static Validator.Constructor lookupLocalId(String string) {
		return Validators.lookup(string);
	}

	public static Validator construct(String s1, String s2, List<Object> args) {
		Validator.Constructor c = s1 == null ? lookupLocalId(s2) : lookupId(s1, s2);
		return c.construct(args);
	}
	
	public static Validator.Constructor lookupId(String string, String string2) {
		Class<?> c;
		try {
			c = Class.forName(string);
			Method m = c.getMethod(string2);
			Object o = m.invoke(null);
			if (!(o instanceof Validator))
				throw new RuntimeException("oops");
			
			return (Validator.Constructor) o;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}

	public static Validator evaluate(Object v) {
		return (Validator) v;
	}

	public static Validator construct(Validator.Constructor v, List<Object> cl) {
		return v.construct(cl);
	}

	public static Object createId(String string, String string2) {
		return null;
	}

	public static Object createId(String string) {
		return null;
	}

}
