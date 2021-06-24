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
 * Criteria class for the {@link com.mycompany.myapp.domain.CompanyPost} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CompanyPostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /company-posts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyPostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter postCode;

    private StringFilter postName;

    private IntegerFilter orderNum;

    private StringFilter remark;

    private BooleanFilter enable;

    private StringFilter createBy;

    private InstantFilter createDate;

    private StringFilter lastModifyBy;

    private InstantFilter lastModifyDate;

    private LongFilter companyUserId;

    public CompanyPostCriteria() {}

    public CompanyPostCriteria(CompanyPostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.postCode = other.postCode == null ? null : other.postCode.copy();
        this.postName = other.postName == null ? null : other.postName.copy();
        this.orderNum = other.orderNum == null ? null : other.orderNum.copy();
        this.remark = other.remark == null ? null : other.remark.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.createBy = other.createBy == null ? null : other.createBy.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModifyBy = other.lastModifyBy == null ? null : other.lastModifyBy.copy();
        this.lastModifyDate = other.lastModifyDate == null ? null : other.lastModifyDate.copy();
        this.companyUserId = other.companyUserId == null ? null : other.companyUserId.copy();
    }

    @Override
    public CompanyPostCriteria copy() {
        return new CompanyPostCriteria(this);
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

    public StringFilter getPostCode() {
        return postCode;
    }

    public StringFilter postCode() {
        if (postCode == null) {
            postCode = new StringFilter();
        }
        return postCode;
    }

    public void setPostCode(StringFilter postCode) {
        this.postCode = postCode;
    }

    public StringFilter getPostName() {
        return postName;
    }

    public StringFilter postName() {
        if (postName == null) {
            postName = new StringFilter();
        }
        return postName;
    }

    public void setPostName(StringFilter postName) {
        this.postName = postName;
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

    public StringFilter getRemark() {
        return remark;
    }

    public StringFilter remark() {
        if (remark == null) {
            remark = new StringFilter();
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
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

    public StringFilter getCreateBy() {
        return createBy;
    }

    public StringFilter createBy() {
        if (createBy == null) {
            createBy = new StringFilter();
        }
        return createBy;
    }

    public void setCreateBy(StringFilter createBy) {
        this.createBy = createBy;
    }

    public InstantFilter getCreateDate() {
        return createDate;
    }

    public InstantFilter createDate() {
        if (createDate == null) {
            createDate = new InstantFilter();
        }
        return createDate;
    }

    public void setCreateDate(InstantFilter createDate) {
        this.createDate = createDate;
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

    public LongFilter getCompanyUserId() {
        return companyUserId;
    }

    public LongFilter companyUserId() {
        if (companyUserId == null) {
            companyUserId = new LongFilter();
        }
        return companyUserId;
    }

    public void setCompanyUserId(LongFilter companyUserId) {
        this.companyUserId = companyUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompanyPostCriteria that = (CompanyPostCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(postCode, that.postCode) &&
            Objects.equals(postName, that.postName) &&
            Objects.equals(orderNum, that.orderNum) &&
            Objects.equals(remark, that.remark) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(createBy, that.createBy) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModifyBy, that.lastModifyBy) &&
            Objects.equals(lastModifyDate, that.lastModifyDate) &&
            Objects.equals(companyUserId, that.companyUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            postCode,
            postName,
            orderNum,
            remark,
            enable,
            createBy,
            createDate,
            lastModifyBy,
            lastModifyDate,
            companyUserId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyPostCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (postCode != null ? "postCode=" + postCode + ", " : "") +
            (postName != null ? "postName=" + postName + ", " : "") +
            (orderNum != null ? "orderNum=" + orderNum + ", " : "") +
            (remark != null ? "remark=" + remark + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (createBy != null ? "createBy=" + createBy + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModifyBy != null ? "lastModifyBy=" + lastModifyBy + ", " : "") +
            (lastModifyDate != null ? "lastModifyDate=" + lastModifyDate + ", " : "") +
            (companyUserId != null ? "companyUserId=" + companyUserId + ", " : "") +
            "}";
    }
}
