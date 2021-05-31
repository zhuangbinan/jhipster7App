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
 * 社区居委会 图片库
 */
@ApiModel(description = "社区居委会 图片库")
@Entity
@Table(name = "wamoli_community_images")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommunityImages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "img_content")
    private byte[] imgContent;

    @Column(name = "img_content_content_type")
    private String imgContentContentType;

    /**
     * 图片名称
     */
    @ApiModelProperty(value = "图片名称")
    @Column(name = "img_title")
    private String imgTitle;

    /**
     * 图片描述
     */
    @ApiModelProperty(value = "图片描述")
    @Column(name = "img_desc")
    private String imgDesc;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "last_modify_date")
    private Instant lastModifyDate;

    @Column(name = "last_modify_by")
    private String lastModifyBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "communityImages" }, allowSetters = true)
    private CommunityImageGroup communityImageGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommunityImages id(Long id) {
        this.id = id;
        return this;
    }

    public byte[] getImgContent() {
        return this.imgContent;
    }

    public CommunityImages imgContent(byte[] imgContent) {
        this.imgContent = imgContent;
        return this;
    }

    public void setImgContent(byte[] imgContent) {
        this.imgContent = imgContent;
    }

    public String getImgContentContentType() {
        return this.imgContentContentType;
    }

    public CommunityImages imgContentContentType(String imgContentContentType) {
        this.imgContentContentType = imgContentContentType;
        return this;
    }

    public void setImgContentContentType(String imgContentContentType) {
        this.imgContentContentType = imgContentContentType;
    }

    public String getImgTitle() {
        return this.imgTitle;
    }

    public CommunityImages imgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
        return this;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public String getImgDesc() {
        return this.imgDesc;
    }

    public CommunityImages imgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
        return this;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public CommunityImages orderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Instant getLastModifyDate() {
        return this.lastModifyDate;
    }

    public CommunityImages lastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
        return this;
    }

    public void setLastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyBy() {
        return this.lastModifyBy;
    }

    public CommunityImages lastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
        return this;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public CommunityImageGroup getCommunityImageGroup() {
        return this.communityImageGroup;
    }

    public CommunityImages communityImageGroup(CommunityImageGroup communityImageGroup) {
        this.setCommunityImageGroup(communityImageGroup);
        return this;
    }

    public void setCommunityImageGroup(CommunityImageGroup communityImageGroup) {
        this.communityImageGroup = communityImageGroup;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommunityImages)) {
            return false;
        }
        return id != null && id.equals(((CommunityImages) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityImages{" +
            "id=" + getId() +
            ", imgContent='" + getImgContent() + "'" +
            ", imgContentContentType='" + getImgContentContentType() + "'" +
            ", imgTitle='" + getImgTitle() + "'" +
            ", imgDesc='" + getImgDesc() + "'" +
            ", orderNum=" + getOrderNum() +
            ", lastModifyDate='" + getLastModifyDate() + "'" +
            ", lastModifyBy='" + getLastModifyBy() + "'" +
            "}";
    }
}
