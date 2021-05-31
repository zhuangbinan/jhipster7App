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
 * Criteria class for the {@link com.mycompany.myapp.domain.CommunityImageGroup} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CommunityImageGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /community-image-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommunityImageGroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imgGroupName;

    private IntegerFilter orderNum;

    private InstantFilter lastModifyDate;

    private StringFilter lastModifyBy;

    private LongFilter communityImagesId;

    public CommunityImageGroupCriteria() {}

    public CommunityImageGroupCriteria(CommunityImageGroupCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imgGroupName = other.imgGroupName == null ? null : other.imgGroupName.copy();
        this.orderNum = other.orderNum == null ? null : other.orderNum.copy();
        this.lastModifyDate = other.lastModifyDate == null ? null : other.lastModifyDate.copy();
        this.lastModifyBy = other.lastModifyBy == null ? null : other.lastModifyBy.copy();
        this.communityImagesId = other.communityImagesId == null ? null : other.communityImagesId.copy();
    }

    @Override
    public CommunityImageGroupCriteria copy() {
        return new CommunityImageGroupCriteria(this);
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

    public StringFilter getImgGroupName() {
        return imgGroupName;
    }

    public StringFilter imgGroupName() {
        if (imgGroupName == null) {
            imgGroupName = new StringFilter();
        }
        return imgGroupName;
    }

    public void setImgGroupName(StringFilter imgGroupName) {
        this.imgGroupName = imgGroupName;
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

    public LongFilter getCommunityImagesId() {
        return communityImagesId;
    }

    public LongFilter communityImagesId() {
        if (communityImagesId == null) {
            communityImagesId = new LongFilter();
        }
        return communityImagesId;
    }

    public void setCommunityImagesId(LongFilter communityImagesId) {
        this.communityImagesId = communityImagesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommunityImageGroupCriteria that = (CommunityImageGroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imgGroupName, that.imgGroupName) &&
            Objects.equals(orderNum, that.orderNum) &&
            Objects.equals(lastModifyDate, that.lastModifyDate) &&
            Objects.equals(lastModifyBy, that.lastModifyBy) &&
            Objects.equals(communityImagesId, that.communityImagesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imgGroupName, orderNum, lastModifyDate, lastModifyBy, communityImagesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityImageGroupCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imgGroupName != null ? "imgGroupName=" + imgGroupName + ", " : "") +
            (orderNum != null ? "orderNum=" + orderNum + ", " : "") +
            (lastModifyDate != null ? "lastModifyDate=" + lastModifyDate + ", " : "") +
            (lastModifyBy != null ? "lastModifyBy=" + lastModifyBy + ", " : "") +
            (communityImagesId != null ? "communityImagesId=" + communityImagesId + ", " : "") +
            "}";
    }
}
