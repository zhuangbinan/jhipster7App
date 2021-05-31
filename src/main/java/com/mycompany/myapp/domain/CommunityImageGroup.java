package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 社区居委会 图片库分组
 */
@ApiModel(description = "社区居委会 图片库分组")
@Entity
@Table(name = "wamoli_community_image_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommunityImageGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 图片组名
     */

    @ApiModelProperty(value = "图片组名")
    @Column(name = "img_group_name", unique = true)
    private String imgGroupName;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "last_modify_date")
    private Instant lastModifyDate;

    @Column(name = "last_modify_by")
    private String lastModifyBy;

    @OneToMany(mappedBy = "communityImageGroup")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "communityImageGroup" }, allowSetters = true)
    private Set<CommunityImages> communityImages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommunityImageGroup id(Long id) {
        this.id = id;
        return this;
    }

    public String getImgGroupName() {
        return this.imgGroupName;
    }

    public CommunityImageGroup imgGroupName(String imgGroupName) {
        this.imgGroupName = imgGroupName;
        return this;
    }

    public void setImgGroupName(String imgGroupName) {
        this.imgGroupName = imgGroupName;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public CommunityImageGroup orderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Instant getLastModifyDate() {
        return this.lastModifyDate;
    }

    public CommunityImageGroup lastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
        return this;
    }

    public void setLastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyBy() {
        return this.lastModifyBy;
    }

    public CommunityImageGroup lastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
        return this;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public Set<CommunityImages> getCommunityImages() {
        return this.communityImages;
    }

    public CommunityImageGroup communityImages(Set<CommunityImages> communityImages) {
        this.setCommunityImages(communityImages);
        return this;
    }

    public CommunityImageGroup addCommunityImages(CommunityImages communityImages) {
        this.communityImages.add(communityImages);
        communityImages.setCommunityImageGroup(this);
        return this;
    }

    public CommunityImageGroup removeCommunityImages(CommunityImages communityImages) {
        this.communityImages.remove(communityImages);
        communityImages.setCommunityImageGroup(null);
        return this;
    }

    public void setCommunityImages(Set<CommunityImages> communityImages) {
        if (this.communityImages != null) {
            this.communityImages.forEach(i -> i.setCommunityImageGroup(null));
        }
        if (communityImages != null) {
            communityImages.forEach(i -> i.setCommunityImageGroup(this));
        }
        this.communityImages = communityImages;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommunityImageGroup)) {
            return false;
        }
        return id != null && id.equals(((CommunityImageGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityImageGroup{" +
            "id=" + getId() +
            ", imgGroupName='" + getImgGroupName() + "'" +
            ", orderNum=" + getOrderNum() +
            ", lastModifyDate='" + getLastModifyDate() + "'" +
            ", lastModifyBy='" + getLastModifyBy() + "'" +
            "}";
    }
}
