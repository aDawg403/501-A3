
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;


import org.jdom2.Document;
import org.jdom2.Element;

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
		hashAdd(obj, hash);
		System.out.println("added class");
		if (!objClass.isArray()) {
			Field[] myFields = objClass.getFields();
			for (Field field:myFields){
				field.setAccessible(true);
				Class declaringClass = field.getDeclaringClass();
				Element fieldElem = new Element("field");
				fieldElem.setAttribute("name", field.getName());
				fieldElem.setAttribute("declaringclass", declaringClass.getName());
				objElem.addContent(fieldElem);	
				serializeFieldVar(fieldElem, field, obj);
			}	
		}
		else {
			objElem.setAttribute("length", String.valueOf(Array.getLength(obj)));
			String storageType = "";
			String storageVal;
			if (objClass.getComponentType().isPrimitive()) {
				storageType = "value";
				for (int i = 0; i < Array.getLength(obj); i++) {
					Element storingElement = new Element(storageType);
					storageVal = String.valueOf(Array.get(obj, i));
					storingElement.addContent(storageVal);
					objElem.addContent(storingElement);
				}
			}
			else {
				storageType = "reference";
				if (!hash.containsKey(obj.getClass().getComponentType())) {
					serializeClass(obj.getClass().getComponentType());
				}
				storageVal = String.valueOf(Integer.parseInt(size)-1);
				for (int i = 0; i < Array.getLength(obj); i++) {
					Element storingElement = new Element(storageType);
					storingElement.addContent(storageVal);
					objElem.addContent(storingElement);
				}	
			}
			
		}
		return myDoc;
	}
	
	public void serializeFieldVar(Element parent, Field myField, Object obj) throws IllegalAccessException {
		//assumes field is not an array
		Class fieldClass = myField.getType();
		if (!hash.containsKey(fieldClass)){
			serializeClass(fieldClass);
		}
		
		String elementName;
		String storeValue;
		if (fieldClass.isPrimitive()){
			elementName = "value";
			storeValue = String.valueOf(myField.get(obj));
		}
		else {
			elementName = "reference";
			storeValue = String.valueOf(hash.get(myField));
		}
		if (Modifier.isTransient(myField.getModifiers())) {
			storeValue = null;
		}
		
		Element storeElem = new Element(elementName);
		storeElem.addContent(storeValue);
		parent.addContent(storeElem);
	}
	
	public void serializeClass(Class myClass) {
		Element objElem = new Element("object");
		objElem.setAttribute("class", String.valueOf(myClass));
		objElem.setAttribute("id", size);
		myDoc.getRootElement().addContent(objElem);
		hashAdd(myClass, hash);
	}
	
}
