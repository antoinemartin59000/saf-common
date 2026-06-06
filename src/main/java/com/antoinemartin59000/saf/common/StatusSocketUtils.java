package com.antoinemartin59000.saf.common;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StatusSocketUtils {

    public static Integer sendCommand(String host, int port, String command) {
        String statusSocketUri = "http://" + host + ":" + port;

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(statusSocketUri + "/" + command))
                .POST(BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println(statusSocketUri + " not reachable.");
            e.printStackTrace();
            return null;
        }

        return response.statusCode();
    }

    public static Map<String, String> extractFieldsFromHostAndPort(String host, int port) {
        String statusSocketUri = host + ":" + port;
        return extractFieldsFromUri(statusSocketUri);
    }

    private static Map<String, String> extractFieldsFromUri(String statusSocketUri) {

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + statusSocketUri))
                .timeout(Duration.ofMillis(1500))
                .GET()
                .build();

        System.out.println("sending request to " + statusSocketUri);

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println(statusSocketUri + " not reachable.");
            e.printStackTrace();
            return null;
        }

        if (response.statusCode() != 200) {
            System.err.println(statusSocketUri + " did not return 200:");
            System.out.println(response);
            return null;
        }

        String htmlBody = response.body();

        if (htmlBody == null) {
            return null;
        }

        return extractFieldsFromHtml(htmlBody);
    }

    private static Map<String, String> extractFieldsFromHtml(String statusSocketHtml) {

        Document document;
        try {
            document = XmlUtils.generateDocument(statusSocketHtml);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        Element fieldsTable = XmlUtils.getElementsByTagNameAndAttribute(document, "table", "id", "fields");

        if (fieldsTable == null) {
            throw new RuntimeException("No table with id='fields' found in status socket html.");
        }

        Map<String, String> result = new HashMap<>();

        // Iterate over <tr> rows
        NodeList rows = fieldsTable.getElementsByTagName("tr");
        for (int i = 0; i < rows.getLength(); i++) {
            Element tr = (Element) rows.item(i);
            NodeList cells = tr.getElementsByTagName("td");

            if (cells.getLength() >= 2) {
                String key = cells.item(0).getTextContent().trim();
                String value = cells.item(1).getTextContent().trim();
                result.put(key, value);
            }
        }

        return result;

    }

}
