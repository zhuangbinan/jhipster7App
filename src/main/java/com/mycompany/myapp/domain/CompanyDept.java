package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 物业公司部门
 */
@ApiModel(description = "物业公司部门")
@Entity
@Table(name = "wamoli_company_dept")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父部门id
     */
    @ApiModelProperty(value = "父部门id")
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 祖级列表
     */
    @ApiModelProperty(value = "祖级列表")
    @Column(name = "ancestors")
    private String ancestors;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 负责人名字
     */
    @ApiModelProperty(value = "负责人名字")
    @Column(name = "leader_name")
    private String leaderName;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    @Column(name = "tel")
    private String tel;

    /**
     * email
     */
    @ApiModelProperty(value = "email")
    @Column(name = "email")
    private String email;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    @Column(name = "enable")
    private Boolean enable;

    /**
     * 逻辑删除标识
     */
    @ApiModelProperty(value = "逻辑删除标识")
    @Column(name = "del_flag")
    private Boolean delFlag;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "last_modify_by")
    private String lastModifyBy;

    @Column(name = "last_modify_date")
    private Instant lastModifyDate;

    @ManyToMany(mappedBy = "companyDepts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "roomAddrs", "companyDepts", "companyPosts" }, allowSetters = true)
    private Set<WamoliUser> wamoliUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyDept id(Long id) {
        this.id = id;
        return this;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public CompanyDept parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return this.ancestors;
    }

    public CompanyDept ancestors(String ancestors) {
        this.ancestors = ancestors;
        return this;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public CompanyDept deptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public CompanyDept orderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getLeaderName() {
        return this.leaderName;
    }

    public CompanyDept leaderName(String leaderName) {
        this.leaderName = leaderName;
        return this;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getTel() {
        return this.tel;
    }

    public CompanyDept tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return this.email;
    }

    public CompanyDept email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public CompanyDept enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getDelFlag() {
        return this.delFlag;
    }

    public CompanyDept delFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public CompanyDept createBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public CompanyDept createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getLastModifyBy() {
        return this.lastModifyBy;
    }

    public CompanyDept lastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
        return this;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public Instant getLastModifyDate() {
        return this.lastModifyDate;
    }

    public CompanyDept lastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
        return this;
    }

    public void setLastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Set<WamoliUser> getWamoliUsers() {
        return this.wamoliUsers;
    }

    public CompanyDept wamoliUsers(Set<WamoliUser> wamoliUsers) {
        this.setWamoliUsers(wamoliUsers);
        return this;
    }

    public CompanyDept addWamoliUser(WamoliUser wamoliUser) {
        this.wamoliUsers.add(wamoliUser);
        wamoliUser.getCompanyDepts().add(this);
        return this;
    }

    public CompanyDept removeWamoliUser(WamoliUser wamoliUser) {
        this.wamoliUsers.remove(wamoliUser);
        wamoliUser.getCompanyDepts().remove(this);
        return this;
    }

    public void setWamoliUsers(Set<WamoliUser> wamoliUsers) {
        if (this.wamoliUsers != null) {
            this.wamoliUsers.forEach(i -> i.removeCompanyDept(this));
        }
        if (wamoliUsers != null) {
            wamoliUsers.forEach(i -> i.addCompanyDept(this));
        }
        this.wamoliUsers = wamoliUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyDept)) {
            return false;
        }
        return id != null && id.equals(((CompanyDept) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyDept{" +
            "id=" + getId() +
            ", parentId=" + getParentId() +
            ", ancestors='" + getAncestors() + "'" +
            ", deptName='" + getDeptName() + "'" +
            ", orderNum=" + getOrderNum() +
            ", leaderName='" + getLeaderName() + "'" +
            ", tel='" + getTel() + "'" +
            ", email='" + getEmail() + "'" +
            ", enable='" + getEnable() + "'" +
            ", delFlag='" + getDelFlag() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModifyBy='" + getLastModifyBy() + "'" +
            ", lastModifyDate='" + getLastModifyDate() + "'" +
            "}";
    }
}
