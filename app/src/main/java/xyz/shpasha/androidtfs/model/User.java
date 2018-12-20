package xyz.shpasha.androidtfs.model;

import java.util.List;
import java.util.Objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id")
public class User  {

    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("phone_mobile")
    @Expose
    private String phoneMobile;
    @SerializedName("t_shirt_size")
    @Expose
    private String tShirtSize;
    @SerializedName("is_client")
    @Expose
    private Boolean isClient;
    @SerializedName("skype_login")
    @Expose
    private String skypeLogin;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("school_graduation")
    @Expose
    private String schoolGraduation;
    @SerializedName("university")
    @Expose
    private String university;
    @SerializedName("faculty")
    @Expose
    private String faculty;
    @SerializedName("university_graduation")
    @Expose
    private Integer universityGraduation;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("current_work")
    @Expose
    private String currentWork;
    @SerializedName("resume")
    @Expose
    private String resume;
    @SerializedName("notifications")
    @Expose
    @Ignore
    private List<Object> notifications = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("admin")
    @Expose
    private Boolean admin;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getTShirtSize() {
        return tShirtSize;
    }

    public void setTShirtSize(String tShirtSize) {
        this.tShirtSize = tShirtSize;
    }

    public Boolean getIsClient() {
        return isClient;
    }

    public void setIsClient(Boolean isClient) {
        this.isClient = isClient;
    }

    public String getSkypeLogin() {
        return skypeLogin;
    }

    public void setSkypeLogin(String skypeLogin) {
        this.skypeLogin = skypeLogin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchoolGraduation() {
        return schoolGraduation;
    }

    public void setSchoolGraduation(String schoolGraduation) {
        this.schoolGraduation = schoolGraduation;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Integer getUniversityGraduation() {
        return universityGraduation;
    }

    public void setUniversityGraduation(Integer universityGraduation) {
        this.universityGraduation = universityGraduation;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCurrentWork() {
        return currentWork;
    }

    public void setCurrentWork(String currentWork) {
        this.currentWork = currentWork;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public List<Object> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Object> notifications) {
        this.notifications = notifications;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (birthday != null ? !birthday.equals(user.birthday) : user.birthday != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (middleName != null ? !middleName.equals(user.middleName) : user.middleName != null) return false;
        if (phoneMobile != null ? !phoneMobile.equals(user.phoneMobile) : user.phoneMobile != null) return false;
        if (tShirtSize != null ? !tShirtSize.equals(user.tShirtSize) : user.tShirtSize != null) return false;
        if (isClient != null ? !isClient.equals(user.isClient) : user.isClient != null) return false;
        if (skypeLogin != null ? !skypeLogin.equals(user.skypeLogin) : user.skypeLogin != null) return false;
        if (description != null ? !description.equals(user.description) : user.description != null) return false;
        if (region != null ? !region.equals(user.region) : user.region != null) return false;
        if (school != null ? !school.equals(user.school) : user.school != null) return false;
        if (schoolGraduation != null ? !schoolGraduation.equals(user.schoolGraduation) : user.schoolGraduation != null)
            return false;
        if (university != null ? !university.equals(user.university) : user.university != null) return false;
        if (faculty != null ? !faculty.equals(user.faculty) : user.faculty != null) return false;
        if (universityGraduation != null ? !universityGraduation.equals(user.universityGraduation) : user.universityGraduation != null)
            return false;
        if (grade != null ? !grade.equals(user.grade) : user.grade != null) return false;
        if (department != null ? !department.equals(user.department) : user.department != null) return false;
        if (currentWork != null ? !currentWork.equals(user.currentWork) : user.currentWork != null) return false;
        if (resume != null ? !resume.equals(user.resume) : user.resume != null) return false;
        if (notifications != null ? !notifications.equals(user.notifications) : user.notifications != null)
            return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (admin != null ? !admin.equals(user.admin) : user.admin != null) return false;
        return avatar != null ? avatar.equals(user.avatar) : user.avatar == null;
    }

    @Override
    public int hashCode() {
        int result = birthday != null ? birthday.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (phoneMobile != null ? phoneMobile.hashCode() : 0);
        result = 31 * result + (tShirtSize != null ? tShirtSize.hashCode() : 0);
        result = 31 * result + (isClient != null ? isClient.hashCode() : 0);
        result = 31 * result + (skypeLogin != null ? skypeLogin.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (school != null ? school.hashCode() : 0);
        result = 31 * result + (schoolGraduation != null ? schoolGraduation.hashCode() : 0);
        result = 31 * result + (university != null ? university.hashCode() : 0);
        result = 31 * result + (faculty != null ? faculty.hashCode() : 0);
        result = 31 * result + (universityGraduation != null ? universityGraduation.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
        result = 31 * result + (currentWork != null ? currentWork.hashCode() : 0);
        result = 31 * result + (resume != null ? resume.hashCode() : 0);
        result = 31 * result + (notifications != null ? notifications.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (admin != null ? admin.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        return result;
    }
}