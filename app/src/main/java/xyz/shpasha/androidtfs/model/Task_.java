package xyz.shpasha.androidtfs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task_ {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("task_type")
    @Expose
    private String taskType;
    @SerializedName("max_score")
    @Expose
    private String maxScore;
    @SerializedName("deadline_date")
    @Expose
    private String deadlineDate;
    @SerializedName("contest_info")
    @Expose
    private ContestInfo contestInfo;
    @SerializedName("short_name")
    @Expose
    private String shortName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(String maxScore) {
        this.maxScore = maxScore;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public ContestInfo getContestInfo() {
        return contestInfo;
    }

    public void setContestInfo(ContestInfo contestInfo) {
        this.contestInfo = contestInfo;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}