import org.jdom2.Document;
import org.jdom2.Element;

public class Driver {
	public static void main(String[] args) {
		Document myDoc = new Document(new Element("Serialized"));
		Serializer s = new Serializer(myDoc);
		
		ClassA test = new ClassA();
		
		//------------MAINLINE PROCEDURE------------
		try {
			s.serialize(test);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		String server = "localhost";
		int port = 8080;
		
		
	}
}
