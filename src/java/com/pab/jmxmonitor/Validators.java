package com.pab.jmxmonitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class Validators {
	static Map<String, Validator.Constructor> validatorMap = new HashMap<String, Validator.Constructor>();
	static {
		validatorMap.put("greaterThan", greaterThan());
		validatorMap.put("lessThan", lessThan());
		validatorMap.put("and", new AndConstructor());
		validatorMap.put("or", new OrConstructor());
		validatorMap.put("isString", isString());
		validatorMap.put("notNull", new FixedConstructor(new NotNullValidator()));
		validatorMap.put("recentMillis", recent());
	}
	
	public static Validator.Constructor lookup(String string) {
		return validatorMap.get(string);
	}


	public static Validator.Constructor lessThan() {
		return new Validator.Constructor() {
			
			@Override
			public Validator construct(List<Object> params) {
				if (params.isEmpty() || params.size() > 2)
					throw new RuntimeException("lessThan takes one or two parameters");
				Object o = params.get(0);
				final String msg = params.size() > 1 ? (String) params.get(1): "is not less than " + o;
				
				if (!(o instanceof Number))
					return null;
				
					final double amount = ((Number) o).doubleValue();
					Validator validator = new Validator() {

						@Override
						public Status test(Object o) {
							double d;
							if (o == null)
								return new Status(StatusCode.RED);
							if (o instanceof Number) {
								d = ((Number) o).doubleValue();
								return d < amount 
									? new Status(StatusCode.GREEN) 
									: new Status(StatusCode.RED, msg + "(value = " + o +")"); 
							}
							return new Status(StatusCode.RED, "Wrong type");
						}
					
					};
					return validator;
			}
		};
	}

	public static Validator.Constructor greaterThan() {
		return new Validator.Constructor() {
			
			@Override
			public Validator construct(List<Object> params) {
				if (params.isEmpty() || params.size() > 2)
					throw new RuntimeException("greaterThan takes one or two parameters");
				Object o = params.get(0);
				final String msg = (params.size() > 1) ? (String) params.get(1) : "is not greater than";
				if (!(o instanceof Number))
					return null;
				
					final double amount = ((Number) o).doubleValue();
					Validator validator = new Validator() {
						@Override
						public Status test(Object o) {
							double d;
							if (o == null)
								return new Status(StatusCode.RED);
							if (o instanceof Number) {
								d = ((Number) o).doubleValue();
								return d > amount 
									? new Status(StatusCode.GREEN)
									: new Status(StatusCode.RED, msg + " (value = " + o + ")");
							}
							return new Status(StatusCode.RED, "Should be a number: " + o + " " + o.getClass().getName());
						}
					
					};
					return validator;
			}
		};
	}

	public static Validator.Constructor recent() {
		return new Validator.Constructor() {
			public Validator construct(List<Object> params) {
				if (params.isEmpty() || params.size() > 1)
					throw new RuntimeException("greaterThan takes one parameter (seconds since last update)");
				Object o = params.get(0);
				if (!(o instanceof Number))
					return null;
				
					final double amount = ((Number) o).doubleValue();
					Validator validator = new Validator() {
						@Override
						public Status test(Object o) {
							long d;
							if (o == null)
								return new Status(StatusCode.RED);
							if (o instanceof Number) {
								d = ((Number) o).longValue();
								return d < System.currentTimeMillis() + amount*1000 
									? new Status(StatusCode.GREEN)
									: new Status(StatusCode.RED, "Last update was " + new Date(d));
							}
							return new Status(StatusCode.RED, "Wrong type: " + o + " " + o.getClass().getName());
						}
					
					};
					return validator;
			}
		};
	}

	public static Validator.Constructor isString() {
		return new Validator.Constructor() {
			
			@Override
			public Validator construct(List<Object> params) {
				if (params.isEmpty() || params.size() > 1)
					throw new RuntimeException("isString takes one or two parameters");
				final String wanted = params.get(0).toString().trim();
				final String msg = (params.size() > 1) ? (String) params.get(1) : "Should be '"+wanted+"'";
				
				Validator validator = new Validator() {
					@Override
					public Status test(Object val) {
						if (val == null)
							return new Status(StatusCode.RED, "No value found");
						String s = val.toString();
						return  (wanted.equals(s.trim()))
							? new Status(StatusCode.GREEN)
							: new Status(StatusCode.RED, msg + " (value = " + val + ")");
					}
				
				};
				return validator;
			}
		};
	}
	
	static class AndConstructor implements Validator.Constructor {
		
		@Override
		public Validator construct(List<Object> params) {
			ArrayList<Validator> validators = new ArrayList<Validator>();
			List<Object> notValidators = new LinkedList<Object>();
			for (Object o: params) {
				if (o instanceof Validator)
					validators.add((Validator) o);
				else
					notValidators.add(o);
			}
			if (!notValidators.isEmpty())
				throw new RuntimeException("Non validators: " + notValidators);

			return new AndValidator(validators);
		}
	
	}

	static class OrConstructor implements Validator.Constructor {
		
		@Override
		public Validator construct(List<Object> params) {
			ArrayList<Validator> validators = new ArrayList<Validator>();
			List<Object> notValidators = new LinkedList<Object>();
			for (Object o: params) {
				if (o instanceof Validator)
					validators.add((Validator) o);
				else
					notValidators.add(o);
			}
			if (!notValidators.isEmpty())
				throw new RuntimeException("Non validators: " + notValidators);

			return new OrValidator(validators);
		}
	
	}

	public static class OrValidator implements Validator {
		List<Validator> validators;
		
		OrValidator(List<Validator> l) {
			this.validators = l;
		}
		
		@Override
		public Status test(Object o) {
			Status result = new Status(StatusCode.RED);
			for (Validator v : validators) {
				result = result.or(v.test(o));
			}
			return result;
		}
	}


	public static class AndValidator implements Validator {
		List<Validator> validators;

		AndValidator(List<Validator> l) {
			this.validators = l;
		}

		@Override
		public Status test(Object o) {
			Status result = new Status(StatusCode.GREEN);
			for (Validator v : validators) {
				result = result.and(v.test(o));
			}
			return result;
		}
	}


}
