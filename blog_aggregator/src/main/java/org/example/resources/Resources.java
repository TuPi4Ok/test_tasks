package org.example.resources;

import lombok.Getter;
import lombok.Setter;
import org.example.Main;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "resources")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Resources {
    @XmlElement(name = "item")
    private List<String> items;

    public void readItems(String fileName) throws FileNotFoundException, JAXBException {
        var resourcePath = Main.class.getClassLoader().getResource(fileName).getFile();
        BufferedReader br = new BufferedReader(new FileReader(resourcePath));
        String body = br.lines().collect(Collectors.joining());
        StringReader reader = new StringReader(body);
        JAXBContext context = JAXBContext.newInstance(Resources.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Resources resources = (Resources) unmarshaller.unmarshal(reader);
        this.items = resources.getItems();
    }
}
