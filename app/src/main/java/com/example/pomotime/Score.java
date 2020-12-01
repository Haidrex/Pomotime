package com.example.pomotime;

public class Score {
    private int id;
    private int done;
    private int giveup;

    public Score(int id, int done, int giveup) {
        this.id = id;
        this.done = done;
        this.giveup = giveup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getGiveup() {
        return giveup;
    }

    public void setGiveup(int giveup) {
        this.giveup = giveup;
    }
}
