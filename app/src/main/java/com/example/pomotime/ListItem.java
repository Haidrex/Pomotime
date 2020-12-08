package com.example.pomotime;


public class ListItem {
    private int id;
    private String title;
    private String category;

    public ListItem() {

    }

    public ListItem(int id, String title, String category) {
        this.id = id;
        this.title = title;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" + "Category: " + category;
    }
}
