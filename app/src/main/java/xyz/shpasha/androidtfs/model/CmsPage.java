package xyz.shpasha.androidtfs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CmsPage {

    @SerializedName("unstyled_statement")
    @Expose
    private String unstyledStatement;

    public String getUnstyledStatement() {
        return unstyledStatement;
    }

    public void setUnstyledStatement(String unstyledStatement) {
        this.unstyledStatement = unstyledStatement;
    }
}
