package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 物业公司
 */
@ApiModel(description = "物业公司")
@Entity
@Table(name = "wamoli_company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 公司名称
     */
    @NotNull
    @ApiModelProperty(value = "公司名称", required = true)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 编码
     */
    @NotNull
    @ApiModelProperty(value = "编码", required = true)
    @Column(name = "long_code", nullable = false, unique = true)
    private String longCode;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    @Column(name = "address")
    private String address;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    @Column(name = "tel")
    private String tel;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Column(name = "email")
    private String email;

    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    @Column(name = "manager_name")
    private String managerName;

    /**
     * 是否生效
     */
    @ApiModelProperty(value = "是否生效")
    @Column(name = "enable")
    private Boolean enable;

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "buildings", "community", "company" }, allowSetters = true)
    private Set<HomelandStation> homelandStations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongCode() {
        return this.longCode;
    }

    public Company longCode(String longCode) {
        this.longCode = longCode;
        return this;
    }

    public void setLongCode(String longCode) {
        this.longCode = longCode;
    }

    public String getAddress() {
        return this.address;
    }

    public Company address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return this.tel;
    }

    public Company tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return this.email;
    }

    public Company email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getManagerName() {
        return this.managerName;
    }

    public Company managerName(String managerName) {
        this.managerName = managerName;
        return this;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public Company enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Set<HomelandStation> getHomelandStations() {
        return this.homelandStations;
    }

    public Company homelandStations(Set<HomelandStation> homelandStations) {
        this.setHomelandStations(homelandStations);
        return this;
    }

    public Company addHomelandStation(HomelandStation homelandStation) {
        this.homelandStations.add(homelandStation);
        homelandStation.setCompany(this);
        return this;
    }

    public Company removeHomelandStation(HomelandStation homelandStation) {
        this.homelandStations.remove(homelandStation);
        homelandStation.setCompany(null);
        return this;
    }

    public void setHomelandStations(Set<HomelandStation> homelandStations) {
        if (this.homelandStations != null) {
            this.homelandStations.forEach(i -> i.setCompany(null));
        }
        if (homelandStations != null) {
            homelandStations.forEach(i -> i.setCompany(this));
        }
        this.homelandStations = homelandStations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", longCode='" + getLongCode() + "'" +
            ", address='" + getAddress() + "'" +
            ", tel='" + getTel() + "'" +
            ", email='" + getEmail() + "'" +
            ", managerName='" + getManagerName() + "'" +
            ", enable='" + getEnable() + "'" +
            "}";
    }
}
