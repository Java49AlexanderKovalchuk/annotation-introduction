package telran.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import telran.test.annotation.BeforeEach;
import telran.test.annotation.Test;

public class TestRunner implements Runnable {
	private Object testObj;
	
	public TestRunner(Object testObj) {
		super();
		this.testObj = testObj;
	} 

	@Override
	public void run() {
			Class<?> clazz = testObj.getClass(); 
			Method[] methods = clazz.getDeclaredMethods();
			Method[] beforeEachMethods = getBeforeEachMethods(methods);
			runTestMethods(methods, beforeEachMethods);
	
	}

	private Method[] getBeforeEachMethods(Method[] methods) {
		
		return Arrays.stream(methods).filter(m -> m.isAnnotationPresent(BeforeEach.class))
				.toArray(Method[]::new);
	} 

	private void runTestMethods(Method[] methods, Method[] beforeEachMethods) {
		for(Method method: methods) {
			if(method.isAnnotationPresent(Test.class)) {
				method.setAccessible(true);
				try {
					runMethods(beforeEachMethods);
					method.invoke(testObj);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private void runMethods(Method[] methods) {
		for(Method method: methods) {
			method.setAccessible(true);
			try {
				method.invoke(testObj);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}		
	}

}
//}
//			System.out.println("List of mtdh's names beforeEach annotated: " + namesBefore);
//			
//			for(Method method: methods) {
//				if(method.isAnnotationPresent(Test.class)) {
//					method.setAccessible(true);
//					callAllBeforeEach(clazz, namesBefore);
//					try {
//						
//						method.invoke(testObj);
//						
//					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//						System.out.println("error: " + e.getMessage());
//					}
//				}
//			} 
//	
//	}
//
//	private void callAllBeforeEach(Class<?> clazz, ArrayList<String> namesBefore) {
//		namesBefore.stream().forEach(n -> {
//			try {
//				Method methodBefore = clazz.getDeclaredMethod(n);
//				methodBefore.setAccessible(true);
//				methodBefore.invoke(testObj);
//			} catch (Exception e) {
//				System.out.println("er: " + e.getMessage());
//			}
//		});
//	}
//
//}
