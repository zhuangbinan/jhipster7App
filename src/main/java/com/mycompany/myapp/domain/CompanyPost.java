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
 * 物业公司岗位
 */
@ApiModel(description = "物业公司岗位")
@Entity
@Table(name = "wamoli_company_post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompanyPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 岗位英文缩写 如 CEO CTO HR
     */
    @ApiModelProperty(value = "岗位英文缩写 如 CEO CTO HR")
    @Column(name = "post_code")
    private String postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    @Column(name = "post_name")
    private String postName;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "last_modify_by")
    private String lastModifyBy;

    @Column(name = "last_modify_date")
    private Instant lastModifyDate;

    @ManyToMany(mappedBy = "companyPosts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "companyDepts", "companyPosts" }, allowSetters = true)
    private Set<CompanyUser> companyUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyPost id(Long id) {
        this.id = id;
        return this;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public CompanyPost postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostName() {
        return this.postName;
    }

    public CompanyPost postName(String postName) {
        this.postName = postName;
        return this;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public CompanyPost orderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getRemark() {
        return this.remark;
    }

    public CompanyPost remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public CompanyPost enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public CompanyPost createBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public CompanyPost createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getLastModifyBy() {
        return this.lastModifyBy;
    }

    public CompanyPost lastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
        return this;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public Instant getLastModifyDate() {
        return this.lastModifyDate;
    }

    public CompanyPost lastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
        return this;
    }

    public void setLastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Set<CompanyUser> getCompanyUsers() {
        return this.companyUsers;
    }

    public CompanyPost companyUsers(Set<CompanyUser> companyUsers) {
        this.setCompanyUsers(companyUsers);
        return this;
    }

    public CompanyPost addCompanyUser(CompanyUser companyUser) {
        this.companyUsers.add(companyUser);
        companyUser.getCompanyPosts().add(this);
        return this;
    }

    public CompanyPost removeCompanyUser(CompanyUser companyUser) {
        this.companyUsers.remove(companyUser);
        companyUser.getCompanyPosts().remove(this);
        return this;
    }

    public void setCompanyUsers(Set<CompanyUser> companyUsers) {
        if (this.companyUsers != null) {
            this.companyUsers.forEach(i -> i.removeCompanyPost(this));
        }
        if (companyUsers != null) {
            companyUsers.forEach(i -> i.addCompanyPost(this));
        }
        this.companyUsers = companyUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyPost)) {
            return false;
        }
        return id != null && id.equals(((CompanyPost) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyPost{" +
            "id=" + getId() +
            ", postCode='" + getPostCode() + "'" +
            ", postName='" + getPostName() + "'" +
            ", orderNum=" + getOrderNum() +
            ", remark='" + getRemark() + "'" +
            ", enable='" + getEnable() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModifyBy='" + getLastModifyBy() + "'" +
            ", lastModifyDate='" + getLastModifyDate() + "'" +
            "}";
    }
}
