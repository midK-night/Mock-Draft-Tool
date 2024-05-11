package org.example;

public class adpRetrieve {
    private static int[] pos;
    private static String[] adp;

    public adpRetrieve(int[] p) {
        pos = p;
    }
    public adpRetrieve(String[] p) {
        adp = p;
    }

    public int[] getPos() {
        return pos;
    }
    public String[] getAdp() {
        return adp;
    }
}
