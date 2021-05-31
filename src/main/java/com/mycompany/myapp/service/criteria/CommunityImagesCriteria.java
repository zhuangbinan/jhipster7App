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
 * Criteria class for the {@link com.mycompany.myapp.domain.CommunityImages} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CommunityImagesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /community-images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommunityImagesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imgTitle;

    private StringFilter imgDesc;

    private IntegerFilter orderNum;

    private InstantFilter lastModifyDate;

    private StringFilter lastModifyBy;

    private LongFilter communityImageGroupId;

    public CommunityImagesCriteria() {}

    public CommunityImagesCriteria(CommunityImagesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imgTitle = other.imgTitle == null ? null : other.imgTitle.copy();
        this.imgDesc = other.imgDesc == null ? null : other.imgDesc.copy();
        this.orderNum = other.orderNum == null ? null : other.orderNum.copy();
        this.lastModifyDate = other.lastModifyDate == null ? null : other.lastModifyDate.copy();
        this.lastModifyBy = other.lastModifyBy == null ? null : other.lastModifyBy.copy();
        this.communityImageGroupId = other.communityImageGroupId == null ? null : other.communityImageGroupId.copy();
    }

    @Override
    public CommunityImagesCriteria copy() {
        return new CommunityImagesCriteria(this);
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

    public StringFilter getImgTitle() {
        return imgTitle;
    }

    public StringFilter imgTitle() {
        if (imgTitle == null) {
            imgTitle = new StringFilter();
        }
        return imgTitle;
    }

    public void setImgTitle(StringFilter imgTitle) {
        this.imgTitle = imgTitle;
    }

    public StringFilter getImgDesc() {
        return imgDesc;
    }

    public StringFilter imgDesc() {
        if (imgDesc == null) {
            imgDesc = new StringFilter();
        }
        return imgDesc;
    }

    public void setImgDesc(StringFilter imgDesc) {
        this.imgDesc = imgDesc;
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

    public LongFilter getCommunityImageGroupId() {
        return communityImageGroupId;
    }

    public LongFilter communityImageGroupId() {
        if (communityImageGroupId == null) {
            communityImageGroupId = new LongFilter();
        }
        return communityImageGroupId;
    }

    public void setCommunityImageGroupId(LongFilter communityImageGroupId) {
        this.communityImageGroupId = communityImageGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommunityImagesCriteria that = (CommunityImagesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imgTitle, that.imgTitle) &&
            Objects.equals(imgDesc, that.imgDesc) &&
            Objects.equals(orderNum, that.orderNum) &&
            Objects.equals(lastModifyDate, that.lastModifyDate) &&
            Objects.equals(lastModifyBy, that.lastModifyBy) &&
            Objects.equals(communityImageGroupId, that.communityImageGroupId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imgTitle, imgDesc, orderNum, lastModifyDate, lastModifyBy, communityImageGroupId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityImagesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imgTitle != null ? "imgTitle=" + imgTitle + ", " : "") +
            (imgDesc != null ? "imgDesc=" + imgDesc + ", " : "") +
            (orderNum != null ? "orderNum=" + orderNum + ", " : "") +
            (lastModifyDate != null ? "lastModifyDate=" + lastModifyDate + ", " : "") +
            (lastModifyBy != null ? "lastModifyBy=" + lastModifyBy + ", " : "") +
            (communityImageGroupId != null ? "communityImageGroupId=" + communityImageGroupId + ", " : "") +
            "}";
    }
}
