
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
public class Deserializer {
	int size;
	Map hashM = new HashMap();
	
	public Deserializer() {
	}
	private void hashAdd(Object obj, Map hash) {
		hash.put(size, obj);
		
	}
	
	public Object deserialize(org.jdom2.Document myDoc) {
		List elements = myDoc.getRootElement().getChildren();
		try {
			createInstances(elements);
			createFields(elements);
		}
		catch(Exception e) {
			
		}
		return (Object)hashM.get(0);
		
		
	}
	public void createInstances(List<Element> elements) {
		Class myClass = null;
		String className = null;
		for (Element e:elements) {
			Object myObj = null;
			try {
				className = String.valueOf(e.getAttributeValue("class"));
				myClass = Class.forName(className);
				if (myClass.isArray()) {
					myObj = Array.newInstance(Class.forName(className), Integer.parseInt(e.getAttributeValue("length")));
					hashM.put(e.getAttributeValue("id"), myObj);
				}
				else {
					Constructor myCons = myClass.getDeclaredConstructor(null);
					myCons.setAccessible(true);
					myObj = myCons.newInstance(null);
				}
				System.out.println(myObj);
				hashAdd(myObj, hashM);
				
			}
			catch(Exception e1) {
				continue;
			}
		}

	}
	
	public void createFields(List<Element> elements) throws Exception{
		for (Element e: elements) {
			List fieldElements = e.getChildren();
			Object value = hashM.get(e.getAttributeValue("id"));
			if (value.getClass().isArray()) {
				Class comptype = value.getClass().getComponentType();
				for (int j=0; j<fieldElements.size(); j++)
					Array.set(value, j, deserializeValue((Element)fieldElements.get(j), comptype, hashM));
			} 
			else {
				for (Object field : fieldElements) {
					Element fieldElement = (Element) field;
					String className = fieldElement.getAttributeValue("declaringclass");
					Class declaring = Class.forName(className);
					String attrName = fieldElement.getAttributeValue("name");
					Field f = declaring.getDeclaredField(attrName);
					f.setAccessible(true);
					Element cElement = fieldElement.getChildren().get(0);
					f.set(value, deserializeValue(cElement, f.getType(), hashM));
				}
			}
		}
	}
	
	
	public static Object deserializeValue(Element e, Class fieldClass, Map hashMap) {
		Object resultingObj;
		String valtype = e.getName();
		if (valtype.equals("null"))
			resultingObj = null;
		else if (valtype.equals("reference"))
			resultingObj = hashMap.get(e.getText());
		else {
			resultingObj = newPrimitive(e, fieldClass);
			
			
		}
		return resultingObj;
	}
	
	
	
	private static Object newPrimitive(Element e, Class fieldClass) {
		Object val;
		if (fieldClass.equals(boolean.class)) {
			if (e.getText().equals("true"))
				val = Boolean.TRUE;
			else
				val = Boolean.FALSE;
		}
		else if (fieldClass.equals(double.class))
			val = Double.valueOf(e.getText());
		else if (fieldClass.equals(short.class))
			val = Short.valueOf(e.getText());
		else if (fieldClass.equals(int.class))
			val = Integer.valueOf(e.getText());
		else if (fieldClass.equals(char.class))
			val = new Character(e.getText().charAt(0));
		else if (fieldClass.equals(byte.class))
			val = Byte.valueOf(e.getText());
		else if (fieldClass.equals(float.class))
			val = Float.valueOf(e.getText());	
		else if (fieldClass.equals(long.class))
			val = Long.valueOf(e.getText());
		else
			val = e.getText();
		return val;
		
	}
	
}
