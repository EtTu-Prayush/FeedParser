package org.example.Classifiers;

/** XML Parsing Dependencies*/
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;

/** Regex and Matcher*/

/** This class is named ClassifyFeed and its purpose is to classify the type of a feed based on its content.*/
public class ClassifyFeed {

    /**
     * This method classifies the type of a feed based on the provided content.
     *
     * @param content The content of the feed to be classified.
     * @return The type of the feed ("JSON", "ATOMRSS", "RSS1.0", "Unknown Feed Type", or "Error in parsing XML").
     */
    public String classify(String content) {
        if (content.trim().startsWith("{")) {
            return "JSON";
        }

        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (StringReader reader = new StringReader(content)) {
            XMLStreamReader xmlStreamReader = factory.createXMLStreamReader(reader);

            while (xmlStreamReader.hasNext()) {
                int event = xmlStreamReader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    String localName = xmlStreamReader.getLocalName();
                    String namespaceURI = xmlStreamReader.getNamespaceURI();

                    if ("rss".equals(localName)) {
                        String version = xmlStreamReader.getAttributeValue(null, "version");
                        if ("2.0".equals(version)) {
                            return "ATOMRSS";
                        }
                    } else if ("RDF".equals(localName) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI)) {
                        return "RSS1.0";
                    } else if ("feed".equals(localName)) {
                        return "ATOMRSS";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error in parsing XML";
        }

        return "Unknown Feed Type";
    }
}
