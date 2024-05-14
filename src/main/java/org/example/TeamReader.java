package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TeamReader {
    public static void main(String[] args) {
        Map<Integer,String> ids = mapMaker();
        List<Team> teams = teamList();


    }

    public static Map mapMaker() {
        Map map = new HashMap<Integer, String>();
        String local = System.getProperty("user.dir");
        local += "\\src\\main\\java\\org\\example\\data\\players.json";
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(local));
            Iterator<JsonNode> iterator = root.elements();
            for (int i = 1; iterator.hasNext(); i++) {
                int id = i;
                String name = iterator.next().get("full_name").asText();
                map.put(id, name);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    public static List<Team> teamList() {
        List<Team> list = new ArrayList<Team>();

        String local = System.getProperty("user.dir");
        local += "\\src\\main\\java\\org\\example\\data\\teams.json";

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(local));
            Iterator<JsonNode> iterator = root.elements();
            while (iterator.hasNext()) {
                Team temp;

                String name = iterator.next().get("team-name").asText();

                int year = Integer.parseInt(iterator.next().get("year").asText());
                int month = Integer.parseInt(iterator.next().get("month").asText());
                int date = Integer.parseInt(iterator.next().get("date").asText());
                Date day = new Date(year, month, date);

                Iterator<JsonNode> team = iterator.next().get("players").elements();
                ArrayList<String> players = new ArrayList<>();
                while (team.hasNext()) {
                    players.add(team.next().asText());
                }

                Iterator<JsonNode> playerDraftOrder = iterator.next().get("players").elements();
                ArrayList<Integer> draftOrder = new ArrayList<>();
                while (playerDraftOrder.hasNext()) {
                    draftOrder.add(Integer.parseInt(team.next().asText()));
                }

                temp = new Team(name, day, players, draftOrder);
                list.add(temp);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
