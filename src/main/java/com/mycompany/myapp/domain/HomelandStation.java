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
 * 小区站点
 */
@ApiModel(description = "小区站点")
@Entity
@Table(name = "wamoli_homeland_station")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HomelandStation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 小区名称
     */
    @NotNull
    @ApiModelProperty(value = "小区名称", required = true)
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
     * 小区地址
     */
    @ApiModelProperty(value = "小区地址")
    @Column(name = "address")
    private String address;

    /**
     * 小区入住人数
     */
    @ApiModelProperty(value = "小区入住人数")
    @Column(name = "living_population")
    private Integer livingPopulation;

    /**
     * 小区楼栋
     */
    @ApiModelProperty(value = "小区楼栋")
    @Column(name = "building_count")
    private Integer buildingCount;

    /**
     * 小区入口数
     */
    @ApiModelProperty(value = "小区入口数")
    @Column(name = "entrance_count")
    private Integer entranceCount;

    /**
     * 小区图片
     */

    @ApiModelProperty(value = "小区图片")
    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    /**
     * 经度
     */
    @NotNull
    @Size(max = 32)
    @ApiModelProperty(value = "经度", required = true)
    @Column(name = "longitude", length = 32, nullable = false)
    private String longitude;

    /**
     * 纬度
     */
    @NotNull
    @Size(max = 32)
    @ApiModelProperty(value = "纬度", required = true)
    @Column(name = "latitude", length = 32, nullable = false)
    private String latitude;

    @OneToMany(mappedBy = "homelandStation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roomAddrs", "homelandStation" }, allowSetters = true)
    private Set<Buildings> buildings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "communityLeaders", "communityNotices", "homelandStations" }, allowSetters = true)
    private Community community;

    @ManyToOne
    @JsonIgnoreProperties(value = { "homelandStations" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HomelandStation id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public HomelandStation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongCode() {
        return this.longCode;
    }

    public HomelandStation longCode(String longCode) {
        this.longCode = longCode;
        return this;
    }

    public void setLongCode(String longCode) {
        this.longCode = longCode;
    }

    public String getAddress() {
        return this.address;
    }

    public HomelandStation address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLivingPopulation() {
        return this.livingPopulation;
    }

    public HomelandStation livingPopulation(Integer livingPopulation) {
        this.livingPopulation = livingPopulation;
        return this;
    }

    public void setLivingPopulation(Integer livingPopulation) {
        this.livingPopulation = livingPopulation;
    }

    public Integer getBuildingCount() {
        return this.buildingCount;
    }

    public HomelandStation buildingCount(Integer buildingCount) {
        this.buildingCount = buildingCount;
        return this;
    }

    public void setBuildingCount(Integer buildingCount) {
        this.buildingCount = buildingCount;
    }

    public Integer getEntranceCount() {
        return this.entranceCount;
    }

    public HomelandStation entranceCount(Integer entranceCount) {
        this.entranceCount = entranceCount;
        return this;
    }

    public void setEntranceCount(Integer entranceCount) {
        this.entranceCount = entranceCount;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public HomelandStation logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public HomelandStation logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public HomelandStation longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public HomelandStation latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Set<Buildings> getBuildings() {
        return this.buildings;
    }

    public HomelandStation buildings(Set<Buildings> buildings) {
        this.setBuildings(buildings);
        return this;
    }

    public HomelandStation addBuildings(Buildings buildings) {
        this.buildings.add(buildings);
        buildings.setHomelandStation(this);
        return this;
    }

    public HomelandStation removeBuildings(Buildings buildings) {
        this.buildings.remove(buildings);
        buildings.setHomelandStation(null);
        return this;
    }

    public void setBuildings(Set<Buildings> buildings) {
        if (this.buildings != null) {
            this.buildings.forEach(i -> i.setHomelandStation(null));
        }
        if (buildings != null) {
            buildings.forEach(i -> i.setHomelandStation(this));
        }
        this.buildings = buildings;
    }

    public Community getCommunity() {
        return this.community;
    }

    public HomelandStation community(Community community) {
        this.setCommunity(community);
        return this;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Company getCompany() {
        return this.company;
    }

    public HomelandStation company(Company company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HomelandStation)) {
            return false;
        }
        return id != null && id.equals(((HomelandStation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HomelandStation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", longCode='" + getLongCode() + "'" +
            ", address='" + getAddress() + "'" +
            ", livingPopulation=" + getLivingPopulation() +
            ", buildingCount=" + getBuildingCount() +
            ", entranceCount=" + getEntranceCount() +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", latitude='" + getLatitude() + "'" +
            "}";
    }
}
