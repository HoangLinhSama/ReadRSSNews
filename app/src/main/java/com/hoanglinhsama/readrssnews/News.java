package com.hoanglinhsama.readrssnews;

public class News {
    private String title, publishDate, picture;

    public News(String title, String publishDate, String picture) {
        this.title = title;
        this.publishDate = publishDate;
        this.picture = picture;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getpublishDate() {
        return this.publishDate;
    }

    public void setpublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
