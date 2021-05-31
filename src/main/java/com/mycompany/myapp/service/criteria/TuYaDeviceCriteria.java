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
 * Criteria class for the {@link com.mycompany.myapp.domain.TuYaDevice} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TuYaDeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tu-ya-devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TuYaDeviceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deviceName;

    private LongFilter longCode;

    private StringFilter tyDeviceId;

    private IntegerFilter deviceType;

    private StringFilter cmdCode;

    private InstantFilter createTime;

    private BooleanFilter enable;

    private LongFilter tuYaCmdId;

    private LongFilter roomAddrId;

    public TuYaDeviceCriteria() {}

    public TuYaDeviceCriteria(TuYaDeviceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deviceName = other.deviceName == null ? null : other.deviceName.copy();
        this.longCode = other.longCode == null ? null : other.longCode.copy();
        this.tyDeviceId = other.tyDeviceId == null ? null : other.tyDeviceId.copy();
        this.deviceType = other.deviceType == null ? null : other.deviceType.copy();
        this.cmdCode = other.cmdCode == null ? null : other.cmdCode.copy();
        this.createTime = other.createTime == null ? null : other.createTime.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.tuYaCmdId = other.tuYaCmdId == null ? null : other.tuYaCmdId.copy();
        this.roomAddrId = other.roomAddrId == null ? null : other.roomAddrId.copy();
    }

    @Override
    public TuYaDeviceCriteria copy() {
        return new TuYaDeviceCriteria(this);
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

    public StringFilter getDeviceName() {
        return deviceName;
    }

    public StringFilter deviceName() {
        if (deviceName == null) {
            deviceName = new StringFilter();
        }
        return deviceName;
    }

    public void setDeviceName(StringFilter deviceName) {
        this.deviceName = deviceName;
    }

    public LongFilter getLongCode() {
        return longCode;
    }

    public LongFilter longCode() {
        if (longCode == null) {
            longCode = new LongFilter();
        }
        return longCode;
    }

    public void setLongCode(LongFilter longCode) {
        this.longCode = longCode;
    }

    public StringFilter getTyDeviceId() {
        return tyDeviceId;
    }

    public StringFilter tyDeviceId() {
        if (tyDeviceId == null) {
            tyDeviceId = new StringFilter();
        }
        return tyDeviceId;
    }

    public void setTyDeviceId(StringFilter tyDeviceId) {
        this.tyDeviceId = tyDeviceId;
    }

    public IntegerFilter getDeviceType() {
        return deviceType;
    }

    public IntegerFilter deviceType() {
        if (deviceType == null) {
            deviceType = new IntegerFilter();
        }
        return deviceType;
    }

    public void setDeviceType(IntegerFilter deviceType) {
        this.deviceType = deviceType;
    }

    public StringFilter getCmdCode() {
        return cmdCode;
    }

    public StringFilter cmdCode() {
        if (cmdCode == null) {
            cmdCode = new StringFilter();
        }
        return cmdCode;
    }

    public void setCmdCode(StringFilter cmdCode) {
        this.cmdCode = cmdCode;
    }

    public InstantFilter getCreateTime() {
        return createTime;
    }

    public InstantFilter createTime() {
        if (createTime == null) {
            createTime = new InstantFilter();
        }
        return createTime;
    }

    public void setCreateTime(InstantFilter createTime) {
        this.createTime = createTime;
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

    public LongFilter getTuYaCmdId() {
        return tuYaCmdId;
    }

    public LongFilter tuYaCmdId() {
        if (tuYaCmdId == null) {
            tuYaCmdId = new LongFilter();
        }
        return tuYaCmdId;
    }

    public void setTuYaCmdId(LongFilter tuYaCmdId) {
        this.tuYaCmdId = tuYaCmdId;
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
        final TuYaDeviceCriteria that = (TuYaDeviceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deviceName, that.deviceName) &&
            Objects.equals(longCode, that.longCode) &&
            Objects.equals(tyDeviceId, that.tyDeviceId) &&
            Objects.equals(deviceType, that.deviceType) &&
            Objects.equals(cmdCode, that.cmdCode) &&
            Objects.equals(createTime, that.createTime) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(tuYaCmdId, that.tuYaCmdId) &&
            Objects.equals(roomAddrId, that.roomAddrId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deviceName, longCode, tyDeviceId, deviceType, cmdCode, createTime, enable, tuYaCmdId, roomAddrId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TuYaDeviceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (deviceName != null ? "deviceName=" + deviceName + ", " : "") +
            (longCode != null ? "longCode=" + longCode + ", " : "") +
            (tyDeviceId != null ? "tyDeviceId=" + tyDeviceId + ", " : "") +
            (deviceType != null ? "deviceType=" + deviceType + ", " : "") +
            (cmdCode != null ? "cmdCode=" + cmdCode + ", " : "") +
            (createTime != null ? "createTime=" + createTime + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (tuYaCmdId != null ? "tuYaCmdId=" + tuYaCmdId + ", " : "") +
            (roomAddrId != null ? "roomAddrId=" + roomAddrId + ", " : "") +
            "}";
    }
}
