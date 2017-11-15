
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.IdentityHashMap;
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

	String size = "";
	
	public Deserializer() {
		
	}
	public void hashAdd(Object obj, Map hash) {
		hash.put(obj, size);
		size = String.valueOf(hash.size());
		
	}
	
	public Object deserialize(org.jdom2.Document myDoc) {
		Map hashM = new IdentityHashMap();
		List elements = myDoc.getRootElement().getChildren();
		//Element firstElem = (Element)elements.get(0);
		//hashAdd(elements, hashM);
		try {
			createInstances(elements, hashM);
			createFields(elements, hashM);
		}
		catch(Exception e) {
			
		}
		
		
		return 1;
		
		
	}
	public void createInstances(List<Element> elements, Map hashM) {
		Class myClass = null;
		String className = null;
		for (Element e:elements) {
			Object myObj = null;
			try {
				className = String.valueOf(e.getAttributeValue("class"));
				myClass = Class.forName(className);
				if (myClass.isArray()) {
					myObj = Array.newInstance(Class.forName(className), Integer.parseInt(e.getAttributeValue("length")));
				}
				else {
					Constructor myCons = myClass.getConstructor();
					myCons.setAccessible(true);
					myObj = myCons.newInstance();
				}
				hashAdd(myObj, hashM);
			}
			catch(Exception e1) {
				continue;
			}
		}

	}
	
	public void createFields(List<Element> elements, Map hashM) throws Exception{
		for (Element e: elements) {
			List fieldElements = e.getChildren();
			Object value = hashM.get(e.getAttributeValue("id"));
			if (!value.getClass().isArray()) {
				for (Object field : fieldElements) {
					Element fieldElement = (Element) field;
					String className = fieldElement.getAttributeValue("declaringclass");
					Class fieldDC = Class.forName(className);
					String fieldName = fieldElement.getAttributeValue("name");
					Field f = fieldDC.getDeclaredField(fieldName);
					f.setAccessible(true);
					Element vElt = (Element) fieldElement.getChildren().get(0);
					f.set(value, deserializeValue(vElt, f.getType(), hashM));
				}
			} else {
				Class comptype = value.getClass().getComponentType();
				for (int j=0; j<fieldElements.size(); j++)
					Array.set(value, j, deserializeValue((Element)fieldElements.get(j), comptype, hashM));
			}
		}
	}
	
	
	public static Object deserializeValue(Element valueE, Class fieldType,Map table) {
		Object returnVal;
		String valtype = valueE.getName();
		if (valtype.equals("null"))
			return null;
		else if (valtype.equals("reference"))
			return table.get(valueE.getText());
		else {
			returnVal = testTypePrimative(fieldType, valueE);
			
			
		}
		return returnVal;
	}
	
	
	
	private static Object testTypePrimative(Class fieldType, Element valueE) {
		if (fieldType.equals(boolean.class)) {
			if (valueE.getText().equals("true"))
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		}
		
		else if (fieldType.equals(long.class))
			return Long.valueOf(valueE.getText());
		else if (fieldType.equals(float.class))
			return Float.valueOf(valueE.getText());
		else if (fieldType.equals(double.class))
			return Double.valueOf(valueE.getText());
		else if (fieldType.equals(char.class))
			return new Character(valueE.getText().charAt(0));
		else if (fieldType.equals(byte.class))
			return Byte.valueOf(valueE.getText());
		else if (fieldType.equals(short.class))
			return Short.valueOf(valueE.getText());
		else if (fieldType.equals(int.class))
			return Integer.valueOf(valueE.getText());
		else
			return valueE.getText();
		
	}
	
}
