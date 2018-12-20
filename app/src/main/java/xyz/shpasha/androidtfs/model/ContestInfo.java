package xyz.shpasha.androidtfs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContestInfo {
    @SerializedName("contest_status")
    @Expose
    private ContestStatus contestStatus;

    @SerializedName("contest_url")
    @Expose
    private String contestUrl;

    public ContestStatus getContestStatus() {
        return contestStatus;
    }

    public void setContestStatus(ContestStatus contestStatus) {
        this.contestStatus = contestStatus;
    }

    public String getContestUrl() {
        return contestUrl;
    }

    public void setContestUrl(String contestUrl) {
        this.contestUrl = contestUrl;
    }
}
