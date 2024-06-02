package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.example.Models.Team;

public class TeamReader {
    public static void main(String[] args) {
        Map<Integer, String> ids = mapMaker();
        List<Team> teams = teamList();
        Scanner console = new Scanner(System.in);

        System.out.println("Which team would you like to see?:");
        for (int i = 0; i < teams.size(); i++) {
            System.out.println("Team " + i + ":");
            System.out.println("    Name: " + teams.get(i).getName());
            System.out.println("    Date: " + teams.get(i).getDate());
            System.out.println();
        }

        System.out.print("Choose a team number: ");
        int teamNumber = console.nextInt();
        console.nextLine();
        System.out.println();

        callTeam(teams, teamNumber);

        System.out.print("Would you like to see the entire draft? (y/n): ");
        boolean draftWrite = console.nextLine().equalsIgnoreCase("y");

        console.close();

        if (draftWrite) {
            draftWriter(ids, teams.get(teamNumber));
        }
    }

    public static Map<Integer, String> mapMaker() {
        Map<Integer, String> map = new HashMap<>();
        String local = System.getProperty("user.dir");
        local += "\\src\\main\\java\\org\\example\\data\\players.json";
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(new File(local));
            for (int i = 1; i <= root.size(); i++) {
                String id = i + "";
                JsonNode node = root.get(id);
                if (node.isTextual()) {
                    String name = node.get("full_name").asText();
                    map.put(i, name);
                }
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    public static List<Team> teamList() {
        List<Team> list = new ArrayList<>();

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

    public static void callTeam(List<Team> teams, int teamNumber) {
        Team team = teams.get(teamNumber);

        System.out.println("Team " + teamNumber + ":");
        System.out.println("    Name: " + team.getName());
        System.out.println("    Date: " + team.getDate());
        System.out.println("    Team:");
        for (int i = 0; i < team.getTeam().size(); i++) {
            System.out.println("    Round " + i + ": " + team.getTeam().get(i));
        }

        System.out.println();
    }

    public static void draftWriter(Map<Integer, String> ids, Team team) {
        try {
            String local = System.getProperty("user.dir");
            local += "\\src\\main\\java\\org\\example\\data\\draft.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(local));

            for (int i = 0; i < team.getDraftOrder().size(); i++) {
                if (team.getDraftOrder().get(i) != null) {
                    bw.write(ids.get(team.getDraftOrder().get(i)) + ", ");
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
