package org.example;

import org.example.resources.Resources;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws Exception {
        Resources resources = new Resources();
        resources.readItems("rss-resources.xml");
        var html = HtmlBuilder.build(resources);
        String filePath = "output.html";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(html);
            System.out.println("The file has been successfully created: " + filePath);
        } catch (IOException e) {
            System.err.println("Error during file creation: " + e.getMessage());
        }

    }
}