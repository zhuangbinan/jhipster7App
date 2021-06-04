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
 * 社区居委会
 */
@ApiModel(description = "社区居委会")
@Entity
@Table(name = "wamoli_community")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Community implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @Column(name = "cname")
    private String cname;

    /**
     * 负责人姓名
     */
    @ApiModelProperty(value = "负责人姓名")
    @Column(name = "manager_name")
    private String managerName;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    @Column(name = "address")
    private String address;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    @Column(name = "tel")
    private String tel;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Column(name = "email")
    private String email;

    /**
     * 办公时间
     */
    @ApiModelProperty(value = "办公时间")
    @Column(name = "office_hours")
    private String officeHours;

    /**
     * 社区描述
     */
    @ApiModelProperty(value = "社区描述")
    @Column(name = "description")
    private String description;

    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    @Column(name = "source")
    private String source;

    /**
     * 父级ID 为了分管办公室级别
     */
    @ApiModelProperty(value = "父级ID 为了分管办公室级别")
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 祖籍 示例 0,100
     */
    @ApiModelProperty(value = "祖籍 示例 0,100")
    @Column(name = "ancestors")
    private String ancestors;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    @Column(name = "long_code")
    private String longCode;

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

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "last_modify_date")
    private Instant lastModifyDate;

    @Column(name = "last_modify_by")
    private String lastModifyBy;

    @OneToMany(mappedBy = "community")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "community" }, allowSetters = true)
    private Set<CommunityLeader> communityLeaders = new HashSet<>();

    @OneToMany(mappedBy = "community")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "community" }, allowSetters = true)
    private Set<CommunityNotice> communityNotices = new HashSet<>();

    @OneToMany(mappedBy = "community")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "buildings", "community", "company" }, allowSetters = true)
    private Set<HomelandStation> homelandStations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Community id(Long id) {
        this.id = id;
        return this;
    }

    public String getCname() {
        return this.cname;
    }

    public Community cname(String cname) {
        this.cname = cname;
        return this;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getManagerName() {
        return this.managerName;
    }

    public Community managerName(String managerName) {
        this.managerName = managerName;
        return this;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getAddress() {
        return this.address;
    }

    public Community address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return this.tel;
    }

    public Community tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return this.email;
    }

    public Community email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeHours() {
        return this.officeHours;
    }

    public Community officeHours(String officeHours) {
        this.officeHours = officeHours;
        return this;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public String getDescription() {
        return this.description;
    }

    public Community description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return this.source;
    }

    public Community source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public Community parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return this.ancestors;
    }

    public Community ancestors(String ancestors) {
        this.ancestors = ancestors;
        return this;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getLongCode() {
        return this.longCode;
    }

    public Community longCode(String longCode) {
        this.longCode = longCode;
        return this;
    }

    public void setLongCode(String longCode) {
        this.longCode = longCode;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public Community enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getDelFlag() {
        return this.delFlag;
    }

    public Community delFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public Community orderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Instant getLastModifyDate() {
        return this.lastModifyDate;
    }

    public Community lastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
        return this;
    }

    public void setLastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyBy() {
        return this.lastModifyBy;
    }

    public Community lastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
        return this;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public Set<CommunityLeader> getCommunityLeaders() {
        return this.communityLeaders;
    }

    public Community communityLeaders(Set<CommunityLeader> communityLeaders) {
        this.setCommunityLeaders(communityLeaders);
        return this;
    }

    public Community addCommunityLeader(CommunityLeader communityLeader) {
        this.communityLeaders.add(communityLeader);
        communityLeader.setCommunity(this);
        return this;
    }

    public Community removeCommunityLeader(CommunityLeader communityLeader) {
        this.communityLeaders.remove(communityLeader);
        communityLeader.setCommunity(null);
        return this;
    }

    public void setCommunityLeaders(Set<CommunityLeader> communityLeaders) {
        if (this.communityLeaders != null) {
            this.communityLeaders.forEach(i -> i.setCommunity(null));
        }
        if (communityLeaders != null) {
            communityLeaders.forEach(i -> i.setCommunity(this));
        }
        this.communityLeaders = communityLeaders;
    }

    public Set<CommunityNotice> getCommunityNotices() {
        return this.communityNotices;
    }

    public Community communityNotices(Set<CommunityNotice> communityNotices) {
        this.setCommunityNotices(communityNotices);
        return this;
    }

    public Community addCommunityNotice(CommunityNotice communityNotice) {
        this.communityNotices.add(communityNotice);
        communityNotice.setCommunity(this);
        return this;
    }

    public Community removeCommunityNotice(CommunityNotice communityNotice) {
        this.communityNotices.remove(communityNotice);
        communityNotice.setCommunity(null);
        return this;
    }

    public void setCommunityNotices(Set<CommunityNotice> communityNotices) {
        if (this.communityNotices != null) {
            this.communityNotices.forEach(i -> i.setCommunity(null));
        }
        if (communityNotices != null) {
            communityNotices.forEach(i -> i.setCommunity(this));
        }
        this.communityNotices = communityNotices;
    }

    public Set<HomelandStation> getHomelandStations() {
        return this.homelandStations;
    }

    public Community homelandStations(Set<HomelandStation> homelandStations) {
        this.setHomelandStations(homelandStations);
        return this;
    }

    public Community addHomelandStation(HomelandStation homelandStation) {
        this.homelandStations.add(homelandStation);
        homelandStation.setCommunity(this);
        return this;
    }

    public Community removeHomelandStation(HomelandStation homelandStation) {
        this.homelandStations.remove(homelandStation);
        homelandStation.setCommunity(null);
        return this;
    }

    public void setHomelandStations(Set<HomelandStation> homelandStations) {
        if (this.homelandStations != null) {
            this.homelandStations.forEach(i -> i.setCommunity(null));
        }
        if (homelandStations != null) {
            homelandStations.forEach(i -> i.setCommunity(this));
        }
        this.homelandStations = homelandStations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Community)) {
            return false;
        }
        return id != null && id.equals(((Community) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Community{" +
            "id=" + getId() +
            ", cname='" + getCname() + "'" +
            ", managerName='" + getManagerName() + "'" +
            ", address='" + getAddress() + "'" +
            ", tel='" + getTel() + "'" +
            ", email='" + getEmail() + "'" +
            ", officeHours='" + getOfficeHours() + "'" +
            ", description='" + getDescription() + "'" +
            ", source='" + getSource() + "'" +
            ", parentId=" + getParentId() +
            ", ancestors='" + getAncestors() + "'" +
            ", longCode='" + getLongCode() + "'" +
            ", enable='" + getEnable() + "'" +
            ", delFlag='" + getDelFlag() + "'" +
            ", orderNum=" + getOrderNum() +
            ", lastModifyDate='" + getLastModifyDate() + "'" +
            ", lastModifyBy='" + getLastModifyBy() + "'" +
            "}";
    }
}
