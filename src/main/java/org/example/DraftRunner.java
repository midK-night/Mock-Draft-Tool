package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class DraftRunner {
    public static void main(String[] args) throws IOException {
        int[] ids = idRetriever();
        String[] ADP = adpRetrieve("ADP").getAdp();
        int[] positions = adpRetrieve("not adp").getPos();

        Draftboard draftboard;
        Scanner console = new Scanner(System.in);
        System.out.print("Enter the amount of players: ");
        int players = console.nextInt();
        console.nextLine();
        System.out.print("Will this be with bots? (y/n): ");
        boolean withBots = console.nextLine().equalsIgnoreCase("y");
        int position = -1;
        if (withBots) {
            System.out.print("Enter the manual draft position: ");
            position = console.nextInt() - 1;
            console.nextLine();
        }
        draftboard = new Draftboard(players, position, ADP, positions);

        System.out.print("Will this be with 3RR? (y/n): ");
        boolean with3RR = console.nextLine().equalsIgnoreCase("y");

        int rounds = ADP.length / players;
        if (rounds > 16) {
            rounds = 16;
        }
        boolean notReverse = true;
        for (int i = 0; i < rounds; i++) {
            if (withBots) {
                draftboard.round(i, notReverse, position);
            } else {
                draftboard.round(i, notReverse);
            }
            boolean stay = with3RR && i == 1;
            if (!stay) {
                notReverse = !notReverse;
            }
        }
    }

    public static adpRetrieve adpRetrieve(String type) throws IOException {
        adpRetrieve adpRetrieve;

        ObjectMapper mapper = new ObjectMapper();
        if (type.equalsIgnoreCase("ADP")) {
            ArrayList<String> adp = new ArrayList<>();
            JsonNode jsonNode = mapper.readTree(new File("./org/example/ADP.json"));
            Iterator<JsonNode> iterator = jsonNode.elements();
            while (iterator.hasNext()) {
                adp.add(iterator.next().get("name").toString());
                iterator.remove();
            }
            adpRetrieve = new adpRetrieve(adp.toArray(new String[adp.size()]));
        } else {
            ArrayList<Integer> adp = new ArrayList<>();
            JsonNode jsonNode = mapper.readTree(new File("./org/example/ADP.json"));
            Iterator<JsonNode> iterator = jsonNode.elements();
            while (iterator.hasNext()) {
                String pos = iterator.next().get("position").toString();
                if (pos.equals("QB")) {
                    adp.add(1);
                } else if (pos.equals("RB")) {
                    adp.add(4);
                } else if (pos.equals("WR")) {
                    adp.add(2);
                } else if (pos.equals("TE")) {
                    adp.add(3);
                }
                iterator.remove();
            }
            adpRetrieve = new adpRetrieve(adp.toArray(new String[adp.size()]));
        }

        return adpRetrieve;
    }

    public static int[] idRetriever() {
        ArrayList<Integer> ids = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(new File("./org/example/ADP.json"));
            Iterator<JsonNode> iterator = jsonNode.elements();
            while (iterator.hasNext()) {
                JsonNode node = iterator.next();
                ids.add(node.get("id").asInt());
                iterator.remove();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        int[] idsArray = new int[ids.size()];
        for (Integer i : ids) {
            idsArray[i] = i;
        }
        return idsArray;
    }


}