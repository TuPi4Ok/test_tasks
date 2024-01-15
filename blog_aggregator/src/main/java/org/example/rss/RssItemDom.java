package org.example.rss;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class RssItemDom {
    @XmlElement(name = "link")
    private String link;
    @XmlElement(name = "pubDate")
    private LocalDateTime pubDate;
    @XmlElement(name = "title")
    private String title;

    public void setPubDate(String pubDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        try {
            this.pubDate = LocalDateTime.parse(pubDate, formatter);
        } catch (DateTimeParseException e) {
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
            this.pubDate = LocalDateTime.parse(pubDate, formatter1);
        }
    }
}
