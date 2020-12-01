package com.example.pomotime;

public class ProgressItem {
    private int progress;
    private int id;

    public ProgressItem(int progress, int id) {
        this.progress = progress;
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
