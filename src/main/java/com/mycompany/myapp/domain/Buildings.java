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
 * 楼栋建筑
 */
@ApiModel(description = "楼栋建筑")
@Entity
@Table(name = "wamoli_buildings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Buildings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    @Column(name = "name")
    private String name;

    /**
     * 编码
     */
    @NotNull
    @ApiModelProperty(value = "编码", required = true)
    @Column(name = "long_code", nullable = false, unique = true)
    private String longCode;

    /**
     * 楼栋层数
     */
    @ApiModelProperty(value = "楼栋层数")
    @Column(name = "floor_count")
    private Integer floorCount;

    /**
     * 单元数
     */
    @ApiModelProperty(value = "单元数")
    @Column(name = "unites")
    private Integer unites;

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

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    @Column(name = "enable")
    private Boolean enable;

    @OneToMany(mappedBy = "buildings")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "visitors", "buildings", "wamoliUsers" }, allowSetters = true)
    private Set<RoomAddr> roomAddrs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "buildings", "community", "company" }, allowSetters = true)
    private HomelandStation homelandStation;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Buildings id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Buildings name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongCode() {
        return this.longCode;
    }

    public Buildings longCode(String longCode) {
        this.longCode = longCode;
        return this;
    }

    public void setLongCode(String longCode) {
        this.longCode = longCode;
    }

    public Integer getFloorCount() {
        return this.floorCount;
    }

    public Buildings floorCount(Integer floorCount) {
        this.floorCount = floorCount;
        return this;
    }

    public void setFloorCount(Integer floorCount) {
        this.floorCount = floorCount;
    }

    public Integer getUnites() {
        return this.unites;
    }

    public Buildings unites(Integer unites) {
        this.unites = unites;
        return this;
    }

    public void setUnites(Integer unites) {
        this.unites = unites;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public Buildings longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public Buildings latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public Buildings enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Set<RoomAddr> getRoomAddrs() {
        return this.roomAddrs;
    }

    public Buildings roomAddrs(Set<RoomAddr> roomAddrs) {
        this.setRoomAddrs(roomAddrs);
        return this;
    }

    public Buildings addRoomAddr(RoomAddr roomAddr) {
        this.roomAddrs.add(roomAddr);
        roomAddr.setBuildings(this);
        return this;
    }

    public Buildings removeRoomAddr(RoomAddr roomAddr) {
        this.roomAddrs.remove(roomAddr);
        roomAddr.setBuildings(null);
        return this;
    }

    public void setRoomAddrs(Set<RoomAddr> roomAddrs) {
        if (this.roomAddrs != null) {
            this.roomAddrs.forEach(i -> i.setBuildings(null));
        }
        if (roomAddrs != null) {
            roomAddrs.forEach(i -> i.setBuildings(this));
        }
        this.roomAddrs = roomAddrs;
    }

    public HomelandStation getHomelandStation() {
        return this.homelandStation;
    }

    public Buildings homelandStation(HomelandStation homelandStation) {
        this.setHomelandStation(homelandStation);
        return this;
    }

    public void setHomelandStation(HomelandStation homelandStation) {
        this.homelandStation = homelandStation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Buildings)) {
            return false;
        }
        return id != null && id.equals(((Buildings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Buildings{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", longCode='" + getLongCode() + "'" +
            ", floorCount=" + getFloorCount() +
            ", unites=" + getUnites() +
            ", longitude='" + getLongitude() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", enable='" + getEnable() + "'" +
            "}";
    }
}
