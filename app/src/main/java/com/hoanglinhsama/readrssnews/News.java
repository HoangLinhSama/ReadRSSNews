package com.hoanglinhsama.readrssnews;

public class News {
    private String title, description, picture;

    public News(String title, String description, String picture) {
        this.title = title;
        this.description = description;
        this.picture = picture;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
