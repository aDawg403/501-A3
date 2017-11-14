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

	Document myDoc = new Document(new Element("serialized"));
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
		Element objElem = new Element("object");
		
		objElem.setAttribute("class", objClass.getName());
		objElem.setAttribute("id", size);
		myDoc.getRootElement().addContent(objElem);
		if (!objClass.isArray()) {
			Field[] myFields = objClass.getFields();
			for (Field field:myFields){
				field.setAccessible(true);
				Class declaringClass = field.getDeclaringClass();
				hashAdd(field, hash);
				Element fieldElem = new Element("field");
				fieldElem.setAttribute("name", field.getName());
				fieldElem.setAttribute("declaringclass", declaringClass.getName());
				serializeFieldVar(fieldElem, field, field.get(obj));
				objElem.addContent(fieldElem);
				
			}	
		}
		else {
			//it is an array
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
		if (!hash.containsKey(myField)) {
			serialize(myField);
		}
		storeValue = String.valueOf(hash.get(myField));
		if (fieldClass.isPrimitive()){
			elementName = "value";
		}
		else {
			elementName = "reference";
		}
		Element varElem = new Element(elementName);
		if (Modifier.isTransient(myField.getModifiers())) {
			storeValue = null;
		}
		varElem.addContent(storeValue);
	}
}
