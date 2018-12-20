package xyz.shpasha.androidtfs.model;

public class Answer {
    private int problemId;
    private String answers;

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getProblemId() {
        return problemId;
    }

    public String getAnswers() {
        return answers;
    }

    public Answer(int problemId, String answers) {
        this.problemId = problemId;
        this.answers = answers;
    }
}
