package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.CertificateType;
import com.mycompany.myapp.domain.enumeration.UserType;
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
 * Criteria class for the {@link com.mycompany.myapp.domain.WamoliUser} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.WamoliUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /wamoli-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WamoliUserCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CertificateType
     */
    public static class CertificateTypeFilter extends Filter<CertificateType> {

        public CertificateTypeFilter() {}

        public CertificateTypeFilter(CertificateTypeFilter filter) {
            super(filter);
        }

        @Override
        public CertificateTypeFilter copy() {
            return new CertificateTypeFilter(this);
        }
    }

    /**
     * Class for filtering UserType
     */
    public static class UserTypeFilter extends Filter<UserType> {

        public UserTypeFilter() {}

        public UserTypeFilter(UserTypeFilter filter) {
            super(filter);
        }

        @Override
        public UserTypeFilter copy() {
            return new UserTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userName;

    private StringFilter gender;

    private StringFilter phoneNum;

    private StringFilter email;

    private StringFilter unitAddr;

    private LongFilter roomAddr;

    private StringFilter idCardNum;

    private CertificateTypeFilter idCardType;

    private UserTypeFilter userType;

    private BooleanFilter enable;

    private BooleanFilter isManager;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter userId;

    private LongFilter roomAddrId;

    private LongFilter companyDeptId;

    public WamoliUserCriteria() {}

    public WamoliUserCriteria(WamoliUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.phoneNum = other.phoneNum == null ? null : other.phoneNum.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.unitAddr = other.unitAddr == null ? null : other.unitAddr.copy();
        this.roomAddr = other.roomAddr == null ? null : other.roomAddr.copy();
        this.idCardNum = other.idCardNum == null ? null : other.idCardNum.copy();
        this.idCardType = other.idCardType == null ? null : other.idCardType.copy();
        this.userType = other.userType == null ? null : other.userType.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.isManager = other.isManager == null ? null : other.isManager.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.roomAddrId = other.roomAddrId == null ? null : other.roomAddrId.copy();
        this.companyDeptId = other.companyDeptId == null ? null : other.companyDeptId.copy();
    }

    @Override
    public WamoliUserCriteria copy() {
        return new WamoliUserCriteria(this);
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

    public StringFilter getUserName() {
        return userName;
    }

    public StringFilter userName() {
        if (userName == null) {
            userName = new StringFilter();
        }
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public StringFilter getGender() {
        return gender;
    }

    public StringFilter gender() {
        if (gender == null) {
            gender = new StringFilter();
        }
        return gender;
    }

    public void setGender(StringFilter gender) {
        this.gender = gender;
    }

    public StringFilter getPhoneNum() {
        return phoneNum;
    }

    public StringFilter phoneNum() {
        if (phoneNum == null) {
            phoneNum = new StringFilter();
        }
        return phoneNum;
    }

    public void setPhoneNum(StringFilter phoneNum) {
        this.phoneNum = phoneNum;
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

    public StringFilter getUnitAddr() {
        return unitAddr;
    }

    public StringFilter unitAddr() {
        if (unitAddr == null) {
            unitAddr = new StringFilter();
        }
        return unitAddr;
    }

    public void setUnitAddr(StringFilter unitAddr) {
        this.unitAddr = unitAddr;
    }

    public LongFilter getRoomAddr() {
        return roomAddr;
    }

    public LongFilter roomAddr() {
        if (roomAddr == null) {
            roomAddr = new LongFilter();
        }
        return roomAddr;
    }

    public void setRoomAddr(LongFilter roomAddr) {
        this.roomAddr = roomAddr;
    }

    public StringFilter getIdCardNum() {
        return idCardNum;
    }

    public StringFilter idCardNum() {
        if (idCardNum == null) {
            idCardNum = new StringFilter();
        }
        return idCardNum;
    }

    public void setIdCardNum(StringFilter idCardNum) {
        this.idCardNum = idCardNum;
    }

    public CertificateTypeFilter getIdCardType() {
        return idCardType;
    }

    public CertificateTypeFilter idCardType() {
        if (idCardType == null) {
            idCardType = new CertificateTypeFilter();
        }
        return idCardType;
    }

    public void setIdCardType(CertificateTypeFilter idCardType) {
        this.idCardType = idCardType;
    }

    public UserTypeFilter getUserType() {
        return userType;
    }

    public UserTypeFilter userType() {
        if (userType == null) {
            userType = new UserTypeFilter();
        }
        return userType;
    }

    public void setUserType(UserTypeFilter userType) {
        this.userType = userType;
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

    public BooleanFilter getIsManager() {
        return isManager;
    }

    public BooleanFilter isManager() {
        if (isManager == null) {
            isManager = new BooleanFilter();
        }
        return isManager;
    }

    public void setIsManager(BooleanFilter isManager) {
        this.isManager = isManager;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getRoomAddrId() {
        return roomAddrId;
    }

    public LongFilter roomAddrId() {
        if (roomAddrId == null) {
            roomAddrId = new LongFilter();
        }
        return roomAddrId;
    }

    public void setRoomAddrId(LongFilter roomAddrId) {
        this.roomAddrId = roomAddrId;
    }

    public LongFilter getCompanyDeptId() {
        return companyDeptId;
    }

    public LongFilter companyDeptId() {
        if (companyDeptId == null) {
            companyDeptId = new LongFilter();
        }
        return companyDeptId;
    }

    public void setCompanyDeptId(LongFilter companyDeptId) {
        this.companyDeptId = companyDeptId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WamoliUserCriteria that = (WamoliUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(phoneNum, that.phoneNum) &&
            Objects.equals(email, that.email) &&
            Objects.equals(unitAddr, that.unitAddr) &&
            Objects.equals(roomAddr, that.roomAddr) &&
            Objects.equals(idCardNum, that.idCardNum) &&
            Objects.equals(idCardType, that.idCardType) &&
            Objects.equals(userType, that.userType) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(isManager, that.isManager) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(roomAddrId, that.roomAddrId) &&
            Objects.equals(companyDeptId, that.companyDeptId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            userName,
            gender,
            phoneNum,
            email,
            unitAddr,
            roomAddr,
            idCardNum,
            idCardType,
            userType,
            enable,
            isManager,
            lastModifiedBy,
            lastModifiedDate,
            userId,
            roomAddrId,
            companyDeptId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WamoliUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userName != null ? "userName=" + userName + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (phoneNum != null ? "phoneNum=" + phoneNum + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (unitAddr != null ? "unitAddr=" + unitAddr + ", " : "") +
            (roomAddr != null ? "roomAddr=" + roomAddr + ", " : "") +
            (idCardNum != null ? "idCardNum=" + idCardNum + ", " : "") +
            (idCardType != null ? "idCardType=" + idCardType + ", " : "") +
            (userType != null ? "userType=" + userType + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (isManager != null ? "isManager=" + isManager + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (roomAddrId != null ? "roomAddrId=" + roomAddrId + ", " : "") +
            (companyDeptId != null ? "companyDeptId=" + companyDeptId + ", " : "") +
            "}";
    }
}
