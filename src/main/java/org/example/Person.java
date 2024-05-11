package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Person {
    private int pos;
    private ArrayList<Integer> roster;

    public Person(int draftPos) {
        pos = draftPos;
        roster = new ArrayList<>();
    }

    public int onTheClock(Draftboard draftboard, int pick) {
        Scanner console = new Scanner(System.in);
        draftboard.topAvailable(pick);
        boolean validResponse = false;
        int location = -1;
        while (!validResponse) {
            System.out.print("\nWho are you drafting? (Full Name): ");
            String input = console.nextLine();
            String[] ADP = draftboard.getADP();
            boolean[] isDrafted = draftboard.getDrafted();
            for (int track = 0; track < ADP.length; track++)
            {
                if (ADP[track].equalsIgnoreCase(input) && !isDrafted[track]) {
                    validResponse = true;
                    location = track;
                }
            }
            if (!validResponse) {
                System.out.println("Invalid response. Please enter full name or an player that has not already been drafted.");
            } else {
                roster.add(location);
            }
        }
        return location;
    }

    public int getPos() {
        return pos;
    }

    public String rosterToString(String[] ADP) {
        String str = "";
        for (int i = 0; i < roster.size(); i++) {
            str += "\nPick " + i + ": " + ADP[roster.get(i)];
        }
        return str;
    }
}
