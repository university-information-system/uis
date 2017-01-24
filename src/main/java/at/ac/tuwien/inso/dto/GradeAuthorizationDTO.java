package at.ac.tuwien.inso.dto;

import at.ac.tuwien.inso.entity.Grade;

public class GradeAuthorizationDTO {

    private Grade grade;
    private String authCode;

    public GradeAuthorizationDTO() {
    }

    public GradeAuthorizationDTO(Grade grade) {
        this.grade = grade;
    }

    public GradeAuthorizationDTO(Grade grade, String code) {
        this.grade = grade;
        this.authCode = code;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
