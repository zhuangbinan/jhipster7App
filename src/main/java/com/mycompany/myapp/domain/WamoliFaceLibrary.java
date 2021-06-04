package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WamoliFaceLibrary.
 */
@Entity
@Table(name = "wamoli_face_library")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WamoliFaceLibrary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @JsonIgnoreProperties(value = { "user", "roomAddrs", "companyDepts", "companyPosts" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private WamoliUser wamoliUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WamoliFaceLibrary id(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public WamoliFaceLibrary content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public WamoliUser getWamoliUser() {
        return this.wamoliUser;
    }

    public WamoliFaceLibrary wamoliUser(WamoliUser wamoliUser) {
        this.setWamoliUser(wamoliUser);
        return this;
    }

    public void setWamoliUser(WamoliUser wamoliUser) {
        this.wamoliUser = wamoliUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WamoliFaceLibrary)) {
            return false;
        }
        return id != null && id.equals(((WamoliFaceLibrary) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WamoliFaceLibrary{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            "}";
    }
}
