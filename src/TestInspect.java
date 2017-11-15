//By: Arthur Iwaniszyn 10124961
//CPSC 501 Assignment 2

import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Array;
import org.junit.Test;


public class TestInspect {

	@Test
	public void TestInterfaces() {
		ClassA a = new ClassA();
		Class [] aInterfaces = a.getClass().getInterfaces();
		assertEquals(aInterfaces, Inspector.inspectInterfaces(a.getClass()));
	}
	
	@Test
	public void TestFields() {
		ClassA a = new ClassA();
		Field[] aFields = a.getClass().getDeclaredFields();
		try {
			assertEquals(aFields, Inspector.inspectFields(a.getClass(), a));
	
		}catch(Exception e) {
			
		}
	}	
	
	@Test
	public void TestMethods() {
		ClassA a = new ClassA();
		Method[] aMethods = a.getClass().getDeclaredMethods();
		
		assertEquals(aMethods, Inspector.inspectMethods(a.getClass()));
	}	
	
	@Test
	public void TestConstructors() {
		ClassA a = new ClassA();
		Constructor[] aCons = a.getClass().getDeclaredConstructors();
		assertEquals(aCons, Inspector.inspectConstructors(a.getClass()));
		
	}
	
}
