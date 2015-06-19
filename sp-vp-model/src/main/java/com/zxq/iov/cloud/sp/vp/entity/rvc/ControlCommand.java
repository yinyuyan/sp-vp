package com.zxq.iov.cloud.sp.vp.entity.rvc;

import com.zxq.iov.cloud.core.dal.entity.MyBaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 安防服务 控制命令类
 * @author 叶荣杰
 * create date 2015-6-17 11:24
 * modify date 2015-6-18 9:50
 * @version 0.2, 2015-6-18
 */
@Entity()
@Table(name = "TB_CONTROL_COMMAND")
public class ControlCommand extends MyBaseEntity<Long> implements Serializable {

    private static final String SEQ_NAME = "seq_tb_control_command";

    @Id
    @Column(name = "ID", nullable = false, updatable = false, length=20)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_NAME)
    @TableGenerator(name = SEQ_NAME, table = MY_SEQ_TABLE, allocationSize = 1,
            pkColumnName = "pk_name", valueColumnName = "current_value", pkColumnValue = SEQ_NAME)
    private Long id;

    @Column(name = "TBOX_ID", nullable = false, precision = 20, scale = 0)
    private Long tboxId;

    @Column(name = "EVENT_ID", precision = 20, scale = 0)
    private Long eventId;

    @Column(name = "NAME", length = 50)
    private String name;

    @Column(name = "CODE", nullable = false, precision = 4, scale = 0)
    private Integer code;

    @Column(name = "PARAMETER", length = 255)
    private String parameter;

    @Column(name = "IS_CANCEL", nullable = false, precision = 1, scale = 0)
    private Boolean isCancel;

    @Column(name = "FAILURE_TYPE", precision = 6, scale = 0)
    private Integer failureType;

    public ControlCommand(){}

    public ControlCommand(Long tboxId, String name, Integer code, String parameter) {
        this.tboxId = tboxId;
        this.name = name;
        this.code = code;
        this.parameter = parameter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTboxId() {
        return tboxId;
    }

    public void setTboxId(Long tboxId) {
        this.tboxId = tboxId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Boolean isCancel() {
        return isCancel;
    }

    public void setIsCancel(Boolean isCancel) {
        this.isCancel = isCancel;
    }

    public Integer getFailureType() {
        return failureType;
    }

    public void setFailureType(Integer failureType) {
        this.failureType = failureType;
    }
}