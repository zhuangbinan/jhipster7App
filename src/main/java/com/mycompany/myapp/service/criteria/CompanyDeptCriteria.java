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
 * Criteria class for the {@link com.mycompany.myapp.domain.CompanyDept} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CompanyDeptResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /company-depts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyDeptCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter parentId;

    private StringFilter ancestors;

    private StringFilter deptName;

    private IntegerFilter orderNum;

    private StringFilter leaderName;

    private StringFilter tel;

    private StringFilter email;

    private BooleanFilter enable;

    private BooleanFilter delFlag;

    private StringFilter createBy;

    private InstantFilter createDate;

    private StringFilter lastModifyBy;

    private InstantFilter lastModifyDate;

    private LongFilter companyUserId;

    public CompanyDeptCriteria() {}

    public CompanyDeptCriteria(CompanyDeptCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.ancestors = other.ancestors == null ? null : other.ancestors.copy();
        this.deptName = other.deptName == null ? null : other.deptName.copy();
        this.orderNum = other.orderNum == null ? null : other.orderNum.copy();
        this.leaderName = other.leaderName == null ? null : other.leaderName.copy();
        this.tel = other.tel == null ? null : other.tel.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.delFlag = other.delFlag == null ? null : other.delFlag.copy();
        this.createBy = other.createBy == null ? null : other.createBy.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModifyBy = other.lastModifyBy == null ? null : other.lastModifyBy.copy();
        this.lastModifyDate = other.lastModifyDate == null ? null : other.lastModifyDate.copy();
        this.companyUserId = other.companyUserId == null ? null : other.companyUserId.copy();
    }

    @Override
    public CompanyDeptCriteria copy() {
        return new CompanyDeptCriteria(this);
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

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public StringFilter getAncestors() {
        return ancestors;
    }

    public StringFilter ancestors() {
        if (ancestors == null) {
            ancestors = new StringFilter();
        }
        return ancestors;
    }

    public void setAncestors(StringFilter ancestors) {
        this.ancestors = ancestors;
    }

    public StringFilter getDeptName() {
        return deptName;
    }

    public StringFilter deptName() {
        if (deptName == null) {
            deptName = new StringFilter();
        }
        return deptName;
    }

    public void setDeptName(StringFilter deptName) {
        this.deptName = deptName;
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

    public StringFilter getLeaderName() {
        return leaderName;
    }

    public StringFilter leaderName() {
        if (leaderName == null) {
            leaderName = new StringFilter();
        }
        return leaderName;
    }

    public void setLeaderName(StringFilter leaderName) {
        this.leaderName = leaderName;
    }

    public StringFilter getTel() {
        return tel;
    }

    public StringFilter tel() {
        if (tel == null) {
            tel = new StringFilter();
        }
        return tel;
    }

    public void setTel(StringFilter tel) {
        this.tel = tel;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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
        final CompanyDeptCriteria that = (CompanyDeptCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(ancestors, that.ancestors) &&
            Objects.equals(deptName, that.deptName) &&
            Objects.equals(orderNum, that.orderNum) &&
            Objects.equals(leaderName, that.leaderName) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(email, that.email) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(delFlag, that.delFlag) &&
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
            parentId,
            ancestors,
            deptName,
            orderNum,
            leaderName,
            tel,
            email,
            enable,
            delFlag,
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
        return "CompanyDeptCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (ancestors != null ? "ancestors=" + ancestors + ", " : "") +
            (deptName != null ? "deptName=" + deptName + ", " : "") +
            (orderNum != null ? "orderNum=" + orderNum + ", " : "") +
            (leaderName != null ? "leaderName=" + leaderName + ", " : "") +
            (tel != null ? "tel=" + tel + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (delFlag != null ? "delFlag=" + delFlag + ", " : "") +
            (createBy != null ? "createBy=" + createBy + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModifyBy != null ? "lastModifyBy=" + lastModifyBy + ", " : "") +
            (lastModifyDate != null ? "lastModifyDate=" + lastModifyDate + ", " : "") +
            (companyUserId != null ? "companyUserId=" + companyUserId + ", " : "") +
            "}";
    }
}
