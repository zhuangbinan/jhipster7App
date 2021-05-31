package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 社区居委会 社区通知、公告、新闻 暂时定为 标题 图片+正文 尾部 这样的 模板格式
 */
@ApiModel(description = "社区居委会 社区通知、公告、新闻 暂时定为 标题 图片+正文 尾部 这样的 模板格式")
@Entity
@Table(name = "wamoli_community_notice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommunityNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "img_1")
    private byte[] img1;

    @Column(name = "img_1_content_type")
    private String img1ContentType;

    @Column(name = "img_1_title")
    private String img1Title;

    @Lob
    @Column(name = "content_1")
    private String content1;

    @Lob
    @Column(name = "img_2")
    private byte[] img2;

    @Column(name = "img_2_content_type")
    private String img2ContentType;

    @Column(name = "img_2_title")
    private String img2Title;

    @Lob
    @Column(name = "content_2")
    private String content2;

    @Lob
    @Column(name = "img_3")
    private byte[] img3;

    @Column(name = "img_3_content_type")
    private String img3ContentType;

    @Column(name = "img_3_title")
    private String img3Title;

    @Lob
    @Column(name = "content_3")
    private String content3;

    @Lob
    @Column(name = "img_4")
    private byte[] img4;

    @Column(name = "img_4_content_type")
    private String img4ContentType;

    @Column(name = "img_4_title")
    private String img4Title;

    @Lob
    @Column(name = "content_4")
    private String content4;

    @Lob
    @Column(name = "img_5")
    private byte[] img5;

    @Column(name = "img_5_content_type")
    private String img5ContentType;

    @Column(name = "img_5_title")
    private String img5Title;

    @Lob
    @Column(name = "content_5")
    private String content5;

    @Column(name = "tail")
    private String tail;

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

    public CommunityNotice id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public CommunityNotice title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImg1() {
        return this.img1;
    }

    public CommunityNotice img1(byte[] img1) {
        this.img1 = img1;
        return this;
    }

    public void setImg1(byte[] img1) {
        this.img1 = img1;
    }

    public String getImg1ContentType() {
        return this.img1ContentType;
    }

    public CommunityNotice img1ContentType(String img1ContentType) {
        this.img1ContentType = img1ContentType;
        return this;
    }

    public void setImg1ContentType(String img1ContentType) {
        this.img1ContentType = img1ContentType;
    }

    public String getImg1Title() {
        return this.img1Title;
    }

    public CommunityNotice img1Title(String img1Title) {
        this.img1Title = img1Title;
        return this;
    }

    public void setImg1Title(String img1Title) {
        this.img1Title = img1Title;
    }

    public String getContent1() {
        return this.content1;
    }

    public CommunityNotice content1(String content1) {
        this.content1 = content1;
        return this;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public byte[] getImg2() {
        return this.img2;
    }

    public CommunityNotice img2(byte[] img2) {
        this.img2 = img2;
        return this;
    }

    public void setImg2(byte[] img2) {
        this.img2 = img2;
    }

    public String getImg2ContentType() {
        return this.img2ContentType;
    }

    public CommunityNotice img2ContentType(String img2ContentType) {
        this.img2ContentType = img2ContentType;
        return this;
    }

    public void setImg2ContentType(String img2ContentType) {
        this.img2ContentType = img2ContentType;
    }

    public String getImg2Title() {
        return this.img2Title;
    }

    public CommunityNotice img2Title(String img2Title) {
        this.img2Title = img2Title;
        return this;
    }

    public void setImg2Title(String img2Title) {
        this.img2Title = img2Title;
    }

    public String getContent2() {
        return this.content2;
    }

    public CommunityNotice content2(String content2) {
        this.content2 = content2;
        return this;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public byte[] getImg3() {
        return this.img3;
    }

    public CommunityNotice img3(byte[] img3) {
        this.img3 = img3;
        return this;
    }

    public void setImg3(byte[] img3) {
        this.img3 = img3;
    }

    public String getImg3ContentType() {
        return this.img3ContentType;
    }

    public CommunityNotice img3ContentType(String img3ContentType) {
        this.img3ContentType = img3ContentType;
        return this;
    }

    public void setImg3ContentType(String img3ContentType) {
        this.img3ContentType = img3ContentType;
    }

    public String getImg3Title() {
        return this.img3Title;
    }

    public CommunityNotice img3Title(String img3Title) {
        this.img3Title = img3Title;
        return this;
    }

    public void setImg3Title(String img3Title) {
        this.img3Title = img3Title;
    }

    public String getContent3() {
        return this.content3;
    }

    public CommunityNotice content3(String content3) {
        this.content3 = content3;
        return this;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    public byte[] getImg4() {
        return this.img4;
    }

    public CommunityNotice img4(byte[] img4) {
        this.img4 = img4;
        return this;
    }

    public void setImg4(byte[] img4) {
        this.img4 = img4;
    }

    public String getImg4ContentType() {
        return this.img4ContentType;
    }

    public CommunityNotice img4ContentType(String img4ContentType) {
        this.img4ContentType = img4ContentType;
        return this;
    }

    public void setImg4ContentType(String img4ContentType) {
        this.img4ContentType = img4ContentType;
    }

    public String getImg4Title() {
        return this.img4Title;
    }

    public CommunityNotice img4Title(String img4Title) {
        this.img4Title = img4Title;
        return this;
    }

    public void setImg4Title(String img4Title) {
        this.img4Title = img4Title;
    }

    public String getContent4() {
        return this.content4;
    }

    public CommunityNotice content4(String content4) {
        this.content4 = content4;
        return this;
    }

    public void setContent4(String content4) {
        this.content4 = content4;
    }

    public byte[] getImg5() {
        return this.img5;
    }

    public CommunityNotice img5(byte[] img5) {
        this.img5 = img5;
        return this;
    }

    public void setImg5(byte[] img5) {
        this.img5 = img5;
    }

    public String getImg5ContentType() {
        return this.img5ContentType;
    }

    public CommunityNotice img5ContentType(String img5ContentType) {
        this.img5ContentType = img5ContentType;
        return this;
    }

    public void setImg5ContentType(String img5ContentType) {
        this.img5ContentType = img5ContentType;
    }

    public String getImg5Title() {
        return this.img5Title;
    }

    public CommunityNotice img5Title(String img5Title) {
        this.img5Title = img5Title;
        return this;
    }

    public void setImg5Title(String img5Title) {
        this.img5Title = img5Title;
    }

    public String getContent5() {
        return this.content5;
    }

    public CommunityNotice content5(String content5) {
        this.content5 = content5;
        return this;
    }

    public void setContent5(String content5) {
        this.content5 = content5;
    }

    public String getTail() {
        return this.tail;
    }

    public CommunityNotice tail(String tail) {
        this.tail = tail;
        return this;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public CommunityNotice enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getDelFlag() {
        return this.delFlag;
    }

    public CommunityNotice delFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public CommunityNotice orderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Instant getLastModifyDate() {
        return this.lastModifyDate;
    }

    public CommunityNotice lastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
        return this;
    }

    public void setLastModifyDate(Instant lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyBy() {
        return this.lastModifyBy;
    }

    public CommunityNotice lastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
        return this;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public Community getCommunity() {
        return this.community;
    }

    public CommunityNotice community(Community community) {
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
        if (!(o instanceof CommunityNotice)) {
            return false;
        }
        return id != null && id.equals(((CommunityNotice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityNotice{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", img1='" + getImg1() + "'" +
            ", img1ContentType='" + getImg1ContentType() + "'" +
            ", img1Title='" + getImg1Title() + "'" +
            ", content1='" + getContent1() + "'" +
            ", img2='" + getImg2() + "'" +
            ", img2ContentType='" + getImg2ContentType() + "'" +
            ", img2Title='" + getImg2Title() + "'" +
            ", content2='" + getContent2() + "'" +
            ", img3='" + getImg3() + "'" +
            ", img3ContentType='" + getImg3ContentType() + "'" +
            ", img3Title='" + getImg3Title() + "'" +
            ", content3='" + getContent3() + "'" +
            ", img4='" + getImg4() + "'" +
            ", img4ContentType='" + getImg4ContentType() + "'" +
            ", img4Title='" + getImg4Title() + "'" +
            ", content4='" + getContent4() + "'" +
            ", img5='" + getImg5() + "'" +
            ", img5ContentType='" + getImg5ContentType() + "'" +
            ", img5Title='" + getImg5Title() + "'" +
            ", content5='" + getContent5() + "'" +
            ", tail='" + getTail() + "'" +
            ", enable='" + getEnable() + "'" +
            ", delFlag='" + getDelFlag() + "'" +
            ", orderNum=" + getOrderNum() +
            ", lastModifyDate='" + getLastModifyDate() + "'" +
            ", lastModifyBy='" + getLastModifyBy() + "'" +
            "}";
    }
}
