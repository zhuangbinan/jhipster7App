package com.mycompany.myapp.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用来查表展示物业员工 基本信息, 部门, 岗位, 角色
 */
@ApiModel(description = "用来查表展示物业员工 基本信息, 部门, 岗位, 角色")
@Entity
@Table(name = "wamoli_company_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    @Column(name = "user_name")
    private String userName;

    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证号码")
    @Column(name = "id_card_num")
    private String idCardNum;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    @Column(name = "gender")
    private String gender;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    @Column(name = "phone_num")
    private String phoneNum;

    /**
     * email
     */
    @ApiModelProperty(value = "email")
    @Column(name = "email")
    private String email;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 职位名称
     */
    @ApiModelProperty(value = "职位名称")
    @Column(name = "post_name")
    private String postName;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    @Column(name = "enable")
    private Boolean enable;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 逻辑删除标识
     */
    @ApiModelProperty(value = "逻辑删除标识")
    @Column(name = "del_flag")
    private Boolean delFlag;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(name = "created_date")
    private Instant createdDate;

    /**
     * 最后修改者
     */
    @ApiModelProperty(value = "最后修改者")
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    /**
     * 最后修改者
     */
    @ApiModelProperty(value = "最后修改者")
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyUser id(Long id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return this.userName;
    }

    public CompanyUser userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCardNum() {
        return this.idCardNum;
    }

    public CompanyUser idCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
        return this;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getGender() {
        return this.gender;
    }

    public CompanyUser gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public CompanyUser phoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return this.email;
    }

    public CompanyUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public CompanyUser deptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPostName() {
        return this.postName;
    }

    public CompanyUser postName(String postName) {
        this.postName = postName;
        return this;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public CompanyUser enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return this.remark;
    }

    public CompanyUser remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getDelFlag() {
        return this.delFlag;
    }

    public CompanyUser delFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public CompanyUser createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public CompanyUser createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public CompanyUser lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public CompanyUser lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyUser)) {
            return false;
        }
        return id != null && id.equals(((CompanyUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyUser{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", idCardNum='" + getIdCardNum() + "'" +
            ", gender='" + getGender() + "'" +
            ", phoneNum='" + getPhoneNum() + "'" +
            ", email='" + getEmail() + "'" +
            ", deptName='" + getDeptName() + "'" +
            ", postName='" + getPostName() + "'" +
            ", enable='" + getEnable() + "'" +
            ", remark='" + getRemark() + "'" +
            ", delFlag='" + getDelFlag() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
