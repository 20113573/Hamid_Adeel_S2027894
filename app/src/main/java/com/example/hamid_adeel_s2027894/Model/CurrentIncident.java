package com.example.hamid_adeel_s2027894.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CurrentIncident implements Serializable {


    //Model Class For CurrentIncident
    // To Store Data Related CurrentIncident


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location=location;
    }

    String location;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type=type;
    }

    String type;
    String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    String title;
    String georss;
    String author;
    String comments;
    String pubDate;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss=georss;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author=author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments=comments;
    }

    public  String  getStartDate()
    {
        return pubDate == null ? "" : new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date(pubDate));
    }
    public String getPubDate() {

        return pubDate == null ? "" : new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(new Date(pubDate));
    }

    public void setPubDate(String pubDate) {
        this.pubDate=pubDate;
    }



}
