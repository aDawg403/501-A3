//By: Arthur Iwaniszyn 10124961
//CPSC 501 Assignment 3
import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.Document;
import org.jdom2.Element;
import java.util.IdentityHashMap;
import java.util.Map;

public class testSerialize {

	@Test
	public void testHashAdd() {
		Document d = new Document();
		Serializer s = new Serializer(d);
		Map m = new IdentityHashMap();
		int temp = m.size();
		s.hashAdd("HELLO", m);
		assertEquals(temp + 1, m.size());
	}
	
	
	@Test
	public void testSerializeRoot() {
		Element e = new Element("serialized");
		Document d = new Document(e);
		Serializer s = new Serializer(d);
		assertEquals(d.getRootElement(), e);
	}
	
}
