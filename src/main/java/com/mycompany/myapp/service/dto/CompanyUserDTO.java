package com.mycompany.myapp.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CompanyUserDTO implements Serializable {

    private Long id;

    @NotNull
    private String userName;
    @NotNull
    private String idCardNum;
    @NotNull
    private String gender;
    @NotNull
    private String phoneNum;
    @NotNull
    private String email;

    @NotNull
    private Long deptId;
    @NotNull
    private String deptName;

    @NotNull
    private Long postId;

    @NotNull
    private String postName;
    @NotNull
    private Boolean enable;

    private String remark;

    public CompanyUserDTO() {
    }

    public CompanyUserDTO(Long id, String userName, String idCardNum, String gender, String phoneNum, String email, Long deptId, String deptName, Long postId, String postName, Boolean enable, String remark) {
        this.id = id;
        this.userName = userName;
        this.idCardNum = idCardNum;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.email = email;
        this.deptId = deptId;
        this.deptName = deptName;
        this.postId = postId;
        this.postName = postName;
        this.enable = enable;
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "CompanyUserDTO{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", idCardNum='" + idCardNum + '\'' +
            ", gender='" + gender + '\'' +
            ", phoneNum='" + phoneNum + '\'' +
            ", email='" + email + '\'' +
            ", deptId=" + deptId +
            ", deptName='" + deptName + '\'' +
            ", postId=" + postId +
            ", postName='" + postName + '\'' +
            ", enable=" + enable +
            ", remark='" + remark + '\'' +
            '}';
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
