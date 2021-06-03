package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.*;

public class CompanyDeptExtends extends CompanyDept{

    private Long id;

    private Long parentId;

    private String ancestors;

    private String deptName;

    private Integer orderNum;

    private String leaderName;

    private String tel;

    private String email;

    private Boolean enable;

    private Boolean delFlag;

    private String createBy;

    private Instant createDate;

    private String lastModifyBy;

    private Instant lastModifyDate;

    private Set<CompanyPost> companyPosts = new HashSet<>();

    private Set<WamoliUser> wamoliUsers = new HashSet<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CompanyDeptExtends> children = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String getAncestors() {
        return ancestors;
    }

    @Override
    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @Override
    public String getDeptName() {
        return deptName;
    }

    @Override
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public Integer getOrderNum() {
        return orderNum;
    }

    @Override
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String getLeaderName() {
        return leaderName;
    }

    @Override
    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    @Override
    public String getTel() {
        return tel;
    }

    @Override
    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Boolean getEnable() {
        return enable;
    }

    @Override
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public Boolean getDelFlag() {
        return delFlag;
    }

    @Override
    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Instant getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    @Override
    public String getLastModifyBy() {
        return lastModifyBy;
    }

    @Override
    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    @Override
    public Instant getLastModifyDate() {
        return lastModifyDate;
    }

    @Override
    public void setLastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    @Override
    public Set<CompanyPost> getCompanyPosts() {
        return companyPosts;
    }

    @Override
    public void setCompanyPosts(Set<CompanyPost> companyPosts) {
        this.companyPosts = companyPosts;
    }

    @Override
    public Set<WamoliUser> getWamoliUsers() {
        return wamoliUsers;
    }

    @Override
    public void setWamoliUsers(Set<WamoliUser> wamoliUsers) {
        this.wamoliUsers = wamoliUsers;
    }

    public List<CompanyDeptExtends> getChildren() {
        return children;
    }

    public void setChildren(List<CompanyDeptExtends> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompanyDeptExtends that = (CompanyDeptExtends) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(ancestors, that.ancestors) &&
            Objects.equals(deptName, that.deptName) &&
            Objects.equals(orderNum, that.orderNum) &&
            Objects.equals(leaderName, that.leaderName) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(email, that.email) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(delFlag, that.delFlag) &&
            Objects.equals(createBy, that.createBy) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModifyBy, that.lastModifyBy) &&
            Objects.equals(lastModifyDate, that.lastModifyDate) &&
            Objects.equals(companyPosts, that.companyPosts) &&
            Objects.equals(wamoliUsers, that.wamoliUsers) &&
            Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, parentId, ancestors, deptName, orderNum, leaderName, tel, email, enable, delFlag, createBy, createDate, lastModifyBy, lastModifyDate, companyPosts, wamoliUsers, children);
    }

    @Override
    public String toString() {
        return "CompanyDeptExtends{" +
            "id=" + id +
            ", parentId=" + parentId +
            ", ancestors='" + ancestors + '\'' +
            ", deptName='" + deptName + '\'' +
            ", orderNum=" + orderNum +
            ", leaderName='" + leaderName + '\'' +
            ", tel='" + tel + '\'' +
            ", email='" + email + '\'' +
            ", enable=" + enable +
            ", delFlag=" + delFlag +
            ", createBy='" + createBy + '\'' +
            ", createDate=" + createDate +
            ", lastModifyBy='" + lastModifyBy + '\'' +
            ", lastModifyDate=" + lastModifyDate +
            ", companyPosts=" + companyPosts +
            ", wamoliUsers=" + wamoliUsers +
            ", children=" + children +
            '}';
    }
}
