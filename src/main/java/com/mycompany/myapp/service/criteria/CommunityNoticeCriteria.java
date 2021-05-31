package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.CommunityNotice} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CommunityNoticeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /community-notices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommunityNoticeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter img1Title;

    private StringFilter img2Title;

    private StringFilter img3Title;

    private StringFilter img4Title;

    private StringFilter img5Title;

    private StringFilter tail;

    private BooleanFilter enable;

    private BooleanFilter delFlag;

    private IntegerFilter orderNum;

    private InstantFilter lastModifyDate;

    private StringFilter lastModifyBy;

    private LongFilter communityId;

    public CommunityNoticeCriteria() {}

    public CommunityNoticeCriteria(CommunityNoticeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.img1Title = other.img1Title == null ? null : other.img1Title.copy();
        this.img2Title = other.img2Title == null ? null : other.img2Title.copy();
        this.img3Title = other.img3Title == null ? null : other.img3Title.copy();
        this.img4Title = other.img4Title == null ? null : other.img4Title.copy();
        this.img5Title = other.img5Title == null ? null : other.img5Title.copy();
        this.tail = other.tail == null ? null : other.tail.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.delFlag = other.delFlag == null ? null : other.delFlag.copy();
        this.orderNum = other.orderNum == null ? null : other.orderNum.copy();
        this.lastModifyDate = other.lastModifyDate == null ? null : other.lastModifyDate.copy();
        this.lastModifyBy = other.lastModifyBy == null ? null : other.lastModifyBy.copy();
        this.communityId = other.communityId == null ? null : other.communityId.copy();
    }

    @Override
    public CommunityNoticeCriteria copy() {
        return new CommunityNoticeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getImg1Title() {
        return img1Title;
    }

    public StringFilter img1Title() {
        if (img1Title == null) {
            img1Title = new StringFilter();
        }
        return img1Title;
    }

    public void setImg1Title(StringFilter img1Title) {
        this.img1Title = img1Title;
    }

    public StringFilter getImg2Title() {
        return img2Title;
    }

    public StringFilter img2Title() {
        if (img2Title == null) {
            img2Title = new StringFilter();
        }
        return img2Title;
    }

    public void setImg2Title(StringFilter img2Title) {
        this.img2Title = img2Title;
    }

    public StringFilter getImg3Title() {
        return img3Title;
    }

    public StringFilter img3Title() {
        if (img3Title == null) {
            img3Title = new StringFilter();
        }
        return img3Title;
    }

    public void setImg3Title(StringFilter img3Title) {
        this.img3Title = img3Title;
    }

    public StringFilter getImg4Title() {
        return img4Title;
    }

    public StringFilter img4Title() {
        if (img4Title == null) {
            img4Title = new StringFilter();
        }
        return img4Title;
    }

    public void setImg4Title(StringFilter img4Title) {
        this.img4Title = img4Title;
    }

    public StringFilter getImg5Title() {
        return img5Title;
    }

    public StringFilter img5Title() {
        if (img5Title == null) {
            img5Title = new StringFilter();
        }
        return img5Title;
    }

    public void setImg5Title(StringFilter img5Title) {
        this.img5Title = img5Title;
    }

    public StringFilter getTail() {
        return tail;
    }

    public StringFilter tail() {
        if (tail == null) {
            tail = new StringFilter();
        }
        return tail;
    }

    public void setTail(StringFilter tail) {
        this.tail = tail;
    }

    public BooleanFilter getEnable() {
        return enable;
    }

    public BooleanFilter enable() {
        if (enable == null) {
            enable = new BooleanFilter();
        }
        return enable;
    }

    public void setEnable(BooleanFilter enable) {
        this.enable = enable;
    }

    public BooleanFilter getDelFlag() {
        return delFlag;
    }

    public BooleanFilter delFlag() {
        if (delFlag == null) {
            delFlag = new BooleanFilter();
        }
        return delFlag;
    }

    public void setDelFlag(BooleanFilter delFlag) {
        this.delFlag = delFlag;
    }

    public IntegerFilter getOrderNum() {
        return orderNum;
    }

    public IntegerFilter orderNum() {
        if (orderNum == null) {
            orderNum = new IntegerFilter();
        }
        return orderNum;
    }

    public void setOrderNum(IntegerFilter orderNum) {
        this.orderNum = orderNum;
    }

    public InstantFilter getLastModifyDate() {
        return lastModifyDate;
    }

    public InstantFilter lastModifyDate() {
        if (lastModifyDate == null) {
            lastModifyDate = new InstantFilter();
        }
        return lastModifyDate;
    }

    public void setLastModifyDate(InstantFilter lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public StringFilter getLastModifyBy() {
        return lastModifyBy;
    }

    public StringFilter lastModifyBy() {
        if (lastModifyBy == null) {
            lastModifyBy = new StringFilter();
        }
        return lastModifyBy;
    }

    public void setLastModifyBy(StringFilter lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    public LongFilter getCommunityId() {
        return communityId;
    }

    public LongFilter communityId() {
        if (communityId == null) {
            communityId = new LongFilter();
        }
        return communityId;
    }

    public void setCommunityId(LongFilter communityId) {
        this.communityId = communityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommunityNoticeCriteria that = (CommunityNoticeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(img1Title, that.img1Title) &&
            Objects.equals(img2Title, that.img2Title) &&
            Objects.equals(img3Title, that.img3Title) &&
            Objects.equals(img4Title, that.img4Title) &&
            Objects.equals(img5Title, that.img5Title) &&
            Objects.equals(tail, that.tail) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(delFlag, that.delFlag) &&
            Objects.equals(orderNum, that.orderNum) &&
            Objects.equals(lastModifyDate, that.lastModifyDate) &&
            Objects.equals(lastModifyBy, that.lastModifyBy) &&
            Objects.equals(communityId, that.communityId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            img1Title,
            img2Title,
            img3Title,
            img4Title,
            img5Title,
            tail,
            enable,
            delFlag,
            orderNum,
            lastModifyDate,
            lastModifyBy,
            communityId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityNoticeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (img1Title != null ? "img1Title=" + img1Title + ", " : "") +
            (img2Title != null ? "img2Title=" + img2Title + ", " : "") +
            (img3Title != null ? "img3Title=" + img3Title + ", " : "") +
            (img4Title != null ? "img4Title=" + img4Title + ", " : "") +
            (img5Title != null ? "img5Title=" + img5Title + ", " : "") +
            (tail != null ? "tail=" + tail + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (delFlag != null ? "delFlag=" + delFlag + ", " : "") +
            (orderNum != null ? "orderNum=" + orderNum + ", " : "") +
            (lastModifyDate != null ? "lastModifyDate=" + lastModifyDate + ", " : "") +
            (lastModifyBy != null ? "lastModifyBy=" + lastModifyBy + ", " : "") +
            (communityId != null ? "communityId=" + communityId + ", " : "") +
            "}";
    }
}
