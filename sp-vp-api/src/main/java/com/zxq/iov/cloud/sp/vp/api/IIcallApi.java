/*
 * Licensed to SAICMotor,Inc. under the terms of the SAICMotor
 * Software License version 1.0.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * ----------------------------------------------------------------------------
 * Date             Author      Version        Comments
 * 2015-06-12       荣杰         1.0            Initial Version
 * 2015-07-24       荣杰         1.1
 *
 * com.zxq.iov.cloud.sp.vp.api.IIcallApi
 *
 * sp - sp-vp-api
 */

package com.zxq.iov.cloud.sp.vp.api;

import com.saicmotor.telematics.framework.core.exception.ApiException;
import com.zxq.iov.cloud.sp.vp.api.dto.OtaDto;
import com.zxq.iov.cloud.sp.vp.api.dto.icall.IcallRecordDto;
import com.zxq.iov.cloud.sp.vp.api.dto.status.VehiclePosDto;

import java.util.List;

/**
 * 安防服务 iCall API
 */
public interface IIcallApi {

	/**
	 * 开始iCall
	 *
	 * @param otaDto               OTA传输对象
	 * @param vehiclePosDtos       车辆GPS位置传输对象列表
	 * @param icallType            呼叫方式
	 * @param tboxBatteryStatus    TBOX电池状态
	 * @param vehicleBatteryStatus 车辆电池状态
	 * @return bCall通话传输对象
	 */
	IcallRecordDto startIcall(OtaDto otaDto, List<VehiclePosDto> vehiclePosDtos, Integer icallType,
			Integer tboxBatteryStatus, Integer vehicleBatteryStatus) throws ApiException;

	/**
	 * 请求iCall状态
	 *
	 * @param vin 车辆唯一码
	 */
	void requestIcallStatus(String vin) throws ApiException;

	/**
	 * 更新iCall状态
	 *
	 * @param otaDto               OTA传输对象
	 * @param vehiclePosDtos       车辆GPS位置传输对象列表
	 * @param icallType            呼叫方式
	 * @param tboxBatteryStatus    TBOX电池状态
	 * @param vehicleBatteryStatus 车辆电池状态
	 * @return 呼叫对象ID
	 */
	Long updateIcall(OtaDto otaDto, List<VehiclePosDto> vehiclePosDtos, Integer icallType, Integer tboxBatteryStatus,
			Integer vehicleBatteryStatus) throws ApiException;

	/**
	 * 请求挂断通话
	 *
	 * @param vin 车辆唯一码
	 */
	void requestHangUp(String vin) throws ApiException;

	/**
	 * 请求车辆回拨
	 *
	 * @param vin        车辆唯一码
	 * @param callNumber 呼叫号码
	 */
	void requestCallBack(String vin, String callNumber) throws ApiException;

	/**
	 * 响应车辆回拨请求
	 *
	 * @param otaDto           OTA传输对象
	 * @param callbackAccepted 是否接受回拨
	 * @param rejectReason     拒绝理由
	 */
	void responseCallBack(OtaDto otaDto, Boolean callbackAccepted, Integer rejectReason) throws ApiException;

	/**
	 * 请求结束iCall
	 *
	 * @param vin 车辆唯一码
	 */
	void requestCloseIcall(String vin) throws ApiException;

	/**
	 * 结束iCall
	 *
	 * @param otaDto OTA传输对象
	 */
	void closeIcall(OtaDto otaDto) throws ApiException;
}
