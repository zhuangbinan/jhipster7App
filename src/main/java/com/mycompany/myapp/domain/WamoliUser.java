package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CertificateType;
import com.mycompany.myapp.domain.enumeration.UserType;
import io.swagger.annotations.ApiModel;
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
 * 用户信息表
 */
@ApiModel(description = "用户信息表")
@Entity
@Table(name = "wamoli_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WamoliUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户姓名
     */
    @NotNull
    @ApiModelProperty(value = "用户姓名", required = true)
    @Column(name = "user_name", nullable = false)
    private String userName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    @Column(name = "gender")
    private String gender;

    /**
     * 电话
     */
    @NotNull
    @ApiModelProperty(value = "电话", required = true)
    @Column(name = "phone_num", nullable = false)
    private String phoneNum;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Column(name = "email")
    private String email;

    /**
     * 单元
     */
    @ApiModelProperty(value = "单元")
    @Column(name = "unit_addr")
    private String unitAddr;

    /**
     * 门牌
     */
    @ApiModelProperty(value = "门牌")
    @Column(name = "room_addr")
    private Long roomAddr;

    /**
     * 证件号码
     */
    @ApiModelProperty(value = "证件号码")
    @Column(name = "id_card_num")
    private String idCardNum;

    /**
     * 证件类型
     */
    @ApiModelProperty(value = "证件类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "id_card_type")
    private CertificateType idCardType;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    /**
     * 是否生效
     */
    @ApiModelProperty(value = "是否生效")
    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "is_manager")
    private Boolean isManager;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_wamoli_user__room_addr",
        joinColumns = @JoinColumn(name = "wamoli_user_id"),
        inverseJoinColumns = @JoinColumn(name = "room_addr_id")
    )
    @JsonIgnoreProperties(value = { "visitors", "buildings", "wamoliUsers" }, allowSetters = true)
    private Set<RoomAddr> roomAddrs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WamoliUser id(Long id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return this.userName;
    }

    public WamoliUser userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return this.gender;
    }

    public WamoliUser gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public WamoliUser phoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return this.email;
    }

    public WamoliUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnitAddr() {
        return this.unitAddr;
    }

    public WamoliUser unitAddr(String unitAddr) {
        this.unitAddr = unitAddr;
        return this;
    }

    public void setUnitAddr(String unitAddr) {
        this.unitAddr = unitAddr;
    }

    public Long getRoomAddr() {
        return this.roomAddr;
    }

    public WamoliUser roomAddr(Long roomAddr) {
        this.roomAddr = roomAddr;
        return this;
    }

    public void setRoomAddr(Long roomAddr) {
        this.roomAddr = roomAddr;
    }

    public String getIdCardNum() {
        return this.idCardNum;
    }

    public WamoliUser idCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
        return this;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public CertificateType getIdCardType() {
        return this.idCardType;
    }

    public WamoliUser idCardType(CertificateType idCardType) {
        this.idCardType = idCardType;
        return this;
    }

    public void setIdCardType(CertificateType idCardType) {
        this.idCardType = idCardType;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public WamoliUser userType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public WamoliUser enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getIsManager() {
        return this.isManager;
    }

    public WamoliUser isManager(Boolean isManager) {
        this.isManager = isManager;
        return this;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public WamoliUser lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public WamoliUser lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getUser() {
        return this.user;
    }

    public WamoliUser user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<RoomAddr> getRoomAddrs() {
        return this.roomAddrs;
    }

    public WamoliUser roomAddrs(Set<RoomAddr> roomAddrs) {
        this.setRoomAddrs(roomAddrs);
        return this;
    }

    public WamoliUser addRoomAddr(RoomAddr roomAddr) {
        this.roomAddrs.add(roomAddr);
        roomAddr.getWamoliUsers().add(this);
        return this;
    }

    public WamoliUser removeRoomAddr(RoomAddr roomAddr) {
        this.roomAddrs.remove(roomAddr);
        roomAddr.getWamoliUsers().remove(this);
        return this;
    }

    public void setRoomAddrs(Set<RoomAddr> roomAddrs) {
        this.roomAddrs = roomAddrs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WamoliUser)) {
            return false;
        }
        return id != null && id.equals(((WamoliUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WamoliUser{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", gender='" + getGender() + "'" +
            ", phoneNum='" + getPhoneNum() + "'" +
            ", email='" + getEmail() + "'" +
            ", unitAddr='" + getUnitAddr() + "'" +
            ", roomAddr=" + getRoomAddr() +
            ", idCardNum='" + getIdCardNum() + "'" +
            ", idCardType='" + getIdCardType() + "'" +
            ", userType='" + getUserType() + "'" +
            ", enable='" + getEnable() + "'" +
            ", isManager='" + getIsManager() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
