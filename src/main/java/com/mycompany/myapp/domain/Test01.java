package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Test01.
 */
@Entity
@Table(name = "test_01")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Test01 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "job_career_desc")
    private String jobCareerDesc;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Test01 id(Long id) {
        this.id = id;
        return this;
    }

    public String getJobCareerDesc() {
        return this.jobCareerDesc;
    }

    public Test01 jobCareerDesc(String jobCareerDesc) {
        this.jobCareerDesc = jobCareerDesc;
        return this;
    }

    public void setJobCareerDesc(String jobCareerDesc) {
        this.jobCareerDesc = jobCareerDesc;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Test01)) {
            return false;
        }
        return id != null && id.equals(((Test01) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Test01{" +
            "id=" + getId() +
            ", jobCareerDesc='" + getJobCareerDesc() + "'" +
            "}";
    }
}
