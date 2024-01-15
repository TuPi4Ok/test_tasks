package org.example;

import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.example.resources.Resources;
import org.example.rss.RssItemDom;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class RssParser {
    private List<String> rssList;
    private List<RssItemDom> rssItemList;
    private Resources resources;
    public RssParser(Resources resources) {
        this.resources = resources;
    }

    private void getRssFromHttp(Resources resources) throws IOException {
        var items = resources.getItems();
        rssList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        for (String item : items) {
            Request request = new Request.Builder()
                    .url(item)
                    .build();
            rssList.add(client.newCall(request).execute().body().string());
        }
    }

    public List<RssItemDom> parse() throws IOException, SAXException, ParserConfigurationException {
        getRssFromHttp(resources);
        rssItemList = new ArrayList<>();
        for (String rss : rssList) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(rss)));
            Node root = document.getFirstChild();
            parseNode(root.getChildNodes());
        }
        return rssItemList;
    }

    private void parseNode(NodeList child) {
        for (int i = 0; i < child.getLength(); i++) {
            Node element = child.item(i);
            if (!element.getNodeName().equals("item")) {
                parseNode(element.getChildNodes());
            } else {
                rssItemList.add(createRssItemDom(element));
            }
        }
    }
    private RssItemDom createRssItemDom(Node element) {
        var childItem = element.getChildNodes();
        var result = new RssItemDom();
        for (int i = 0; i < childItem.getLength(); i++) {
            Node element1 = childItem.item(i);
            if (element1.getNodeName().equals("link")) {
                result.setLink(element1.getTextContent());
            }
            if (element1.getNodeName().equals("title")) {
                result.setTitle(element1.getTextContent());
            }
            if (element1.getNodeName().equals("pubDate")) {
                result.setPubDate(element1.getTextContent());
            }
        }
        return result;
    }
}
