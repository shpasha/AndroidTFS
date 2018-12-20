package xyz.shpasha.androidtfs.model;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Problem_ {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cms_page")
    @Expose
    private CmsPage cmsPage;
    @SerializedName("verbose_title")
    @Expose
    private String verboseTitle;
    @SerializedName("problem_type")
    @Expose
    private String problemType;
    @SerializedName("answer_choices")
    @Expose
    private ArrayList<String> answerChoices = null;
    @SerializedName("attempts_limit_type")
    @Expose
    private String attemptsLimitType;
    @SerializedName("ML")
    @Expose
    private String mL;
    @SerializedName("TL")
    @Expose
    private String tL;
    @SerializedName("problem_page_data")
    @Expose
    private Object problemPageData;
    @SerializedName("allowed_extensions")
    @Expose
    private String allowedExtensions;
    @SerializedName("max_submission_size")
    @Expose
    private Double maxSubmissionSize;
    @SerializedName("allowed_languages")
    @Expose
    private List<Object> allowedLanguages = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CmsPage getCmsPage() {
        return cmsPage;
    }

    public void setCmsPage(CmsPage cmsPage) {
        this.cmsPage = cmsPage;
    }

    public String getVerboseTitle() {
        return verboseTitle;
    }

    public void setVerboseTitle(String verboseTitle) {
        this.verboseTitle = verboseTitle;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public ArrayList<String> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(ArrayList<String> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public String getAttemptsLimitType() {
        return attemptsLimitType;
    }

    public void setAttemptsLimitType(String attemptsLimitType) {
        this.attemptsLimitType = attemptsLimitType;
    }

    public String getML() {
        return mL;
    }

    public void setML(String mL) {
        this.mL = mL;
    }

    public String getTL() {
        return tL;
    }

    public void setTL(String tL) {
        this.tL = tL;
    }

    public Object getProblemPageData() {
        return problemPageData;
    }

    public void setProblemPageData(Object problemPageData) {
        this.problemPageData = problemPageData;
    }

    public String getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(String allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    public Double getMaxSubmissionSize() {
        return maxSubmissionSize;
    }

    public void setMaxSubmissionSize(Double maxSubmissionSize) {
        this.maxSubmissionSize = maxSubmissionSize;
    }

    public List<Object> getAllowedLanguages() {
        return allowedLanguages;
    }

    public void setAllowedLanguages(List<Object> allowedLanguages) {
        this.allowedLanguages = allowedLanguages;
    }

}