import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLParserTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static void main(String[] args) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		long start = System.currentTimeMillis();
		
		// START building list of MBID
		String urlToParse = "http://ws.audioscrobbler.com/2.0/?method=tag.gettoptracks&tag=happy&api_key=6ea9780361e0c7342a8a08a1ad78dfec&limit=5";

		URL website = new URL(urlToParse);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("request.xml");
		fos.getChannel().transferFrom(rbc, 0, 1 << 24);

		File xmlFile = new File("request.xml");

		List<String> mbidList = new ArrayList<String>();
		
		Document document = (Document) builder.build(xmlFile);
		Element rootNode = document.getRootElement();
		List<Element> list = rootNode.getChildren("toptracks");
		
		for (int i = 0; i < list.size(); i++) {
			Element node = (Element) list.get(i);
			List<Element> subList = node.getChildren("track");
			for (int j = 0; j < subList.size(); j++) {
				Element subNode = (Element) subList.get(j);
				mbidList.add(subNode.getChildText("mbid"));
			}
		}
		// END building list of MBID
		
		// START building list of Info
		List<Info> tracksInfo = new ArrayList<Info>();
		for (String mbid : mbidList) {
			urlToParse = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=6ea9780361e0c7342a8a08a1ad78dfec&mbid=" + mbid;
			website = new URL(urlToParse);
			rbc = Channels.newChannel(website.openStream());
			fos = new FileOutputStream("request.xml");
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);

			xmlFile = new File("request.xml");

			document = (Document) builder.build(xmlFile);
			rootNode = document.getRootElement();
			list = rootNode.getChildren("track");
			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				String name = node.getChildText("name");
				int playCount = Integer.parseInt(node.getChildText("playcount"));
				int listeners = Integer.parseInt(node.getChildText("listeners"));
				List<Element> artistList = node.getChildren("artist");
				for (int j = 0; j < artistList.size(); j++) {
					Element artistElement = (Element) artistList.get(j);
					tracksInfo.add(new Info(name, 
							mbid, artistElement.getChildText("name"), 
							playCount, listeners));
				}
			}
		}
		// END building list of Info
		
		long end = System.currentTimeMillis();
		System.out.println(end - start + " ms");
		
		System.out.println(tracksInfo.toString());
	}

}