
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Driver{
	
	
	public static void main(String[] args) {
		int port = 8091;
		
		//EDIT OBJECTS HERE ***************************************************************************************
		
		
		
		//ClassB test = new ClassB();				//Simple object with primitive fields
		//ClassA test = new ClassA();				//Object with references
		//ClassC test = new ClassC();				//Object with array of primitives
		//ClassD test = new ClassD();				//Object with array of references
		ClassE test = new ClassE();			//Object collection
		
		
		//EDIT OBJECTS HERE ***************************************************************************************
		try {
			Document myDoc = new Document(new Element("Serialized"));
			Serializer s = new Serializer(myDoc);
			Document doc = s.serialize(test);
			Deserializer d = new Deserializer();
			Object obj = d.deserialize(doc);
			Inspector.inspect(obj, true);
			//runSerializer(test, port);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
			
	}
	
	public static void runSerializer(Object transferObj, int portNum) throws Exception{
		Document myDoc = new Document(new Element("Serialized"));
		Serializer s = new Serializer(myDoc);
		System.out.println("Please enter the IP address of destination");
		
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		String destination = buff.readLine();
		Document doc = s.serialize(transferObj);
		File aFile = saveFile(doc);
		//make xml doc
		transfer(aFile, destination, portNum);
		//transfer to other computer
		
	}
	
	public static File saveFile(Document myDoc) throws Exception{
		File myFile = new File("objects.xml");
		BufferedWriter write = new BufferedWriter(new FileWriter(myFile));
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(myDoc, write);
		return myFile;
	}
	
	public static void transfer(File myFile, String destination, int port){
		try {
			Socket sock = new Socket(destination, port);
			OutputStream out = sock.getOutputStream();
			
			FileInputStream inputStream = new FileInputStream(myFile);
			byte[] buffer = new byte[1024 * 1024];
			int read = 0;
			System.out.println("test");
			while ((read = inputStream.read(buffer)) > 0) {
				System.out.println("test1");
				out.write(buffer, 0, read);
			}
			System.out.println("Finished transfer");
		}
		catch (Exception e) {
			System.out.print(e);
		}
		
	}
	
	
}
