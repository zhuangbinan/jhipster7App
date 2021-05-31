package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.CmdType;
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
 * Criteria class for the {@link com.mycompany.myapp.domain.TuYaCmd} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TuYaCmdResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tu-ya-cmds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TuYaCmdCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CmdType
     */
    public static class CmdTypeFilter extends Filter<CmdType> {

        public CmdTypeFilter() {}

        public CmdTypeFilter(CmdTypeFilter filter) {
            super(filter);
        }

        @Override
        public CmdTypeFilter copy() {
            return new CmdTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cmdName;

    private StringFilter cmdCode;

    private BooleanFilter value;

    private CmdTypeFilter cmdType;

    private InstantFilter createTime;

    private BooleanFilter enable;

    private LongFilter tuYaDeviceId;

    public TuYaCmdCriteria() {}

    public TuYaCmdCriteria(TuYaCmdCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cmdName = other.cmdName == null ? null : other.cmdName.copy();
        this.cmdCode = other.cmdCode == null ? null : other.cmdCode.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.cmdType = other.cmdType == null ? null : other.cmdType.copy();
        this.createTime = other.createTime == null ? null : other.createTime.copy();
        this.enable = other.enable == null ? null : other.enable.copy();
        this.tuYaDeviceId = other.tuYaDeviceId == null ? null : other.tuYaDeviceId.copy();
    }

    @Override
    public TuYaCmdCriteria copy() {
        return new TuYaCmdCriteria(this);
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

    public StringFilter getCmdName() {
        return cmdName;
    }

    public StringFilter cmdName() {
        if (cmdName == null) {
            cmdName = new StringFilter();
        }
        return cmdName;
    }

    public void setCmdName(StringFilter cmdName) {
        this.cmdName = cmdName;
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

    public BooleanFilter getValue() {
        return value;
    }

    public BooleanFilter value() {
        if (value == null) {
            value = new BooleanFilter();
        }
        return value;
    }

    public void setValue(BooleanFilter value) {
        this.value = value;
    }

    public CmdTypeFilter getCmdType() {
        return cmdType;
    }

    public CmdTypeFilter cmdType() {
        if (cmdType == null) {
            cmdType = new CmdTypeFilter();
        }
        return cmdType;
    }

    public void setCmdType(CmdTypeFilter cmdType) {
        this.cmdType = cmdType;
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

    public LongFilter getTuYaDeviceId() {
        return tuYaDeviceId;
    }

    public LongFilter tuYaDeviceId() {
        if (tuYaDeviceId == null) {
            tuYaDeviceId = new LongFilter();
        }
        return tuYaDeviceId;
    }

    public void setTuYaDeviceId(LongFilter tuYaDeviceId) {
        this.tuYaDeviceId = tuYaDeviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TuYaCmdCriteria that = (TuYaCmdCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cmdName, that.cmdName) &&
            Objects.equals(cmdCode, that.cmdCode) &&
            Objects.equals(value, that.value) &&
            Objects.equals(cmdType, that.cmdType) &&
            Objects.equals(createTime, that.createTime) &&
            Objects.equals(enable, that.enable) &&
            Objects.equals(tuYaDeviceId, that.tuYaDeviceId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cmdName, cmdCode, value, cmdType, createTime, enable, tuYaDeviceId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TuYaCmdCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cmdName != null ? "cmdName=" + cmdName + ", " : "") +
            (cmdCode != null ? "cmdCode=" + cmdCode + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (cmdType != null ? "cmdType=" + cmdType + ", " : "") +
            (createTime != null ? "createTime=" + createTime + ", " : "") +
            (enable != null ? "enable=" + enable + ", " : "") +
            (tuYaDeviceId != null ? "tuYaDeviceId=" + tuYaDeviceId + ", " : "") +
            "}";
    }
}
