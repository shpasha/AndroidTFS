package xyz.shpasha.androidtfs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contest {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rel_titles")
    @Expose
    private String relTitles;
    @SerializedName("time_left")
    @Expose
    private Integer timeLeft;
    @SerializedName("duration")
    @Expose
    private Integer duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelTitles() {
        return relTitles;
    }

    public void setRelTitles(String relTitles) {
        this.relTitles = relTitles;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

}