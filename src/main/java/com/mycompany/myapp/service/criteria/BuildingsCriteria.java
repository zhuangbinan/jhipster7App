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
 * Criteria class for the {@link com.mycompany.myapp.domain.Buildings} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BuildingsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /buildings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BuildingsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter longCode;

    private IntegerFilter floorCount;

    private IntegerFilter unites;

    private StringFilter longitude;

    private StringFilter latitude;

    private BooleanFilter enable;

    private LongFilter roomAddrId;

    private LongFilter homelandStationId;

    public BuildingsCriteria() {}

    public BuildingsCriteria(BuildingsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.longCode = other.longCode == null ? null : other.longCode.copy();
        this.floorCount = other.floorCount == null ? null : other.floorCount.copy();
        this.unites = other.unites == null ? null : other.unites.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.roomAddrId = other.roomAddrId == null ? null : other.roomAddrId.copy();
        this.homelandStationId = other.homelandStationId == null ? null : other.homelandStationId.copy();
    }

    @Override
    public BuildingsCriteria copy() {
        return new BuildingsCriteria(this);
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

    public IntegerFilter getFloorCount() {
        return floorCount;
    }

    public IntegerFilter floorCount() {
        if (floorCount == null) {
            floorCount = new IntegerFilter();
        }
        return floorCount;
    }

    public void setFloorCount(IntegerFilter floorCount) {
        this.floorCount = floorCount;
    }

    public IntegerFilter getUnites() {
        return unites;
    }

    public IntegerFilter unites() {
        if (unites == null) {
            unites = new IntegerFilter();
        }
        return unites;
    }

    public void setUnites(IntegerFilter unites) {
        this.unites = unites;
    }

    public StringFilter getLongitude() {
        return longitude;
    }

    public StringFilter longitude() {
        if (longitude == null) {
            longitude = new StringFilter();
        }
        return longitude;
    }

    public void setLongitude(StringFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getLatitude() {
        return latitude;
    }

    public StringFilter latitude() {
        if (latitude == null) {
            latitude = new StringFilter();
        }
        return latitude;
    }

    public void setLatitude(StringFilter latitude) {
        this.latitude = latitude;
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
        final BuildingsCriteria that = (BuildingsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(longCode, that.longCode) &&
            Objects.equals(floorCount, that.floorCount) &&
            Objects.equals(unites, that.unites) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(roomAddrId, that.roomAddrId) &&
            Objects.equals(homelandStationId, that.homelandStationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, longCode, floorCount, unites, longitude, latitude, enable, roomAddrId, homelandStationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BuildingsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (longCode != null ? "longCode=" + longCode + ", " : "") +
            (floorCount != null ? "floorCount=" + floorCount + ", " : "") +
            (unites != null ? "unites=" + unites + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (roomAddrId != null ? "roomAddrId=" + roomAddrId + ", " : "") +
            (homelandStationId != null ? "homelandStationId=" + homelandStationId + ", " : "") +
            "}";
    }
}
