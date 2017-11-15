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
				System.out.println(socket);
				getFile(myFile, socket);
				System.out.println("Established connectio1");
				Deserializer d = new Deserializer();
				System.out.println("Established connection2");
				Document doc = buildDoc(myFile);
				System.out.println("Established connection3");
				//deserialize the document and get primary object
				Object myObj = d.deserialize(doc);
				Inspector.inspect(myObj, true);
			}
			catch(Exception e) {
				System.out.println(e);
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
		InputStream in = mySocket.getInputStream();
		FileOutputStream out = new FileOutputStream(myFile);
		int recievedBytes = 0;
		byte[] buff = new byte[1024 * 1024];
		System.out.println("before");
		while ((recievedBytes = in.read(buff)) > 0) {
			System.out.println("HELLO");
			//recievedBytes = in.read(buff);
			System.out.print("HELLO2");
			out.write(buff, 0, recievedBytes);
			System.out.print("HELLO3");
			System.out.println(recievedBytes + " Bytes received.");
			break;
		}
	}
	
	
	
	
	
	
	
	
}
