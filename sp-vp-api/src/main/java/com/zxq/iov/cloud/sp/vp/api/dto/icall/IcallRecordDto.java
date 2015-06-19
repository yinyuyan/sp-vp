package com.zxq.iov.cloud.sp.vp.api.dto.icall;

import com.zxq.iov.cloud.sp.vp.api.dto.OtaDto;

import java.util.Date;

/**
 * 安防 iCall通话传输对象
 *
 * @author 叶荣杰
 * create date 2015-6-12 15:17
 * modify date
 * @version 0.1, 2015-6-12
 */
public class IcallRecordDto extends OtaDto {

    private Long id;
    private Long callId;
    private String callNumber;
    private Date callTime;
    private Date hangUpTime;
    private String errorCode;

    public IcallRecordDto() {}

    public IcallRecordDto(Long id, Long callId, String callNumber, Date callTime, Date hangUpTime, String errorCode) {
        this.id = id;
        this.callId = callId;
        this.callNumber = callNumber;
        this.callTime = callTime;
        this.hangUpTime = hangUpTime;
        this.errorCode = errorCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public Date getCallTime() {
        return callTime;
    }

    public void setCallTime(Date callTime) {
        this.callTime = callTime;
    }

    public Date getHangUpTime() {
        return hangUpTime;
    }

    public void setHangUpTime(Date hangUpTime) {
        this.hangUpTime = hangUpTime;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}