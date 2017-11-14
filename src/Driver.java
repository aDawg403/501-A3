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

public class Driver {
	public static void main(String[] args) {
		Document myDoc = new Document(new Element("Serialized"));
		Serializer s = new Serializer(myDoc);
		String server = "localhost";
		int port = 8080;
		ClassA test = new ClassA();
		
		//------------MAINLINE PROCEDURE------------
		try {
			myDoc = s.serialize(test);
		}
		catch(Exception e) {
		}
		
		System.out.print(new XMLOutputter().outputString(myDoc));
		
		
	}
}
