import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class XMLParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("test.xml");

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
	}
}