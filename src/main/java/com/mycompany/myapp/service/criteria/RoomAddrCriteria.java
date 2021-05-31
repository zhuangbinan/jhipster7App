package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.RoomAddr} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.RoomAddrResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /room-addrs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RoomAddrCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter roomNum;

    private StringFilter unit;

    private StringFilter roomType;

    private FloatFilter roomArea;

    private BooleanFilter used;

    private BooleanFilter autoControl;

    private StringFilter longCode;

    private LongFilter visitorId;

    private LongFilter buildingsId;

    private LongFilter wamoliUserId;

    public RoomAddrCriteria() {}

    public RoomAddrCriteria(RoomAddrCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.roomNum = other.roomNum == null ? null : other.roomNum.copy();
        this.unit = other.unit == null ? null : other.unit.copy();
        this.roomType = other.roomType == null ? null : other.roomType.copy();
        this.roomArea = other.roomArea == null ? null : other.roomArea.copy();
        this.used = other.used == null ? null : other.used.copy();
        this.autoControl = other.autoControl == null ? null : other.autoControl.copy();
        this.longCode = other.longCode == null ? null : other.longCode.copy();
        this.visitorId = other.visitorId == null ? null : other.visitorId.copy();
        this.buildingsId = other.buildingsId == null ? null : other.buildingsId.copy();
        this.wamoliUserId = other.wamoliUserId == null ? null : other.wamoliUserId.copy();
    }

    @Override
    public RoomAddrCriteria copy() {
        return new RoomAddrCriteria(this);
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

    public StringFilter getRoomNum() {
        return roomNum;
    }

    public StringFilter roomNum() {
        if (roomNum == null) {
            roomNum = new StringFilter();
        }
        return roomNum;
    }

    public void setRoomNum(StringFilter roomNum) {
        this.roomNum = roomNum;
    }

    public StringFilter getUnit() {
        return unit;
    }

    public StringFilter unit() {
        if (unit == null) {
            unit = new StringFilter();
        }
        return unit;
    }

    public void setUnit(StringFilter unit) {
        this.unit = unit;
    }

    public StringFilter getRoomType() {
        return roomType;
    }

    public StringFilter roomType() {
        if (roomType == null) {
            roomType = new StringFilter();
        }
        return roomType;
    }

    public void setRoomType(StringFilter roomType) {
        this.roomType = roomType;
    }

    public FloatFilter getRoomArea() {
        return roomArea;
    }

    public FloatFilter roomArea() {
        if (roomArea == null) {
            roomArea = new FloatFilter();
        }
        return roomArea;
    }

    public void setRoomArea(FloatFilter roomArea) {
        this.roomArea = roomArea;
    }

    public BooleanFilter getUsed() {
        return used;
    }

    public BooleanFilter used() {
        if (used == null) {
            used = new BooleanFilter();
        }
        return used;
    }

    public void setUsed(BooleanFilter used) {
        this.used = used;
    }

    public BooleanFilter getAutoControl() {
        return autoControl;
    }

    public BooleanFilter autoControl() {
        if (autoControl == null) {
            autoControl = new BooleanFilter();
        }
        return autoControl;
    }

    public void setAutoControl(BooleanFilter autoControl) {
        this.autoControl = autoControl;
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

    public LongFilter getVisitorId() {
        return visitorId;
    }

    public LongFilter visitorId() {
        if (visitorId == null) {
            visitorId = new LongFilter();
        }
        return visitorId;
    }

    public void setVisitorId(LongFilter visitorId) {
        this.visitorId = visitorId;
    }

    public LongFilter getBuildingsId() {
        return buildingsId;
    }

    public LongFilter buildingsId() {
        if (buildingsId == null) {
            buildingsId = new LongFilter();
        }
        return buildingsId;
    }

    public void setBuildingsId(LongFilter buildingsId) {
        this.buildingsId = buildingsId;
    }

    public LongFilter getWamoliUserId() {
        return wamoliUserId;
    }

    public LongFilter wamoliUserId() {
        if (wamoliUserId == null) {
            wamoliUserId = new LongFilter();
        }
        return wamoliUserId;
    }

    public void setWamoliUserId(LongFilter wamoliUserId) {
        this.wamoliUserId = wamoliUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RoomAddrCriteria that = (RoomAddrCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(roomNum, that.roomNum) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(roomType, that.roomType) &&
            Objects.equals(roomArea, that.roomArea) &&
            Objects.equals(used, that.used) &&
            Objects.equals(autoControl, that.autoControl) &&
            Objects.equals(longCode, that.longCode) &&
            Objects.equals(visitorId, that.visitorId) &&
            Objects.equals(buildingsId, that.buildingsId) &&
            Objects.equals(wamoliUserId, that.wamoliUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomNum, unit, roomType, roomArea, used, autoControl, longCode, visitorId, buildingsId, wamoliUserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomAddrCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (roomNum != null ? "roomNum=" + roomNum + ", " : "") +
            (unit != null ? "unit=" + unit + ", " : "") +
            (roomType != null ? "roomType=" + roomType + ", " : "") +
            (roomArea != null ? "roomArea=" + roomArea + ", " : "") +
            (used != null ? "used=" + used + ", " : "") +
            (autoControl != null ? "autoControl=" + autoControl + ", " : "") +
            (longCode != null ? "longCode=" + longCode + ", " : "") +
            (visitorId != null ? "visitorId=" + visitorId + ", " : "") +
            (buildingsId != null ? "buildingsId=" + buildingsId + ", " : "") +
            (wamoliUserId != null ? "wamoliUserId=" + wamoliUserId + ", " : "") +
            "}";
    }
}
