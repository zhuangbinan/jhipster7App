package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CmdType;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TuYaCmd.
 */
@Entity
@Table(name = "tu_ya_cmd")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TuYaCmd implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @Column(name = "cmd_name")
    private String cmdName;

    /**
     * 命令code
     */
    @ApiModelProperty(value = "命令code")
    @Column(name = "cmd_code")
    private String cmdCode;

    /**
     * 值
     */
    @ApiModelProperty(value = "值")
    @Column(name = "value")
    private Boolean value;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    @Enumerated(EnumType.STRING)
    @Column(name = "cmd_type")
    private CmdType cmdType;

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "tuYaCmds", "roomAddr" }, allowSetters = true)
    private TuYaDevice tuYaDevice;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TuYaCmd id(Long id) {
        this.id = id;
        return this;
    }

    public String getCmdName() {
        return this.cmdName;
    }

    public TuYaCmd cmdName(String cmdName) {
        this.cmdName = cmdName;
        return this;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public String getCmdCode() {
        return this.cmdCode;
    }

    public TuYaCmd cmdCode(String cmdCode) {
        this.cmdCode = cmdCode;
        return this;
    }

    public void setCmdCode(String cmdCode) {
        this.cmdCode = cmdCode;
    }

    public Boolean getValue() {
        return this.value;
    }

    public TuYaCmd value(Boolean value) {
        this.value = value;
        return this;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public CmdType getCmdType() {
        return this.cmdType;
    }

    public TuYaCmd cmdType(CmdType cmdType) {
        this.cmdType = cmdType;
        return this;
    }

    public void setCmdType(CmdType cmdType) {
        this.cmdType = cmdType;
    }

    public Instant getCreateTime() {
        return this.createTime;
    }

    public TuYaCmd createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public TuYaCmd enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public TuYaDevice getTuYaDevice() {
        return this.tuYaDevice;
    }

    public TuYaCmd tuYaDevice(TuYaDevice tuYaDevice) {
        this.setTuYaDevice(tuYaDevice);
        return this;
    }

    public void setTuYaDevice(TuYaDevice tuYaDevice) {
        this.tuYaDevice = tuYaDevice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TuYaCmd)) {
            return false;
        }
        return id != null && id.equals(((TuYaCmd) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TuYaCmd{" +
            "id=" + getId() +
            ", cmdName='" + getCmdName() + "'" +
            ", cmdCode='" + getCmdCode() + "'" +
            ", value='" + getValue() + "'" +
            ", cmdType='" + getCmdType() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", enable='" + getEnable() + "'" +
            "}";
    }
}
