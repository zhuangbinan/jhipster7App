package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TuYaDevice.
 */
@Entity
@Table(name = "tu_ya_device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TuYaDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    @Column(name = "device_name")
    private String deviceName;

    /**
     * 设备编号longCode
     */

    @ApiModelProperty(value = "设备编号longCode")
    @Column(name = "long_code", unique = true)
    private Long longCode;

    /**
     * tuya设备ID
     */

    @ApiModelProperty(value = "tuya设备ID")
    @Column(name = "ty_device_id", unique = true)
    private String tyDeviceId;

    /**
     * 设备类型 0-网关 1-通断启 2-开关 3-温湿度燃气 4-报警器
     */
    @ApiModelProperty(value = "设备类型 0-网关 1-通断启 2-开关 3-温湿度燃气 4-报警器")
    @Column(name = "device_type")
    private Integer deviceType;

    /**
     * 命令编码
     */
    @ApiModelProperty(value = "命令编码")
    @Column(name = "cmd_code")
    private String cmdCode;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    private Instant createTime;

    /**
     * 是否有效
     */
    @ApiModelProperty(value = "是否有效")
    @Column(name = "enable")
    private Boolean enable;

    @OneToMany(mappedBy = "tuYaDevice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tuYaDevice" }, allowSetters = true)
    private Set<TuYaCmd> tuYaCmds = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "visitors", "buildings", "wamoliUsers" }, allowSetters = true)
    private RoomAddr roomAddr;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TuYaDevice id(Long id) {
        this.id = id;
        return this;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public TuYaDevice deviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Long getLongCode() {
        return this.longCode;
    }

    public TuYaDevice longCode(Long longCode) {
        this.longCode = longCode;
        return this;
    }

    public void setLongCode(Long longCode) {
        this.longCode = longCode;
    }

    public String getTyDeviceId() {
        return this.tyDeviceId;
    }

    public TuYaDevice tyDeviceId(String tyDeviceId) {
        this.tyDeviceId = tyDeviceId;
        return this;
    }

    public void setTyDeviceId(String tyDeviceId) {
        this.tyDeviceId = tyDeviceId;
    }

    public Integer getDeviceType() {
        return this.deviceType;
    }

    public TuYaDevice deviceType(Integer deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getCmdCode() {
        return this.cmdCode;
    }

    public TuYaDevice cmdCode(String cmdCode) {
        this.cmdCode = cmdCode;
        return this;
    }

    public void setCmdCode(String cmdCode) {
        this.cmdCode = cmdCode;
    }

    public Instant getCreateTime() {
        return this.createTime;
    }

    public TuYaDevice createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public TuYaDevice enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Set<TuYaCmd> getTuYaCmds() {
        return this.tuYaCmds;
    }

    public TuYaDevice tuYaCmds(Set<TuYaCmd> tuYaCmds) {
        this.setTuYaCmds(tuYaCmds);
        return this;
    }

    public TuYaDevice addTuYaCmd(TuYaCmd tuYaCmd) {
        this.tuYaCmds.add(tuYaCmd);
        tuYaCmd.setTuYaDevice(this);
        return this;
    }

    public TuYaDevice removeTuYaCmd(TuYaCmd tuYaCmd) {
        this.tuYaCmds.remove(tuYaCmd);
        tuYaCmd.setTuYaDevice(null);
        return this;
    }

    public void setTuYaCmds(Set<TuYaCmd> tuYaCmds) {
        if (this.tuYaCmds != null) {
            this.tuYaCmds.forEach(i -> i.setTuYaDevice(null));
        }
        if (tuYaCmds != null) {
            tuYaCmds.forEach(i -> i.setTuYaDevice(this));
        }
        this.tuYaCmds = tuYaCmds;
    }

    public RoomAddr getRoomAddr() {
        return this.roomAddr;
    }

    public TuYaDevice roomAddr(RoomAddr roomAddr) {
        this.setRoomAddr(roomAddr);
        return this;
    }

    public void setRoomAddr(RoomAddr roomAddr) {
        this.roomAddr = roomAddr;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TuYaDevice)) {
            return false;
        }
        return id != null && id.equals(((TuYaDevice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TuYaDevice{" +
            "id=" + getId() +
            ", deviceName='" + getDeviceName() + "'" +
            ", longCode=" + getLongCode() +
            ", tyDeviceId='" + getTyDeviceId() + "'" +
            ", deviceType=" + getDeviceType() +
            ", cmdCode='" + getCmdCode() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", enable='" + getEnable() + "'" +
            "}";
    }
}
