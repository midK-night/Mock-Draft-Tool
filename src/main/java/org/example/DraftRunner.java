package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.example.Data.*;
import org.example.Models.Draftboard;
import org.example.Models.Person;
import org.example.Models.adpRetrieve;

public class DraftRunner {
    public static void main() throws IOException {
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
        draftboard = new Draftboard(players, position, ADP, positions, ids);

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

        System.out.print("Would you like to save this team? (y/n): ");
        boolean save = console.nextLine().equalsIgnoreCase("y");
        if (save) {
            System.out.print("What is your team's name: ");
            String name = console.nextLine();
            teamSaver(draftboard, name);
        }
        console.close();
    }

    public static adpRetrieve adpRetrieve(String type) throws IOException {
        adpRetrieve adpRetrieve;

        String dir = System.getProperty("user.dir");
        String local = dir + "\\src\\main\\java\\org\\example\\data\\ADP.json";

        ObjectMapper mapper = new ObjectMapper();
        Root root = mapper.readValue(new File(local), Root.class);
        if (type.equalsIgnoreCase("ADP")) {
            ArrayList<String> adp = new ArrayList<>();
            Iterator<Adp> iterator = root.adp.iterator();
            while (iterator.hasNext()) {
                adp.add(iterator.next().player.name);
                iterator.remove();
            }
            adpRetrieve = new adpRetrieve(adp.toArray(new String[adp.size()]));
        } else {
            ArrayList<Integer> adp = new ArrayList<>();
            Iterator<Adp> iterator = root.adp.iterator();
            while (iterator.hasNext()) {
                String pos = iterator.next().player.position;
                if (pos.equals("\"QB\"")) {
                    adp.add(1);
                } else if (pos.equals("\"RB\"")) {
                    adp.add(4);
                } else if (pos.equals("\"WR\"")) {
                    adp.add(2);
                } else if (pos.equals("\"TE\"")) {
                    adp.add(3);
                }
                iterator.remove();
            }

            adpRetrieve = new adpRetrieve(adp.toArray(new Integer[adp.size()]));
        }

        return adpRetrieve;
    }

    public static int[] idRetriever() throws IOException {
        ArrayList<Integer> ids = new ArrayList<>();

        String dir = System.getProperty("user.dir");
        String local = dir + "\\src\\main\\java\\org\\example\\data\\ADP.json";

        ObjectMapper mapper = new ObjectMapper();
        Root root = mapper.readValue(new File(local), Root.class);

        Iterator<Adp> iterator = root.adp.iterator();
        while (iterator.hasNext()) {
            Adp mainNode = iterator.next();
            int id = ((int) Integer.parseInt(mainNode.player.sleeperId));
            ids.add(id);
            iterator.remove();
        }

        int[] idsArray = new int[ids.size()];
        for (int i = 0; i < idsArray.length; i++) {
            idsArray[i] = ids.get(i);
        }
        return idsArray;
    }

    public static void teamSaver(Draftboard d, String name) {
        Date date = new Date();
        String str = "{\n";

        str += "\"name\": \"" + name + "\",\n";
        str += "\"month\": \"" + date.getMonth() + "\",\n";
        str += "\"date\": \"" + date.getDate() + "\",\n";
        str += "\"year\": \"" + date.getYear() + "\",\n";
        str += "\"players\": [";

        Person p = d.getPlayers().get(d.getPlayerDraftPos()).getPerson();
        for (int i : p.getRoster()) {
            String tempPlayer = d.getADP()[p.getRoster().get(i)];
            str += "\"" + tempPlayer + "\",\n";
        }
        str += "]\n";

        ArrayList<Integer> temp = d.getDraftOrder();
        for (int i : temp) {
            str += "" + i + ",\n";
        }

        str += "]\n}\n]";

        try {
            String local = System.getProperty("user.dir");
            local += "\\src\\main\\java\\org\\example\\data\\player_teams.json";
            BufferedWriter writer = new BufferedWriter(new FileWriter(local));
            lastLineDelete(local);
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void lastLineDelete(String filename) {
        File inputFile = new File(filename);
        File tempFile = new File("mytemp.json");

        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            String temp = "";
            while ((currentLine = br.readLine()) != null) {
                temp += currentLine;
            }
            br.close();
            int lines = countLines(temp);

            br = new BufferedReader(new FileReader(inputFile));
            for (int i = 0; i < lines - 1; i++) {
                currentLine = br.readLine();
                bw.write(currentLine);
            }

            br.close();
            bw.close();

            tempFile.renameTo(inputFile);
            inputFile.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int countLines(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        int lines = 1;
        int pos = 0;
        while ((pos = str.indexOf("\n", pos) + 1) != 0) {
            lines++;
        }
        return lines;
    }
}