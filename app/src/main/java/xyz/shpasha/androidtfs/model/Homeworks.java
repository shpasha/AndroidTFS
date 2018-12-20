package xyz.shpasha.androidtfs.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Homeworks {

    @SerializedName("homeworks")
    @Expose
    private List<Homework> homeworks = null;

    public List<Homework> getHomeworks() {
        return homeworks;
    }

    public void setHomeworks(List<Homework> homeworks) {
        this.homeworks = homeworks;
    }

}