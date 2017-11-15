//By: Arthur Iwaniszyn 10124961
//CPSC 501 Assignment 2


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.util.Vector;

public class Inspector {
	
	
	static boolean recursive = false;
	static Vector inspectedClasses = new Vector();
	
	
	public static void inspect(Object myObject, boolean recurse) throws IllegalArgumentException, IllegalAccessException {
		recursive = recurse;
		Class classObject = myObject.getClass();
		
		String declaringClass = myObject.getClass().getSimpleName();
		System.out.println("-------------------------------------------------------------------------------------");
		System.out.println("The name of the declaring class is: " + declaringClass + "\n");
		
		if (classObject.isArray()) {
			System.out.println("The length of the Array object is " + Array.getLength(myObject) + "\n");
			System.out.println("Type: " + myObject.getClass());
		}
		
		String superClass = myObject.getClass().getSuperclass().getName();
		System.out.println("The name of the immediate superclass is: " + superClass + "\n");
		
		//name of interfaces implemented
		inspectInterfaces(classObject);
		inspectMethods(classObject);
		inspectConstructors(classObject);
		inspectFields(classObject, myObject);
		
		System.out.println("-------------------------------------------------------------------------------------");
		
		
		while (myObject.getClass().getSuperclass() != Object.class && !inspectedClasses.contains(classObject)){
			inspectedClasses.add(classObject);
			Class superC = myObject.getClass().getSuperclass();
			inspectClass(superC);
		}
	}
	
	public static void inspectClass(Class myClass) throws IllegalArgumentException, IllegalAccessException {
		//inspectedClasses = new Vector();
		Class classObject = myClass;
	
		String declaringClass = myClass.getSimpleName();
		System.out.println("\n-------------------------------------------------------------------------------------");
		
		System.out.println("The name of the declaring class is: " + declaringClass + "\n");
		Class superClass = myClass.getSuperclass();
		System.out.println("The name of the immediate superclass is: " + superClass + "\n");
		
		
		
		//name of interfaces implemented
		inspectInterfaces(classObject);
		inspectMethods(classObject);
		inspectConstructors(classObject);
		
		if(recursive) {
			Field[] classFields = classObject.getDeclaredFields();
			for(Field field : classFields) {
				inspectedClasses.add(field.getType());
				inspectClass(field.getType());
			}
		}
		
		
		
		
		System.out.println("\n-------------------------------------------------------------------------------------");
		if (myClass.getSuperclass() != Object.class && myClass.getSuperclass() != null){
			inspectedClasses.add(myClass);
			inspectClass(myClass.getSuperclass());
		}
	}
	
	
	
	
	
	
	
	
	
	
	

	public static Class[] inspectInterfaces(Class myObject){
		Class[] interfaces= myObject.getInterfaces();
		String className = myObject.getSimpleName();
		if (interfaces.length != 0) {
			for (Class i : interfaces) {
				String interfaceName = i.getSimpleName();
				System.out.println(className + " implements: " + interfaceName);
			
			//print methods and fields?
			}
		}
		else {
			System.out.println(className + " does not implement any interfaces");
		}
		return interfaces;
	}
	
	public static Constructor[] inspectConstructors(Class myObject) {
		Constructor[] constructors = myObject.getConstructors();
		int constructorCount = 0;
		
		for(Constructor constructor: constructors) {
			System.out.println("\nConstructor " + constructorCount + ": ");
			Class[] parameterTypes = constructor.getParameterTypes();
			if (parameterTypes.length != 0) {
				int parameterCount = 0;
				for (Class param: parameterTypes) {
					System.out.println("Parameter " + parameterCount + ": " + param.getSimpleName());
					parameterCount += 1;
				}
			}
			else {
				System.out.println("Constructor " + constructorCount + " does not take any parameters");
			}
			constructorCount += 1;
		
			int mod = constructor.getModifiers();
			String modifier = Modifier.toString(mod);
			System.out.println("Modifiers: " + modifier);
				
		}
		return constructors;
	}

	public static Method[] inspectMethods(Class myObject) {
		Method[] methods = myObject.getDeclaredMethods();
		String className = myObject.getSimpleName();
		if (methods.length != 0) {
			int count = 0;
			for (Method m: methods){
				String methodName = m.getName();
				System.out.println("\nMethod " + count + ": " + methodName);
				
				//Output Exceptions
				Class[] exceptions = m.getExceptionTypes();
				if (exceptions.length != 0) {
					for(Class e: exceptions) {
						System.out.println(methodName + " throws exception: " + e.getSimpleName());
					}
				}
				else {
					System.out.println(methodName + " does not throw any exceptions");
				}
				
				//Output Parameter types
				Class[] parameterTypes = m.getParameterTypes();
				if (parameterTypes.length != 0) {
					int parameterCount = 0;
					for (Class param: parameterTypes) {
						System.out.println("Parameter " + parameterCount + ": " + param.getSimpleName());
						parameterCount += 1;
					}
				}
				else {
					System.out.println(methodName + " does not have any parameters");
				}
				
				//Output return type
				Class returnClass = m.getReturnType();
				String returnType = returnClass.getSimpleName();
				System.out.println("Return type: " + returnType);
				
				
				//Get modifiers
				int mod = m.getModifiers();
				String modifier = Modifier.toString(mod);
				System.out.println("Modifiers: " + modifier);

				count++;	
			}
		}
		else {
			System.out.println(className + " does not have any methods");
		}
		return methods;
	}
	
	
	
	public static Field[] inspectFields(Class classObject, Object obj) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = classObject.getDeclaredFields();
		int fieldCount = 0;
		
		if (fields.length != 0) {
			System.out.println("Declared fields: ");
			for (Field field: fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				System.out.println("\nField " + fieldCount + ": " + fieldName);
				
				if (field.getType().isArray()) {
					System.out.println("Array Type: " + field.getType().getComponentType());
					 int length = Array.getLength(field.get(obj));
					 System.out.println("Array Length: " + length);
				}
				else {
					//String fieldClass = field.getName();
					System.out.println("Value: " + field.get(obj));
					System.out.println("Type: " + field.getType());
				}
				System.out.println("Modifier: " + Modifier.toString(field.getModifiers()));
				
				if (recursive) {
					inspectClass(field.getType());
				}
				fieldCount += 1;
			}
		}
		else {
			System.out.println("\n" + classObject.getSimpleName() + " does not contain any fields");
			
		}
		return fields;
		
	}

	
}
