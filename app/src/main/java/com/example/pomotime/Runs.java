package com.example.pomotime;

public class Runs {
    private int id;
    private int steps;
    private double maxSpeed;
    private int time;

    public Runs(int id, int steps, double maxSpeed, int time) {
        this.id = id;
        this.steps = steps;
        this.maxSpeed = maxSpeed;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
