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
 * Criteria class for the {@link com.mycompany.myapp.domain.CommunityLeader} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CommunityLeaderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /community-leaders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommunityLeaderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter realName;

    private StringFilter tel;

    private StringFilter jobTitle;

    private StringFilter jobDesc;

    private BooleanFilter enable;

    private BooleanFilter delFlag;

    private IntegerFilter orderNum;

    private InstantFilter lastModifyDate;

    private StringFilter lastModifyBy;

    private LongFilter communityId;

    public CommunityLeaderCriteria() {}

    public CommunityLeaderCriteria(CommunityLeaderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.realName = other.realName == null ? null : other.realName.copy();
        this.tel = other.tel == null ? null : other.tel.copy();
        this.jobTitle = other.jobTitle == null ? null : other.jobTitle.copy();
        this.jobDesc = other.jobDesc == null ? null : other.jobDesc.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.delFlag = other.delFlag == null ? null : other.delFlag.copy();
        this.orderNum = other.orderNum == null ? null : other.orderNum.copy();
        this.lastModifyDate = other.lastModifyDate == null ? null : other.lastModifyDate.copy();
        this.lastModifyBy = other.lastModifyBy == null ? null : other.lastModifyBy.copy();
        this.communityId = other.communityId == null ? null : other.communityId.copy();
    }

    @Override
    public CommunityLeaderCriteria copy() {
        return new CommunityLeaderCriteria(this);
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

    public StringFilter getRealName() {
        return realName;
    }

    public StringFilter realName() {
        if (realName == null) {
            realName = new StringFilter();
        }
        return realName;
    }

    public void setRealName(StringFilter realName) {
        this.realName = realName;
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

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            jobTitle = new StringFilter();
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public StringFilter getJobDesc() {
        return jobDesc;
    }

    public StringFilter jobDesc() {
        if (jobDesc == null) {
            jobDesc = new StringFilter();
        }
        return jobDesc;
    }

    public void setJobDesc(StringFilter jobDesc) {
        this.jobDesc = jobDesc;
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
        final CommunityLeaderCriteria that = (CommunityLeaderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(realName, that.realName) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(jobDesc, that.jobDesc) &&
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
        return Objects.hash(id, realName, tel, jobTitle, jobDesc, enable, delFlag, orderNum, lastModifyDate, lastModifyBy, communityId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityLeaderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (realName != null ? "realName=" + realName + ", " : "") +
            (tel != null ? "tel=" + tel + ", " : "") +
            (jobTitle != null ? "jobTitle=" + jobTitle + ", " : "") +
            (jobDesc != null ? "jobDesc=" + jobDesc + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (delFlag != null ? "delFlag=" + delFlag + ", " : "") +
            (orderNum != null ? "orderNum=" + orderNum + ", " : "") +
            (lastModifyDate != null ? "lastModifyDate=" + lastModifyDate + ", " : "") +
            (lastModifyBy != null ? "lastModifyBy=" + lastModifyBy + ", " : "") +
            (communityId != null ? "communityId=" + communityId + ", " : "") +
            "}";
    }
}
