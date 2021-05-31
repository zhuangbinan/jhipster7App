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
 * 用户位置表\n@author yifeng
 */
@ApiModel(description = "用户位置表\n@author yifeng")
@Entity
@Table(name = "wamoli_user_location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WamoliUserLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户位置状态
     */
    @NotNull
    @ApiModelProperty(value = "用户位置状态", required = true)
    @Column(name = "state", nullable = false)
    private Integer state;

    /**
     * 用户唯一标识
     */
    @NotNull
    @ApiModelProperty(value = "用户唯一标识", required = true)
    @Column(name = "card_num", nullable = false)
    private String cardNum;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @Column(name = "expire_time")
    private Instant expireTime;

    /**
     * 延期时间，单位为分钟
     */
    @ApiModelProperty(value = "延期时间，单位为分钟")
    @Column(name = "delay_time")
    private Integer delayTime;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @JsonIgnoreProperties(value = { "user", "roomAddrs", "companyDepts" }, allowSetters = true)
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

    public WamoliUserLocation id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getState() {
        return this.state;
    }

    public WamoliUserLocation state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCardNum() {
        return this.cardNum;
    }

    public WamoliUserLocation cardNum(String cardNum) {
        this.cardNum = cardNum;
        return this;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Instant getExpireTime() {
        return this.expireTime;
    }

    public WamoliUserLocation expireTime(Instant expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public void setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getDelayTime() {
        return this.delayTime;
    }

    public WamoliUserLocation delayTime(Integer delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public WamoliUserLocation lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public WamoliUserLocation lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public WamoliUser getWamoliUser() {
        return this.wamoliUser;
    }

    public WamoliUserLocation wamoliUser(WamoliUser wamoliUser) {
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
        if (!(o instanceof WamoliUserLocation)) {
            return false;
        }
        return id != null && id.equals(((WamoliUserLocation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WamoliUserLocation{" +
            "id=" + getId() +
            ", state=" + getState() +
            ", cardNum='" + getCardNum() + "'" +
            ", expireTime='" + getExpireTime() + "'" +
            ", delayTime=" + getDelayTime() +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
