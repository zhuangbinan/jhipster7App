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
 * Criteria class for the {@link com.mycompany.myapp.domain.Test01} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.Test01Resource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /test-01-s?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class Test01Criteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    public Test01Criteria() {}

    public Test01Criteria(Test01Criteria other) {
        this.id = other.id == null ? null : other.id.copy();
    }

    @Override
    public Test01Criteria copy() {
        return new Test01Criteria(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Test01Criteria that = (Test01Criteria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Test01Criteria{" +
            (id != null ? "id=" + id + ", " : "") +
            "}";
    }
}
