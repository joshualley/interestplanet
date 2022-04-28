package com.example.interestplanet.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * the model of articles
 */
@IgnoreExtraProperties
public class Article extends AbstractModel {
    private String title;
    private String authorId;
    @Exclude
    private User author;
    private String date;
    private String content;
    private String cover;
    private String planetId;
    @Exclude
    private Planet planet;

    public Article() {}

    public Article(String planetId, String authorId, String cover, String title, String content, String date) {
        this.cover = cover;
        this.title = title;
        this.authorId = authorId;
        this.date = date;
        this.content = content;
        this.planetId = planetId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public String getPlanetId() {
        return planetId;
    }
    public void setPlanetId(String planetId) {
        this.planetId = planetId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
