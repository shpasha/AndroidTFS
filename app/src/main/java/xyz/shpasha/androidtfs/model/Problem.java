package xyz.shpasha.androidtfs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Problem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("problem")
    @Expose
    private Problem_ problem;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("attempts_left")
    @Expose
    private Integer attemptsLeft;
    @SerializedName("report_attempts_left")
    @Expose
    private Integer reportAttemptsLeft;
    @SerializedName("last_submission")
    @Expose
    private File lastSubmission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Problem_ getProblem() {
        return problem;
    }

    public void setProblem(Problem_ problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAttemptsLeft() {
        return attemptsLeft;
    }

    public void setAttemptsLeft(Integer attemptsLeft) {
        this.attemptsLeft = attemptsLeft;
    }

    public Integer getReportAttemptsLeft() {
        return reportAttemptsLeft;
    }

    public void setReportAttemptsLeft(Integer reportAttemptsLeft) {
        this.reportAttemptsLeft = reportAttemptsLeft;
    }

    public File getLastSubmission() {
        return lastSubmission;
    }

    public void setLastSubmission(File lastSubmission) {
        this.lastSubmission = lastSubmission;
    }

}