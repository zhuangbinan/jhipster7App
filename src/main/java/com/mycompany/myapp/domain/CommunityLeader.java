package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 社区居委会 人员介绍
 */
@ApiModel(description = "社区居委会 人员介绍")
@Entity
@Table(name = "wamoli_community_leader")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommunityLeader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 证件照
     */
    @ApiModelProperty(value = "证件照")
    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @Column(name = "real_name")
    private String realName;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    @Column(name = "tel")
    private String tel;

    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    @Column(name = "job_title")
    private String jobTitle;

    /**
     * 职责简述
     */
    @ApiModelProperty(value = "职责简述")
    @Column(name = "job_desc")
    private String jobDesc;

    /**
     * 职业生涯简述
     */
    @ApiModelProperty(value = "职业生涯简述")
    @Lob
    @Column(name = "job_career_desc")
    private String jobCareerDesc;

    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "del_flag")
    private Boolean delFlag;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "last_modify_date")
    private Instant lastModifyDate;

    @Column(name = "last_modify_by")
    private String lastModifyBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "communityNotices", "communityLeaders", "homelandStations" }, allowSetters = true)
    private Community community;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommunityLeader id(Long id) {
        this.id = id;
        return this;
    }

    public byte[] getAvatar() {
        return this.avatar;
    }

    public CommunityLeader avatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return this.avatarContentType;
    }

    public CommunityLeader avatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
        return this;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public String getRealName() {
        return this.realName;
    }

    public CommunityLeader realName(String realName) {
        this.realName = realName;
        return this;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTel() {
        return this.tel;
    }

    public CommunityLeader tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public CommunityLeader jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDesc() {
        return this.jobDesc;
    }

    public CommunityLeader jobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
        return this;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getJobCareerDesc() {
        return this.jobCareerDesc;
    }

    public CommunityLeader jobCareerDesc(String jobCareerDesc) {
        this.jobCareerDesc = jobCareerDesc;
        return this;
    }

    public void setJobCareerDesc(String jobCareerDesc) {
        this.jobCareerDesc = jobCareerDesc;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public CommunityLeader enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getDelFlag() {
        return this.delFlag;
    }

    public CommunityLeader delFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public CommunityLeader orderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Instant getLastModifyDate() {
        return this.lastModifyDate;
    }

    public CommunityLeader lastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
        return this;
    }

    public void setLastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyBy() {
        return this.lastModifyBy;
    }

    public CommunityLeader lastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
        return this;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public Community getCommunity() {
        return this.community;
    }

    public CommunityLeader community(Community community) {
        this.setCommunity(community);
        return this;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommunityLeader)) {
            return false;
        }
        return id != null && id.equals(((CommunityLeader) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityLeader{" +
            "id=" + getId() +
            ", avatar='" + getAvatar() + "'" +
            ", avatarContentType='" + getAvatarContentType() + "'" +
            ", realName='" + getRealName() + "'" +
            ", tel='" + getTel() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", jobDesc='" + getJobDesc() + "'" +
            ", jobCareerDesc='" + getJobCareerDesc() + "'" +
            ", enable='" + getEnable() + "'" +
            ", delFlag='" + getDelFlag() + "'" +
            ", orderNum=" + getOrderNum() +
            ", lastModifyDate='" + getLastModifyDate() + "'" +
            ", lastModifyBy='" + getLastModifyBy() + "'" +
            "}";
    }
}
