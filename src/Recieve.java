import java.io.*;
import java.net.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Document;
import org.jdom2.JDOMException;

public class Recieve {
	
	public static void main(String[] args) {
		ServerSocket s = null;
		int portNum = 9091;
		try {
			s = new ServerSocket(portNum);
		}
		catch(Exception e) {
			System.out.println("Could not access port " + portNum);
			System.out.println(e);
			System.exit(0);
		}
		
		while(true) {
			System.out.println("Waiting for connection");
			File myFile = new File("objects.xml");
			Socket socket = null;
			try {
				socket = s.accept();
				System.out.println("Established connection");
				getFile(myFile, socket);
				Deserializer d = new Deserializer();
				Document doc = buildDoc(myFile);
				//deserialize the document and get primary object
				Object myObj = d.deserialize(doc);
				Inspector.inspect(myObj, true);
			}
			catch(Exception e) {
				System.out.println("Failed to establish connection");
				System.exit(0);
			}	
		}
	}
	
	
	public static Document buildDoc(File myFile) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = (Document)builder.build(myFile);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public static void getFile(File myFile, Socket mySocket) throws Exception{
		FileOutputStream out = new FileOutputStream(myFile);
		InputStream in = mySocket.getInputStream();
		int recievedBytes = 0;
		byte[] buff = new byte[1024 * 1024];
		while (in.read(buff) > 0) {
			recievedBytes = in.read(buff);
			out.write(buff, 0, recievedBytes);
			System.out.println(recievedBytes + " Bytes received.");
			break;
		}
	}
	
	
	
	
	
	
	
	
}
