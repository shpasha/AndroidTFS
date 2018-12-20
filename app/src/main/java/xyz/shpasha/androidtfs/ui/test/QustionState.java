package xyz.shpasha.androidtfs.ui.test;

class QustionState {
    private Integer questionNum;
    private boolean isSent;
    private boolean isCurrent = false;

    void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public Integer getQuestionNum() {
        return questionNum;
    }

    void setSent(boolean isSent) {
        this.isSent = isSent;
    }

    boolean isSent() {
        return isSent;
    }

    void setCurrent(boolean current) {
        isCurrent = current;
    }

    boolean isCurrent() {
        return isCurrent;
    }
}
