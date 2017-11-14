import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

public class Serializer {

	Document myDoc = new Document(new Element("Serialized"));
	Map hash = new IdentityHashMap();
	String size =  String.valueOf(hash.size());
	
	public Serializer(Document document) {
		Document myDoc = document;
	}
	
	public void hashAdd(Object obj, Map hash) {
		hash.put(obj, size);
		size = String.valueOf(hash.size());
		
	}
	
	
	public org.jdom2.Document serialize(Object obj) throws IllegalAccessException{
		Class objClass = obj.getClass();
		Element objElem = new Element("Object");
		
		objElem.setAttribute("Class", objClass.getName());
		objElem.setAttribute("id", size);
		myDoc.getRootElement().addContent(objElem);
		if (!objClass.isArray()) {
			Field[] myFields = objClass.getFields();
			for (Field field:myFields){
				field.setAccessible(true);
				Class fieldClass = field.getClass();
				Class declaringClass = field.getDeclaringClass();
				hashAdd(field, hash);
				Element fieldElem = new Element("Field");
				fieldElem.setAttribute("Class", fieldClass.getName());
				serializeFieldVar(fieldElem, field, field.get(obj));
				objElem.addContent(fieldElem);
				
			}	
		}
		return myDoc;
	}
	
	//First param is parent element, second is the field itself
	public void serializeFieldVar(Element parent, Field myField, Object obj) throws IllegalAccessException {
		//value or reference
		//get element name and element value/reference
		Class fieldClass = myField.getClass();
		if (!hash.containsKey(myField)){
			serialize(obj);
			
		}
		else {
			
		}
		
		String elementName;
		String storeValue;
		if (fieldClass.isPrimitive()){
			elementName = "Value";
			storeValue = String.valueOf(myField.get(obj));
			//storeValue = 
		}
		else {
			elementName = "Reference";
			String referenceNum;
			if (!hash.containsKey(myField)) {
				serialize(myField);
				storeValue = String.valueOf(Integer.parseInt(size)-1);
			}
			else {
				storeValue = String.valueOf(hash.get(myField));
			}
		}
		Element varElem = new Element(elementName);
		System.out.println(storeValue);
		varElem.addContent(storeValue);
		
		
		
	}
}
