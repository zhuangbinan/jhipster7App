package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.*;

/**
 * 增加 子元素 继承自CompanyDept
 */
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

    public List<CompanyDeptExtends> getChildren() {
        return children;
    }

    public void setChildren(List<CompanyDeptExtends> children) {
        this.children = children;
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
