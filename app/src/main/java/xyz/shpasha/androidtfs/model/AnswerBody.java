package xyz.shpasha.androidtfs.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerBody {
    @SerializedName("answer")
    @Expose
    private String answer;

    @SerializedName("language")
    @Expose
    private Integer language = 1;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public AnswerBody(String answer) {
        this.answer = answer;
    }
}
