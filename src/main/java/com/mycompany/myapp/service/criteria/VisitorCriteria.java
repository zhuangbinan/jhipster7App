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
 * Criteria class for the {@link com.mycompany.myapp.domain.Visitor} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.VisitorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /visitors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VisitorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter phoneNum;

    private StringFilter carPlateNum;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private StringFilter passwd;

    private StringFilter whichEntrance;

    private LongFilter roomAddrId;

    public VisitorCriteria() {}

    public VisitorCriteria(VisitorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.phoneNum = other.phoneNum == null ? null : other.phoneNum.copy();
        this.carPlateNum = other.carPlateNum == null ? null : other.carPlateNum.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.passwd = other.passwd == null ? null : other.passwd.copy();
        this.whichEntrance = other.whichEntrance == null ? null : other.whichEntrance.copy();
        this.roomAddrId = other.roomAddrId == null ? null : other.roomAddrId.copy();
    }

    @Override
    public VisitorCriteria copy() {
        return new VisitorCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public StringFilter getCarPlateNum() {
        return carPlateNum;
    }

    public StringFilter carPlateNum() {
        if (carPlateNum == null) {
            carPlateNum = new StringFilter();
        }
        return carPlateNum;
    }

    public void setCarPlateNum(StringFilter carPlateNum) {
        this.carPlateNum = carPlateNum;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            startTime = new InstantFilter();
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            endTime = new InstantFilter();
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public StringFilter getPasswd() {
        return passwd;
    }

    public StringFilter passwd() {
        if (passwd == null) {
            passwd = new StringFilter();
        }
        return passwd;
    }

    public void setPasswd(StringFilter passwd) {
        this.passwd = passwd;
    }

    public StringFilter getWhichEntrance() {
        return whichEntrance;
    }

    public StringFilter whichEntrance() {
        if (whichEntrance == null) {
            whichEntrance = new StringFilter();
        }
        return whichEntrance;
    }

    public void setWhichEntrance(StringFilter whichEntrance) {
        this.whichEntrance = whichEntrance;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VisitorCriteria that = (VisitorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(phoneNum, that.phoneNum) &&
            Objects.equals(carPlateNum, that.carPlateNum) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(passwd, that.passwd) &&
            Objects.equals(whichEntrance, that.whichEntrance) &&
            Objects.equals(roomAddrId, that.roomAddrId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneNum, carPlateNum, startTime, endTime, passwd, whichEntrance, roomAddrId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisitorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (phoneNum != null ? "phoneNum=" + phoneNum + ", " : "") +
            (carPlateNum != null ? "carPlateNum=" + carPlateNum + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (passwd != null ? "passwd=" + passwd + ", " : "") +
            (whichEntrance != null ? "whichEntrance=" + whichEntrance + ", " : "") +
            (roomAddrId != null ? "roomAddrId=" + roomAddrId + ", " : "") +
            "}";
    }
}
