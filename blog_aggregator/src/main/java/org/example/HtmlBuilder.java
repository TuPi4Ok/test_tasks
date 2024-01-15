package org.example;

import org.example.resources.Resources;
import org.example.rss.RssItemDom;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static j2html.TagCreator.*;

public class HtmlBuilder {

    public static String build(Resources resources) throws ParserConfigurationException, IOException, SAXException {
        RssParser rssParser = new RssParser(resources);
        List<RssItemDom> rssItemDomList = rssParser.parse();
        rssItemDomList.removeIf(rss -> rss.getPubDate() == null);
        rssItemDomList.sort(Comparator.comparing(RssItemDom::getPubDate));
        var countPosts = 50;
        rssItemDomList = rssItemDomList.subList(0, countPosts);
        return html(
                head(
                        title("Blog aggregator")
                ),
                body(
                        main(attrs("#main.content"),
                                h1("50 latest posts"),
                                each(rssItemDomList, list ->
                                        form(
                                                button(text(list.getTitle()))
                                        ).withAction(list.getLink())
                                )
                        )
                )
        ).render();
    }
}
