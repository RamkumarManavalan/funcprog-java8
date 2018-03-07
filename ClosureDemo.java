package funcprog;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ClosureDemo {

	public static void main(String[] args) {
		closureType1Demo();
		closureType2Demo();
		closureType3Demo();
		closureType4Demo();
		closureType5Demo();
		closureType6Demo();
		closureType7Demo();
	}

	/* 
	CLOSURES: 
	A closure is the combination of a function bundled together (enclosed) with 
	references to its surrounding state (the lexical environment). In other words, 
	a closure gives you access to an outer function’s scope from an inner function.

	Closures are commonly used to give objects data privacy. When you use closures for data privacy, 
	the enclosed variables are only in scope within the containing (outer) function. 
	You can’t get at the data from an outside scope except through the object’s privileged methods. 
	In JavaScript, any exposed method defined within the closure scope is privileged.

	Closures are also used when we need to partially apply functions and also for currying. The returned partial function is the privileged function
	that remembers the applied parameters.

	Closures are basically stateful functions
	 */ 

	@FunctionalInterface // optional
	public interface NumToTextConverter {
		String convert(int x);
	}

	// Type 1: Closure with custom Functional Interface and inner class
	static void closureType1Demo() {
		NumToTextConverter textOfWeekday = new NumToTextConverter() {
			String [] weeks = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
			@Override
			public String convert(int num) {
				return (num > 0 && num <= weeks.length) ? weeks[num-1] : null;
			};
		};
		System.out.println(textOfWeekday.convert(1)); // Mon
	}

	// Type 2: Closure with custom Functional Interface & Lambda expression
	static void closureType2Demo() {
		NumToTextConverter textOfWeekday = num -> {
			String [] weeks = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
			return (num > 0 && num <= weeks.length) ? weeks[num-1] : null;
		};
		System.out.println(textOfWeekday.convert(2)); // Tue
	}

	// Type 3: Closure with pre-defined Functional Interface, with Lambda expression
	static void closureType3Demo() {
		Function<Integer, String> getTextOfWeekday = num -> {
			String [] weeks = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
			return (num > 0 && num <= weeks.length) ? weeks[num-1] : null;
		}; 
		System.out.println(getTextOfWeekday.apply(3)); // Wed
	}

	// Type 4: [ACTUAL] Closure with pre-defined Functional Interface, with Lambda expression, with inner function 
	// having access to parent scope (String [] weeks)	
	static Function<Integer, String> getTextOfWeekday() {
		String [] weeks = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
		return num -> (num > 0 && num <= weeks.length) ? weeks[num-1] : null; // privileged inner function that encloses/remembers weeks
	}
	static void closureType4Demo() {
		System.out.println(getTextOfWeekday().apply(4)); // Thu
	}

	// Type 5: Closure with pre-defined Functional Interface, with Lambda expression, with inner function having access to parent scope
	// parent scope, in this scope, is nothing but state passed by client
	static Function<Integer, String> getTextOfWeekday(String [] weeks) {
		return num -> (num > 0 && num <= weeks.length) ? weeks[num-1] : null;
	}
	static void closureType5Demo() {
		Function<Integer, String> getArabTextOfWeekday = getTextOfWeekday(new String[]{ "Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu"});
		Function<Integer, String> getIndianTextOfWeekday = getTextOfWeekday(new String[]{ "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
		System.out.println(getArabTextOfWeekday.apply(5)); // Tue
		System.out.println(getIndianTextOfWeekday.apply(5)); // Fri
	}


	// Type 6: Closure with lambda and with pre-defined Functional Interface, demonstrating the fact that
	// Lambdas/CLosures in Java 8 cover values and not variables
	static Supplier<Integer> getNextInSequence() {
		int lastInSequence = 0;
		return () -> lastInSequence + 1; // ++lastSequence will be a compiler error coz Java 8 forces the parent scope variables (non heaps) to be immutable
	}; 
	static void closureType6Demo() {
		System.out.println(getNextInSequence().get()); // 1
		System.out.println(getNextInSequence().get()); // 1 and not 2
	}

	// Type 7: Closure, used to partially apply
	static Function<Integer, Integer> partial(BiFunction<Integer, Integer, Integer> fn, Integer x) {
		return y -> fn.apply(x, y); // privileged inner function that remembers x (value and not variable)
	}
	static void closureType7Demo() {
		BiFunction<Integer, Integer, Integer> add = (x, y) -> x + y;
		Function<Integer, Integer> tenAdder = partial(add, 10);
		System.out.println(tenAdder.apply(5));
		Function<Integer, Integer> twentyAdder = partial(add, 20);
		System.out.println(twentyAdder.apply(5));
	}

}
