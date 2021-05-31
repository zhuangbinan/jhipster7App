package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Test01} entity.
 */
public class Test01DTO implements Serializable {

    private Long id;

    @Lob
    private String jobCareerDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobCareerDesc() {
        return jobCareerDesc;
    }

    public void setJobCareerDesc(String jobCareerDesc) {
        this.jobCareerDesc = jobCareerDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Test01DTO)) {
            return false;
        }

        Test01DTO test01DTO = (Test01DTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, test01DTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Test01DTO{" +
            "id=" + getId() +
            ", jobCareerDesc='" + getJobCareerDesc() + "'" +
            "}";
    }
}
