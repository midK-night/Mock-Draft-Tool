package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Draftboard {
    static ArrayList<Player> players;
    static String[] ADP;
    static int[] positions;
    static boolean[] isDrafted;
    public static int[] ids;

    static int playerDraftPos;
    private int round;

    static int[] QB_Index;
    static int[] WR_Index;
    static int[] TE_Index;
    static int[] RB_Index;

    public static ArrayList<Integer> draftboard;

    public Draftboard(int teamAmount, int playerDraftPos, String[] names, int[] pos, int[] ids) {
        this.playerDraftPos = playerDraftPos;
        players = new ArrayList<>();
        if (playerDraftPos == -1) {
            for (int i = 0; i < teamAmount; i++) {
                players.add(new Player(new Person(i)));
            }
        } else {
            for (int i = 0; i < teamAmount; i++) {
                if (playerDraftPos == i) {
                    players.add(new Player(new Person(i)));
                } else {
                    players.add(new Player(new Bot()));
                }
            }
        }
        ADP = names;
        positions = pos;
        isDrafted = new boolean[names.length];

        QB_Index = intArrayMaker(positions, 1);
        WR_Index = intArrayMaker(positions, 2);
        TE_Index = intArrayMaker(positions, 3);
        RB_Index = intArrayMaker(positions, 4);

        draftboard = new ArrayList<>();
        this.ids = ids;
    }

    public void round(int round, boolean oddRound, int playerPosition) {
        this.round = round + 1;
        Scanner console = new Scanner(System.in);
        System.out.println("\n*************\nROUND " + (round + 1) + "\n*************\n");
        if (oddRound) {
            for (int i = 0; i < players.size(); i++) {
                if (i == playerPosition) {
                    System.out.print("\nWould you like to see your roster? (y/n): ");
                    boolean roster = console.nextLine().equalsIgnoreCase("y");
                    if (roster) {
                        System.out.println(players.get(i).getPerson().rosterToString(ADP));
                    }
                    isDrafted[players.get(i).getPerson().onTheClock(this, (round * players.size()) + i)] = true;
                } else {
                    System.out.print("\nWould you like to see Bot " + (i + 1) + "'s roster? (y/n): ");
                    boolean roster = console.nextLine().equalsIgnoreCase("y");
                    if (roster) {
                        System.out.println(players.get(i).getBot().rosterToString(ADP));
                    }
                    System.out.print("Bot " + (i + 1) + "'s attribute: ");
                    Bot temp = players.get(i).getBot();
                    switch (temp.getAttribute()) {
                        case 1:
                            System.out.println("BPA");
                            break;
                        case 2:
                            System.out.println("Hero RB");
                            break;
                        case 3:
                            System.out.println("QB Hoard");
                            break;
                        case 4:
                            System.out.println("WR Heavy");
                            break;
                        case 5:
                            System.out.println("TE Heavy");
                            break;
                        case 6:
                            System.out.println("RB Heavy");
                            break;
                        case 7:
                            System.out.println("Need");
                            break;
                        case 8:
                            System.out.println("None");
                    }
                    int pick = temp.onTheClock(this, (round * players.size()) + i);
                    isDrafted[pick] = true;
                    draftboard.add(pick);
                }
            }
        } else {
            for (int i = players.size() - 1; i >= 0; i--) {
                if (i == playerPosition) {
                    System.out.print("\nWould you like to see your roster? (y/n): ");
                    boolean roster = console.nextLine().equalsIgnoreCase("y");
                    if (roster) {
                        System.out.println(players.get(i).getPerson().rosterToString(ADP));
                    }
                    isDrafted[players.get(i).getPerson().onTheClock(this, (round * players.size()) + i)] = true;
                } else {
                    System.out.print("\nWould you like to see Bot " + (i + 1) + "'s roster? (y/n): ");
                    boolean roster = console.nextLine().equalsIgnoreCase("y");
                    if (roster) {
                        System.out.println(players.get(i).getBot().rosterToString(ADP));
                    }
                    System.out.print("Bot " + (i + 1) + "'s attribute: ");
                    switch (players.get(i).getBot().getAttribute()) {
                        case 1:
                            System.out.println("BPA");
                            break;
                        case 2:
                            System.out.println("Hero RB");
                            break;
                        case 3:
                            System.out.println("QB Hoard");
                            break;
                        case 4:
                            System.out.println("WR Heavy");
                            break;
                        case 5:
                            System.out.println("TE Heavy");
                            break;
                        case 6:
                            System.out.println("RB Heavy");
                            break;
                        case 7:
                            System.out.println("Need");
                            break;
                        case 8:
                            System.out.println("None");
                    }
                    int pick = players.get(i).getBot().onTheClock(this, (round * players.size()) + (players.size() - i));
                    isDrafted[pick] = true;
                    draftboard.add(pick);
                }
            }
        }
    }

    public void round(int round, boolean oddRound) {
        this.round = round + 1;
        System.out.println("\n*************\nROUND " + round + "\n*************\n");
        if (oddRound) {
            for (int i = 0; i < players.size(); i++) {
                System.out.print("Would you like to see your roster? (true/false): ");
                boolean roster = Boolean.parseBoolean(System.console().readLine());
                if (roster) {
                    System.out.println(players.get(i).getPerson().rosterToString(ADP));
                }
                isDrafted[players.get(i).getPerson().onTheClock(this, (round * players.size()) + i)] = true;
            }
        } else {
            for (int i = players.size() - 1; i >= 0; i--) {
                System.out.print("Would you like to see your roster? (true/false): ");
                boolean roster = Boolean.parseBoolean(System.console().readLine());
                if (roster) {
                    System.out.println(players.get(i).getPerson().rosterToString(ADP));
                }
                isDrafted[players.get(i).getPerson().onTheClock(this, (round * players.size()) + i)] = true;
            }
        }
    }

    public void topAvailable(int pick) {
        System.out.println("\nRound " + (pick / players.size() + 1) + "\nPick " + (pick % players.size() + 1));
        top5();
        top5(1);
        top5(2);
        top5(3);
        top5(4);
    }

    public void top5() {
        System.out.println("\nTop 5 Overall Players");
        int ovrTrack = highestADP(), ovrPlayerTrack = 1;
        while (ovrPlayerTrack <= 5) {
            if (!isDrafted[ovrTrack]) {
                System.out.println(ADP[ovrTrack] + " - ADP: " + (ovrTrack + 1));
                ovrPlayerTrack++;
            }
            ovrTrack++;
        }
    }

    public void top5(int pos) {
        String position = switch (pos) {
            case 1 -> "QB";
            case 2 -> "WR";
            case 3 -> "TE";
            case 4 -> "RB";
            default -> "";
        };
        int ovrLow = highestADP(pos), ovrPlayerTrack = 1;
        int[] index = switch (pos) {
            case 1 -> QB_Index;
            case 2 -> WR_Index;
            case 3 -> TE_Index;
            case 4 -> RB_Index;
            default -> throw new IllegalStateException("Unexpected value: " + pos);
        };
        int ovrTrack = 0;
        for (int i = 0; i < index.length; i++) {
            if (index[i] == ovrLow) {
                ovrTrack = i;
            }
        }
        System.out.println("\nTop 5 " + position + ": ");
        while (ovrPlayerTrack <= 5) {
            if (!isDrafted[index[ovrTrack]]) {
                System.out.println(ADP[index[ovrTrack]] + " - ADP: " + (index[ovrTrack]));
                ovrPlayerTrack++;
            }
            ovrTrack++;
        }
    }

    public int highestADP() {
        int highestADP = positions.length;
        for (int i = 0; i < positions.length; i++) {
            if (!isDrafted[i]) {
                highestADP = Math.min(highestADP, i);
            }
            if (highestADP < positions.length) {
                break;
            }
        }
        return highestADP;
    }

    public int highestADP(int pos) {
        int highestPos = positions.length;
        int[] index = switch (pos) {
            case 1 -> QB_Index;
            case 2 -> WR_Index;
            case 3 -> TE_Index;
            case 4 -> RB_Index;
            default -> throw new IllegalStateException("Unexpected value: " + pos);
        };
        for (int i = 0; i < index.length; i++) {
            if (!isDrafted[index[i]]) {
                highestPos = Math.min(highestPos, index[i]);
            }
            if (highestPos < positions.length) {
                break;
            }
        }
        return highestPos;
    }

    public static int[] intArrayMaker(int[] pos, int position) {
        int amount = 0;
        for (int po : pos) {
            if (po == position) {
                amount++;
            }
        }
        int[] array = new int[amount];
        int tracker = 0;
        for (int i = 0; i < pos.length; i++) {
            if (pos[i] == position) {
                array[tracker] = i;
                tracker++;
            }
        }
        return array;
    }

    public String[] getADP() {
        return ADP;
    }

    public int getPlayerAmount() {
        return players.size();
    }

    public int[] getPositions() {
        return positions;
    }

    public boolean[] getDrafted() {
        return isDrafted;
    }

    public int getPlayerDraftPos() {
        return playerDraftPos;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getRound() {
        return round;
    }

    public ArrayList<Integer> getDraftOrder() {
        return draftboard;
    }
}