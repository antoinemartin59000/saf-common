package com.antoinemartin59000.saf.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtils {

    public static Document generateDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes()));
    }

    public static Element getElementsByTagNameAndAttribute(Document document, String tagName, String attributeName, String attributeValue) {
        NodeList tables = document.getElementsByTagName(tagName);
        for (int i = 0; i < tables.getLength(); i++) {
            Element table = (Element) tables.item(i);
            if (attributeValue.equals(table.getAttribute(attributeName))) {
                return table;
            }
        }
        return null;
    }
}
