package org.example.templates;

import java.util.ArrayList;
import java.util.Date;

public class Team {
    private String name;
    private Date date;
    private ArrayList<String> team;
    private ArrayList<Integer> draftOrder;

    public Team(String name, Date date, ArrayList<String> team, ArrayList<Integer> draftOrder) {
        this.name = name;
        this.date = date;
        this.team = team;
        this.draftOrder = draftOrder;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<String> getTeam() {
        return team;
    }

    public ArrayList<Integer> getDraftOrder() {
        return draftOrder;
    }
}
