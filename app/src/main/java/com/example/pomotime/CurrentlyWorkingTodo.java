package com.example.pomotime;

public class CurrentlyWorkingTodo {
    private int id;
    private String todo;

    public CurrentlyWorkingTodo() {

    }

    public CurrentlyWorkingTodo(int id, String todo) {
        this.id = id;
        this.todo = todo;
    }

    public int getId() {
        return id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Working on: " + todo;
    }
}
