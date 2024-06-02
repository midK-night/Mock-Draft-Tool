package org.example.Models;

import java.util.ArrayList;
import java.util.Random;

public class Bot {
    private final ArrayList<Integer> roster;
    private final int attribute;

    public Bot() {
        Random rand = new Random();
        this.roster = new ArrayList<>();
        this.attribute = rand.nextInt(8) + 1;
    }

    public int onTheClock(Draftboard draftboard, int pick) {
        int location = -1;
        int QBs = posAmount(1, draftboard), WRs = posAmount(2, draftboard);
        int TEs = posAmount(3, draftboard), RBs = posAmount(4, draftboard);
        String[] ADP = draftboard.getADP();
        int[] positions = draftboard.getPositions();
        boolean[] isDrafted = draftboard.getDrafted();
        boolean validResponse = false;
        while (!validResponse) {
            switch (attribute) {
                case 1: // BPA
                    location = BPA(draftboard, pick);
                    break;
                case 2: // Hero RB
                    int round = (pick / draftboard.getPlayerAmount()) + 1;
                    if (round == 2) {
                        location = draftboard.highestADP(4);
                    } else if (round > 9) {
                        location = BPA(draftboard, pick);
                    } else {
                        boolean isRB = true;
                        int RB_tracker = draftboard.highestADP();
                        while (isRB) {
                            if (positions[RB_tracker] != 4) {
                                isRB = false;
                                location = RB_tracker;
                            } else {
                                RB_tracker++;
                            }
                        }
                    }
                    break;
                case 3: // QB Hoard
                    if (positions[draftboard.highestADP()] == 1 && (draftboard.highestADP() < pick)) {
                        location = draftboard.highestADP();
                    } else {
                        location = randomPos(draftboard);
                    }
                    break;
                case 4: // WR Heavy
                    heavyDraft WR = heavyDraft(draftboard, 2);
                    boolean WR_isDrafted = WR.getDraft();
                    if (WR_isDrafted) {
                        location = WR.getLocation();
                    } else {
                        location = randomPos(draftboard);
                    }
                    break;
                case 5: // TE Heavy
                    heavyDraft TE = heavyDraft(draftboard, 3);
                    boolean TE_isDrafted = TE.getDraft();
                    if (TE_isDrafted) {
                        location = TE.getLocation();
                    } else {
                        location = randomPos(draftboard);
                    }
                    break;
                case 6: // RB Heavy
                    heavyDraft RB = heavyDraft(draftboard, 4);
                    boolean RB_isDrafted = RB.getDraft();
                    if (RB_isDrafted) {
                        location = RB.getLocation();
                    } else {
                        location = randomPos(draftboard);
                    }
                    break;
                case 7: // Need
                    boolean hasQBs = QBs >= 3, hasWRs = WRs >= 5, hasTEs = TEs >= 4, hasRBs = RBs >= 4;
                    int needTracker = draftboard.highestADP();
                    boolean fitsNeeds = false;
                    while (!fitsNeeds) {
                        switch (positions[needTracker]) {
                            case 1:
                                if (!hasQBs) {
                                    location = needTracker;
                                    fitsNeeds = true;
                                    break;
                                }
                            case 2:
                                if (!hasWRs) {
                                    location = needTracker;
                                    fitsNeeds = true;
                                    break;
                                }
                            case 3:
                                if (!hasTEs) {
                                    location = needTracker;
                                    fitsNeeds = true;
                                    break;
                                }
                            case 4:
                                if (!hasRBs) {
                                    location = needTracker;
                                    fitsNeeds = true;
                                    break;
                                }
                        }
                        needTracker++;
                    }
                    break;
                case 8: // random pos
                    location = randomPos(draftboard);
                    break;
            }
            validResponse = !isDrafted[location];
        }
        roster.add(location);

        int currentRound = draftboard.getRound();
        int currentPick = 1 + (pick % currentRound);

        System.out.println(ADP[location] + " was drafted at pick " + (1 + pick) + ", which is round " + (currentRound)
                + " pick " + (currentPick));
        return location;
    }

    public int posAmount(int pos, Draftboard draftboard) {
        int posAmount = 0;
        for (Integer integer : roster) {
            if (draftboard.getPositions()[integer] == pos) {
                posAmount++;
            }
        }
        return posAmount;
    }

    public heavyDraft heavyDraft(Draftboard draftboard, int pos) {
        boolean isDrafted = false;
        int[] positions = draftboard.getPositions();
        boolean[] isDraft = draftboard.getDrafted();
        int location = -1;
        for (int i = 0; i < 5 && draftboard.highestADP() + i < positions.length; i++) {
            if (positions[draftboard.highestADP() + i] == pos && !isDraft[draftboard.highestADP() + i]) {
                location = draftboard.highestADP() + i;
                isDrafted = true;
                break;
            }
        }
        return new heavyDraft(location, isDrafted);
    }

    public int BPA(Draftboard draftboard, int pick) {
        int QBs = posAmount(1, draftboard), WRs = posAmount(2, draftboard);
        int TEs = posAmount(3, draftboard), RBs = posAmount(4, draftboard);
        int[] positions = draftboard.getPositions();
        int location = -1;
        int BPA_tracker = draftboard.highestADP();
        boolean BPA_break_tracker = false;
        for (int a = 0; a < 8; a++) {
            boolean qb_pos = (positions[BPA_tracker] == 1 && (QBs < 3 || BPA_tracker - 8 >= (pick)));
            boolean wr_pos = (positions[BPA_tracker] == 2 && (WRs <= 5 || BPA_tracker - 8 >= (pick)));
            boolean te_pos = (positions[BPA_tracker] == 3 && (TEs <= 4 || BPA_tracker - 8 >= (pick)));
            boolean rb_pos = (positions[BPA_tracker] == 4 && (RBs <= 4 || BPA_tracker - 8 >= (pick)));
            if (qb_pos || wr_pos || te_pos || rb_pos) {
                location = BPA_tracker;
                BPA_break_tracker = true;
                break;
            } else {
                BPA_tracker++;
            }
        }
        if (!BPA_break_tracker) {
            location = draftboard.highestADP();
        }
        return location;
    }

    public int randomPos(Draftboard draftboard) throws IllegalArgumentException {
        Random rng = new Random();
        int highestADP = draftboard.highestADP(), highestQB = draftboard.highestADP(1);
        int highestWR = draftboard.highestADP(2), highestTE = draftboard.highestADP(3);
        int highestRB = draftboard.highestADP(4);
        boolean reach = true;
        int random_pos = 0;
        while (reach) {
            random_pos = rng.nextInt(4) + 1;
            reach = switch (random_pos) {
                case 1 -> (highestQB - highestADP) >= 6;
                case 2 -> (highestWR - highestADP) >= 6;
                case 3 -> (highestTE - highestADP) >= 6;
                case 4 -> (highestRB - highestADP) >= 6;
                default -> throw new IllegalStateException("Unexpected value: " + random_pos);
            };
        }
        return switch (random_pos) {
            case 1 -> highestQB;
            case 2 -> highestWR;
            case 3 -> highestTE;
            case 4 -> highestRB;
            default -> throw new IllegalStateException("Unexpected value: " + random_pos);
        };
    }

    public int getAttribute() {
        return attribute;
    }

    public String rosterToString(String[] ADP) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < roster.size(); i++) {
            str.append("\nPick ").append(i).append(": ").append(ADP[roster.get(i)]);
        }
        return str.toString();
    }
}