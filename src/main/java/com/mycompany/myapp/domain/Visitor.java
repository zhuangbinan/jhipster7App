package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 访客信息
 */
@ApiModel(description = "访客信息")
@Entity
@Table(name = "wamoli_visitor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Visitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 姓名
     */
    @NotNull
    @ApiModelProperty(value = "姓名", required = true)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 电话
     */
    @NotNull
    @ApiModelProperty(value = "电话", required = true)
    @Column(name = "phone_num", nullable = false)
    private String phoneNum;

    /**
     * 车牌号码
     */
    @ApiModelProperty(value = "车牌号码")
    @Column(name = "car_plate_num")
    private String carPlateNum;

    /**
     * 生效时间
     */
    @NotNull
    @ApiModelProperty(value = "生效时间", required = true)
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    /**
     * 失效时间
     */
    @NotNull
    @ApiModelProperty(value = "失效时间", required = true)
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    /**
     * 密码密钥
     */
    @ApiModelProperty(value = "密码密钥")
    @Column(name = "passwd")
    private String passwd;

    /**
     * 人脸
     */

    @ApiModelProperty(value = "人脸")
    @Lob
    @Column(name = "face_image")
    private byte[] faceImage;

    @Column(name = "face_image_content_type")
    private String faceImageContentType;

    /**
     * 远程开启选择的门,进小区或楼栋入口
     */
    @ApiModelProperty(value = "远程开启选择的门,进小区或楼栋入口")
    @Column(name = "which_entrance")
    private String whichEntrance;

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

    public Visitor id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Visitor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public Visitor phoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCarPlateNum() {
        return this.carPlateNum;
    }

    public Visitor carPlateNum(String carPlateNum) {
        this.carPlateNum = carPlateNum;
        return this;
    }

    public void setCarPlateNum(String carPlateNum) {
        this.carPlateNum = carPlateNum;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Visitor startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Visitor endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public Visitor passwd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public byte[] getFaceImage() {
        return this.faceImage;
    }

    public Visitor faceImage(byte[] faceImage) {
        this.faceImage = faceImage;
        return this;
    }

    public void setFaceImage(byte[] faceImage) {
        this.faceImage = faceImage;
    }

    public String getFaceImageContentType() {
        return this.faceImageContentType;
    }

    public Visitor faceImageContentType(String faceImageContentType) {
        this.faceImageContentType = faceImageContentType;
        return this;
    }

    public void setFaceImageContentType(String faceImageContentType) {
        this.faceImageContentType = faceImageContentType;
    }

    public String getWhichEntrance() {
        return this.whichEntrance;
    }

    public Visitor whichEntrance(String whichEntrance) {
        this.whichEntrance = whichEntrance;
        return this;
    }

    public void setWhichEntrance(String whichEntrance) {
        this.whichEntrance = whichEntrance;
    }

    public RoomAddr getRoomAddr() {
        return this.roomAddr;
    }

    public Visitor roomAddr(RoomAddr roomAddr) {
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
        if (!(o instanceof Visitor)) {
            return false;
        }
        return id != null && id.equals(((Visitor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Visitor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phoneNum='" + getPhoneNum() + "'" +
            ", carPlateNum='" + getCarPlateNum() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", passwd='" + getPasswd() + "'" +
            ", faceImage='" + getFaceImage() + "'" +
            ", faceImageContentType='" + getFaceImageContentType() + "'" +
            ", whichEntrance='" + getWhichEntrance() + "'" +
            "}";
    }
}
