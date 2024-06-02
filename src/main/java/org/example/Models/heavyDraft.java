package org.example.Models;

public class heavyDraft {
    static int location;
    static boolean isDrafted;

    public heavyDraft(int n, boolean draft) {
        location = n;
        isDrafted = draft;
    }

    public int getLocation() {
        return location;
    }

    public boolean getDraft() {
        return isDrafted;
    }
}
