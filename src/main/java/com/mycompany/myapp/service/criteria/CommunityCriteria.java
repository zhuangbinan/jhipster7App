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
 * Criteria class for the {@link com.mycompany.myapp.domain.Community} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CommunityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /communities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommunityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cname;

    private StringFilter managerName;

    private StringFilter address;

    private StringFilter tel;

    private StringFilter email;

    private StringFilter officeHours;

    private StringFilter description;

    private StringFilter source;

    private LongFilter parentId;

    private StringFilter ancestors;

    private StringFilter longCode;

    private BooleanFilter enable;

    private BooleanFilter delFlag;

    private IntegerFilter orderNum;

    private InstantFilter lastModifyDate;

    private StringFilter lastModifyBy;

    private LongFilter communityLeaderId;

    private LongFilter communityNoticeId;

    private LongFilter homelandStationId;

    public CommunityCriteria() {}

    public CommunityCriteria(CommunityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cname = other.cname == null ? null : other.cname.copy();
        this.managerName = other.managerName == null ? null : other.managerName.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.tel = other.tel == null ? null : other.tel.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.officeHours = other.officeHours == null ? null : other.officeHours.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.source = other.source == null ? null : other.source.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.ancestors = other.ancestors == null ? null : other.ancestors.copy();
        this.longCode = other.longCode == null ? null : other.longCode.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.delFlag = other.delFlag == null ? null : other.delFlag.copy();
        this.orderNum = other.orderNum == null ? null : other.orderNum.copy();
        this.lastModifyDate = other.lastModifyDate == null ? null : other.lastModifyDate.copy();
        this.lastModifyBy = other.lastModifyBy == null ? null : other.lastModifyBy.copy();
        this.communityLeaderId = other.communityLeaderId == null ? null : other.communityLeaderId.copy();
        this.communityNoticeId = other.communityNoticeId == null ? null : other.communityNoticeId.copy();
        this.homelandStationId = other.homelandStationId == null ? null : other.homelandStationId.copy();
    }

    @Override
    public CommunityCriteria copy() {
        return new CommunityCriteria(this);
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

    public StringFilter getCname() {
        return cname;
    }

    public StringFilter cname() {
        if (cname == null) {
            cname = new StringFilter();
        }
        return cname;
    }

    public void setCname(StringFilter cname) {
        this.cname = cname;
    }

    public StringFilter getManagerName() {
        return managerName;
    }

    public StringFilter managerName() {
        if (managerName == null) {
            managerName = new StringFilter();
        }
        return managerName;
    }

    public void setManagerName(StringFilter managerName) {
        this.managerName = managerName;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
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

    public StringFilter getOfficeHours() {
        return officeHours;
    }

    public StringFilter officeHours() {
        if (officeHours == null) {
            officeHours = new StringFilter();
        }
        return officeHours;
    }

    public void setOfficeHours(StringFilter officeHours) {
        this.officeHours = officeHours;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getSource() {
        return source;
    }

    public StringFilter source() {
        if (source == null) {
            source = new StringFilter();
        }
        return source;
    }

    public void setSource(StringFilter source) {
        this.source = source;
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

    public StringFilter getLongCode() {
        return longCode;
    }

    public StringFilter longCode() {
        if (longCode == null) {
            longCode = new StringFilter();
        }
        return longCode;
    }

    public void setLongCode(StringFilter longCode) {
        this.longCode = longCode;
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

    public LongFilter getCommunityLeaderId() {
        return communityLeaderId;
    }

    public LongFilter communityLeaderId() {
        if (communityLeaderId == null) {
            communityLeaderId = new LongFilter();
        }
        return communityLeaderId;
    }

    public void setCommunityLeaderId(LongFilter communityLeaderId) {
        this.communityLeaderId = communityLeaderId;
    }

    public LongFilter getCommunityNoticeId() {
        return communityNoticeId;
    }

    public LongFilter communityNoticeId() {
        if (communityNoticeId == null) {
            communityNoticeId = new LongFilter();
        }
        return communityNoticeId;
    }

    public void setCommunityNoticeId(LongFilter communityNoticeId) {
        this.communityNoticeId = communityNoticeId;
    }

    public LongFilter getHomelandStationId() {
        return homelandStationId;
    }

    public LongFilter homelandStationId() {
        if (homelandStationId == null) {
            homelandStationId = new LongFilter();
        }
        return homelandStationId;
    }

    public void setHomelandStationId(LongFilter homelandStationId) {
        this.homelandStationId = homelandStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommunityCriteria that = (CommunityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cname, that.cname) &&
            Objects.equals(managerName, that.managerName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(email, that.email) &&
            Objects.equals(officeHours, that.officeHours) &&
            Objects.equals(description, that.description) &&
            Objects.equals(source, that.source) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(ancestors, that.ancestors) &&
            Objects.equals(longCode, that.longCode) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(delFlag, that.delFlag) &&
            Objects.equals(orderNum, that.orderNum) &&
            Objects.equals(lastModifyDate, that.lastModifyDate) &&
            Objects.equals(lastModifyBy, that.lastModifyBy) &&
            Objects.equals(communityLeaderId, that.communityLeaderId) &&
            Objects.equals(communityNoticeId, that.communityNoticeId) &&
            Objects.equals(homelandStationId, that.homelandStationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            cname,
            managerName,
            address,
            tel,
            email,
            officeHours,
            description,
            source,
            parentId,
            ancestors,
            longCode,
            enable,
            delFlag,
            orderNum,
            lastModifyDate,
            lastModifyBy,
            communityLeaderId,
            communityNoticeId,
            homelandStationId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cname != null ? "cname=" + cname + ", " : "") +
            (managerName != null ? "managerName=" + managerName + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (tel != null ? "tel=" + tel + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (officeHours != null ? "officeHours=" + officeHours + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (source != null ? "source=" + source + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (ancestors != null ? "ancestors=" + ancestors + ", " : "") +
            (longCode != null ? "longCode=" + longCode + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (delFlag != null ? "delFlag=" + delFlag + ", " : "") +
            (orderNum != null ? "orderNum=" + orderNum + ", " : "") +
            (lastModifyDate != null ? "lastModifyDate=" + lastModifyDate + ", " : "") +
            (lastModifyBy != null ? "lastModifyBy=" + lastModifyBy + ", " : "") +
            (communityLeaderId != null ? "communityLeaderId=" + communityLeaderId + ", " : "") +
            (communityNoticeId != null ? "communityNoticeId=" + communityNoticeId + ", " : "") +
            (homelandStationId != null ? "homelandStationId=" + homelandStationId + ", " : "") +
            "}";
    }
}
