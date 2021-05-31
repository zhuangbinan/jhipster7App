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
 * Criteria class for the {@link com.mycompany.myapp.domain.Company} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter longCode;

    private StringFilter address;

    private StringFilter tel;

    private StringFilter email;

    private StringFilter managerName;

    private BooleanFilter enable;

    private LongFilter homelandStationId;

    public CompanyCriteria() {}

    public CompanyCriteria(CompanyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.longCode = other.longCode == null ? null : other.longCode.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.tel = other.tel == null ? null : other.tel.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.managerName = other.managerName == null ? null : other.managerName.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.homelandStationId = other.homelandStationId == null ? null : other.homelandStationId.copy();
    }

    @Override
    public CompanyCriteria copy() {
        return new CompanyCriteria(this);
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
        final CompanyCriteria that = (CompanyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(longCode, that.longCode) &&
            Objects.equals(address, that.address) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(email, that.email) &&
            Objects.equals(managerName, that.managerName) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(homelandStationId, that.homelandStationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, longCode, address, tel, email, managerName, enable, homelandStationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (longCode != null ? "longCode=" + longCode + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (tel != null ? "tel=" + tel + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (managerName != null ? "managerName=" + managerName + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (homelandStationId != null ? "homelandStationId=" + homelandStationId + ", " : "") +
            "}";
    }
}
