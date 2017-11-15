import static org.junit.Assert.*;
import java.io.File;
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
