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
 * 门牌地址
 */
@ApiModel(description = "门牌地址")
@Entity
@Table(name = "wamoli_room_addr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RoomAddr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 门牌名称
     */
    @NotNull
    @ApiModelProperty(value = "门牌名称", required = true)
    @Column(name = "room_num", nullable = false)
    private String roomNum;

    /**
     * 单元
     */
    @ApiModelProperty(value = "单元")
    @Column(name = "unit")
    private String unit;

    /**
     * 户型
     */
    @ApiModelProperty(value = "户型")
    @Column(name = "room_type")
    private String roomType;

    /**
     * 面积
     */
    @ApiModelProperty(value = "面积")
    @Column(name = "room_area")
    private Float roomArea;

    /**
     * 是否入住
     */
    @ApiModelProperty(value = "是否入住")
    @Column(name = "used")
    private Boolean used;

    /**
     * 是否启用自动控制
     */
    @ApiModelProperty(value = "是否启用自动控制")
    @Column(name = "auto_control")
    private Boolean autoControl;

    /**
     * 编码
     */
    @NotNull
    @ApiModelProperty(value = "编码", required = true)
    @Column(name = "long_code", nullable = false, unique = true)
    private String longCode;

    @OneToMany(mappedBy = "roomAddr")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roomAddr" }, allowSetters = true)
    private Set<Visitor> visitors = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "roomAddrs", "homelandStation" }, allowSetters = true)
    private Buildings buildings;

    @ManyToMany(mappedBy = "roomAddrs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "roomAddrs", "companyDepts", "companyPosts" }, allowSetters = true)
    private Set<WamoliUser> wamoliUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomAddr id(Long id) {
        this.id = id;
        return this;
    }

    public String getRoomNum() {
        return this.roomNum;
    }

    public RoomAddr roomNum(String roomNum) {
        this.roomNum = roomNum;
        return this;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getUnit() {
        return this.unit;
    }

    public RoomAddr unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public RoomAddr roomType(String roomType) {
        this.roomType = roomType;
        return this;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Float getRoomArea() {
        return this.roomArea;
    }

    public RoomAddr roomArea(Float roomArea) {
        this.roomArea = roomArea;
        return this;
    }

    public void setRoomArea(Float roomArea) {
        this.roomArea = roomArea;
    }

    public Boolean getUsed() {
        return this.used;
    }

    public RoomAddr used(Boolean used) {
        this.used = used;
        return this;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Boolean getAutoControl() {
        return this.autoControl;
    }

    public RoomAddr autoControl(Boolean autoControl) {
        this.autoControl = autoControl;
        return this;
    }

    public void setAutoControl(Boolean autoControl) {
        this.autoControl = autoControl;
    }

    public String getLongCode() {
        return this.longCode;
    }

    public RoomAddr longCode(String longCode) {
        this.longCode = longCode;
        return this;
    }

    public void setLongCode(String longCode) {
        this.longCode = longCode;
    }

    public Set<Visitor> getVisitors() {
        return this.visitors;
    }

    public RoomAddr visitors(Set<Visitor> visitors) {
        this.setVisitors(visitors);
        return this;
    }

    public RoomAddr addVisitor(Visitor visitor) {
        this.visitors.add(visitor);
        visitor.setRoomAddr(this);
        return this;
    }

    public RoomAddr removeVisitor(Visitor visitor) {
        this.visitors.remove(visitor);
        visitor.setRoomAddr(null);
        return this;
    }

    public void setVisitors(Set<Visitor> visitors) {
        if (this.visitors != null) {
            this.visitors.forEach(i -> i.setRoomAddr(null));
        }
        if (visitors != null) {
            visitors.forEach(i -> i.setRoomAddr(this));
        }
        this.visitors = visitors;
    }

    public Buildings getBuildings() {
        return this.buildings;
    }

    public RoomAddr buildings(Buildings buildings) {
        this.setBuildings(buildings);
        return this;
    }

    public void setBuildings(Buildings buildings) {
        this.buildings = buildings;
    }

    public Set<WamoliUser> getWamoliUsers() {
        return this.wamoliUsers;
    }

    public RoomAddr wamoliUsers(Set<WamoliUser> wamoliUsers) {
        this.setWamoliUsers(wamoliUsers);
        return this;
    }

    public RoomAddr addWamoliUser(WamoliUser wamoliUser) {
        this.wamoliUsers.add(wamoliUser);
        wamoliUser.getRoomAddrs().add(this);
        return this;
    }

    public RoomAddr removeWamoliUser(WamoliUser wamoliUser) {
        this.wamoliUsers.remove(wamoliUser);
        wamoliUser.getRoomAddrs().remove(this);
        return this;
    }

    public void setWamoliUsers(Set<WamoliUser> wamoliUsers) {
        if (this.wamoliUsers != null) {
            this.wamoliUsers.forEach(i -> i.removeRoomAddr(this));
        }
        if (wamoliUsers != null) {
            wamoliUsers.forEach(i -> i.addRoomAddr(this));
        }
        this.wamoliUsers = wamoliUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomAddr)) {
            return false;
        }
        return id != null && id.equals(((RoomAddr) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomAddr{" +
            "id=" + getId() +
            ", roomNum='" + getRoomNum() + "'" +
            ", unit='" + getUnit() + "'" +
            ", roomType='" + getRoomType() + "'" +
            ", roomArea=" + getRoomArea() +
            ", used='" + getUsed() + "'" +
            ", autoControl='" + getAutoControl() + "'" +
            ", longCode='" + getLongCode() + "'" +
            "}";
    }
}
