package com.pab.jmxmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pab.jmxmonitor.Main.Status;


public class Validators {
	static Map<String, Validator.Constructor> validatorMap = new HashMap<String, Validator.Constructor>();
	static {
		validatorMap.put("greaterThan", greaterThan());
		validatorMap.put("lessThan", lessThan());
		validatorMap.put("and", new AndConstructor());
		validatorMap.put("or", new OrConstructor());
		validatorMap.put("notNull", new FixedConstructor(new NotNullValidator()));
		validatorMap.put("moreRecentThan", recent());
	}
	
	public static Validator.Constructor lookup(String string) {
		return validatorMap.get(string);
	}


	public static Validator.Constructor lessThan() {
		return new Validator.Constructor() {
			
			@Override
			public Validator construct(List<Object> params) {
				if (params.isEmpty() || params.size() > 1)
					throw new RuntimeException("lessThan takes one parameter");
				Object o = params.get(0);

				if (!(o instanceof Number))
					return null;
				
					final double amount = ((Number) o).doubleValue();
					Validator validator = new Validator() {
						@Override
						public Status test(Object o) {
							double d;
							if (o == null)
								return Status.RED;
							if (o instanceof Number) {
								d = ((Number) o).doubleValue();
								return d < amount ? Status.GREEN: Status.RED;
							}
							return Status.RED;
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
				if (params.isEmpty() || params.size() > 1)
					throw new RuntimeException("lessThan takes one parameter");
				Object o = params.get(0);

				if (!(o instanceof Number))
					return null;
				
					final double amount = ((Number) o).doubleValue();
					Validator validator = new Validator() {
						@Override
						public Status test(Object o) {
							double d;
							if (o == null)
								return Status.RED;
							if (o instanceof Number) {
								d = ((Number) o).doubleValue();
								return d > amount ? Status.GREEN: Status.RED;
							}
							return Status.RED;
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
			Status result = Status.GREEN;
			for (Validator v : validators) {
				result = result.and(v.test(o));
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
			Status result = Status.GREEN;
			for (Validator v : validators) {
				result = result.and(v.test(o));
			}
			return result;
		}
	}


}
