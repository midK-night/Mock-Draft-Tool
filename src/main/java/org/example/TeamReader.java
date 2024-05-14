package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class TeamReader {
    public static void main(String[] args) {
        String local = System.getProperty("user.dir");
        local += "\\src\\main\\java\\org\\example\\data\\teams.json";

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(local));
            Iterator<JsonNode> iterator = root.elements();
            while (iterator.hasNext()) {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
