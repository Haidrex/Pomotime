package com.example.pomotime;

public class ModelPost {
    int id;
    int done;
    int gaveup;
    int totalCount;
    public ModelPost(){

    }

    public ModelPost(int id, int done, int gaveup, int totalCount){
        this.id = id;
        this.done = done;
        this.gaveup = gaveup;
        this.totalCount = totalCount;
    }

    public int getId() {
        return id;
    }

    public int getDone() {
        return done;
    }

    public int getGaveup() {
        return gaveup;
    }

    public int getTotalCount(){
        return totalCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public void setGaveup(int gaveup) {
        this.gaveup = gaveup;
    }

    public void setTotalCount(int totalCount){
        this.totalCount = totalCount;
    }
}
