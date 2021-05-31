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
 * Criteria class for the {@link com.mycompany.myapp.domain.HomelandStation} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.HomelandStationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /homeland-stations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HomelandStationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter longCode;

    private StringFilter address;

    private IntegerFilter livingPopulation;

    private IntegerFilter buildingCount;

    private IntegerFilter entranceCount;

    private StringFilter longitude;

    private StringFilter latitude;

    private LongFilter buildingsId;

    private LongFilter communityId;

    private LongFilter companyId;

    public HomelandStationCriteria() {}

    public HomelandStationCriteria(HomelandStationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.longCode = other.longCode == null ? null : other.longCode.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.livingPopulation = other.livingPopulation == null ? null : other.livingPopulation.copy();
        this.buildingCount = other.buildingCount == null ? null : other.buildingCount.copy();
        this.entranceCount = other.entranceCount == null ? null : other.entranceCount.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.buildingsId = other.buildingsId == null ? null : other.buildingsId.copy();
        this.communityId = other.communityId == null ? null : other.communityId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
    }

    @Override
    public HomelandStationCriteria copy() {
        return new HomelandStationCriteria(this);
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

    public IntegerFilter getLivingPopulation() {
        return livingPopulation;
    }

    public IntegerFilter livingPopulation() {
        if (livingPopulation == null) {
            livingPopulation = new IntegerFilter();
        }
        return livingPopulation;
    }

    public void setLivingPopulation(IntegerFilter livingPopulation) {
        this.livingPopulation = livingPopulation;
    }

    public IntegerFilter getBuildingCount() {
        return buildingCount;
    }

    public IntegerFilter buildingCount() {
        if (buildingCount == null) {
            buildingCount = new IntegerFilter();
        }
        return buildingCount;
    }

    public void setBuildingCount(IntegerFilter buildingCount) {
        this.buildingCount = buildingCount;
    }

    public IntegerFilter getEntranceCount() {
        return entranceCount;
    }

    public IntegerFilter entranceCount() {
        if (entranceCount == null) {
            entranceCount = new IntegerFilter();
        }
        return entranceCount;
    }

    public void setEntranceCount(IntegerFilter entranceCount) {
        this.entranceCount = entranceCount;
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

    public LongFilter getCompanyId() {
        return companyId;
    }

    public LongFilter companyId() {
        if (companyId == null) {
            companyId = new LongFilter();
        }
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HomelandStationCriteria that = (HomelandStationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(longCode, that.longCode) &&
            Objects.equals(address, that.address) &&
            Objects.equals(livingPopulation, that.livingPopulation) &&
            Objects.equals(buildingCount, that.buildingCount) &&
            Objects.equals(entranceCount, that.entranceCount) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(buildingsId, that.buildingsId) &&
            Objects.equals(communityId, that.communityId) &&
            Objects.equals(companyId, that.companyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            longCode,
            address,
            livingPopulation,
            buildingCount,
            entranceCount,
            longitude,
            latitude,
            buildingsId,
            communityId,
            companyId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HomelandStationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (longCode != null ? "longCode=" + longCode + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (livingPopulation != null ? "livingPopulation=" + livingPopulation + ", " : "") +
            (buildingCount != null ? "buildingCount=" + buildingCount + ", " : "") +
            (entranceCount != null ? "entranceCount=" + entranceCount + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (buildingsId != null ? "buildingsId=" + buildingsId + ", " : "") +
            (communityId != null ? "communityId=" + communityId + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            "}";
    }
}
