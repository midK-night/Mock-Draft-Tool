package org.example.Models;

public class Player {
    private Person controlled;
    private Bot AI;

    public Player(Person manual) {
        controlled = manual;
    }

    public Player(Bot bot) {
        AI = bot;
    }

    public Person getPerson() {
        return controlled;
    }

    public Bot getBot() {
        return AI;
    }
}