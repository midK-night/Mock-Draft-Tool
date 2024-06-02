package org.example;

public class adpRetrieve {
    private static Integer[] pos;
    private static String[] adp;

    public adpRetrieve(Integer[] integers) {
        pos = integers;
    }

    public adpRetrieve(String[] p) {
        adp = p;
    }

    public int[] getPos() {
        int[] positions = new int[pos.length];
        for (int i = 0; i < pos.length; i++) {
            positions[i] = pos[i].intValue();
        }
        return positions;
    }

    public String[] getAdp() {
        return adp;
    }
}
