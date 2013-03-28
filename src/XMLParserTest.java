import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLParserTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		SAXBuilder builder = new SAXBuilder();
		String urlToParse = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=6ea9780361e0c7342a8a08a1ad78dfec&artist=cher&track=believe";
		
		long start = System.currentTimeMillis();
		URL website = new URL(urlToParse);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("request.xml");
		fos.getChannel().transferFrom(rbc, 0, 1 << 24);

		File xmlFile = new File("request.xml");

		try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("track");

			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				System.out.println("Name : " + node.getChildText("name"));
				System.out.println("Plays : " + node.getChildText("playcount"));
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		long end = System.currentTimeMillis();
		
		System.out.println(end - start + " ms");
	}
}