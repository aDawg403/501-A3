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
	public void testGetHash(){
		Document d = new Document();
		Serializer s = new Serializer(d);
		Map m = new IdentityHashMap();
		int temp = m.size();
		s.hashAdd("HELLO", m);
		Object result = m.get(temp+1);
		assertEquals(result, "HELLO");
	}
	
	@Test
	public void testSerializeRoot() {
		Document d = new Document(new Element("serialized"));
		Serializer s = new Serializer(d);
		assertEquals(d.getRootElement(), new Element("serialized"));
	}

}
