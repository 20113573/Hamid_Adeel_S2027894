package com.example.hamid_adeel_s2027894.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RoadWork implements Serializable
{



    //Model Class For CurrentIncident
    // To Store Data Related RoadWork


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss=georss;
    }

    public void setPubDate(String pubDate) {
        this.pubDate=pubDate;
    }

    public String getStartDate() {


        if(startDate!=null && !startDate.isEmpty())
        {
            return  startDate.split("Start Date:")[1].trim();
        }
        else
            return "";
    }

    public String getFormattedStartDate() {


       if(!getStartDate().isEmpty())
       {
           return new SimpleDateFormat("dd MMM yyyy HH:mm a",Locale.ENGLISH).format(new Date(getStartDate()));
       }
       else
           return "";
    }

    public String getFormattedEndDate() {


        if(!getEndDate().isEmpty())
        {
            return new SimpleDateFormat("dd MMM yyyy HH:mm a",Locale.ENGLISH).format(new Date(getEndDate()));
        }
        else
            return "";
    }

    public void setStartDate(String startDate) {
        this.startDate=startDate;
    }

    public String getEndDate() {
        if(endDate!=null && !endDate.isEmpty() )
        {
            return  endDate.split("End Date:")[1].trim();
        }
        else
            return "";
    }

    public void setEndDate(String endDate) {
        this.endDate=endDate;
    }

    public String getWork() {
        if(work!=null && !work.isEmpty())
        {
            return  work.split("Works:")[1].trim();
        }
        else
            return "N/A";
    }

    public void setWork(String work) {
        this.work=work;
    }

    String title;
    String georss;
    String pubDate;
    String startDate;
    String endDate;
    String work;

    public String getLocation() {

        if(location==null || location.isEmpty())
            return "N/A";

        return location;
    }

    public void setLocation(String location) {
        this.location=location;
    }

    String location;

    public String getPlannedRoadWorkType() {
        if(type!=null && !type.isEmpty())
        {
            return  type.split("TYPE :")[1].trim();
        }
        else
            return "N/A";
    }
    public String getRoadWorkType() {
        if(title!=null && !title.isEmpty())
        {
            String[] info=title.split(" - ");
            if(info.length<=1)
                return  "N/A";

            return  info[info.length-1];
        }
        else
            return  "N/A";
    }

    public void setType(String type) {
        this.type=type;
    }

    String type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    String description;

    public String getPubDate() {

        return pubDate == null ? "" : new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(new Date(pubDate));
    }



    @Override
    public String toString() {
        return "PlannedRoadWork{" +
                "title='" + title + '\'' +
                ", georss='" + georss + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", work='" + work + '\'' +
                '}';
    }
}
