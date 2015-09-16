/*
 * Licensed to SAICMotor,Inc. under the terms of the SAICMotor
 * Software License version 1.0.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * ----------------------------------------------------------------------------
 * Date             Author      Version        Comments
 * 2015-06-17       荣杰         1.0            Initial Version
 * 2015-08-11       荣杰         1.1
 *
 * com.zxq.iov.cloud.sp.vp.api.impl.RvcApiImpl
 *
 * sp - sp-vp-api-impl
 */

package com.zxq.iov.cloud.sp.vp.api.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.saicmotor.telematics.framework.core.exception.ServLayerException;
import com.saicmotor.telematics.framework.core.logger.Logger;
import com.saicmotor.telematics.framework.core.logger.LoggerFactory;
import com.zxq.iov.cloud.sp.vp.api.IRvcApi;
import com.zxq.iov.cloud.sp.vp.api.dto.OtaDto;
import com.zxq.iov.cloud.sp.vp.api.dto.rvc.RvcDto;
import com.zxq.iov.cloud.sp.vp.api.dto.rvc.RvcStatusDto;
import com.zxq.iov.cloud.sp.vp.api.dto.status.VehiclePosDto;
import com.zxq.iov.cloud.sp.vp.api.dto.status.VehicleStatusDto;
import com.zxq.iov.cloud.sp.vp.api.impl.assembler.EventAssembler;
import com.zxq.iov.cloud.sp.vp.api.impl.assembler.rvc.RvcStatusDtoAssembler;
import com.zxq.iov.cloud.sp.vp.api.impl.assembler.status.VehiclePosDtoAssembler;
import com.zxq.iov.cloud.sp.vp.api.impl.assembler.status.VehicleStatusDtoAssembler;
import com.zxq.iov.cloud.sp.vp.common.util.BinaryAndHexUtil;
import com.zxq.iov.cloud.sp.vp.common.constants.Constants;
import com.zxq.iov.cloud.sp.vp.common.constants.ExceptionConstants;
import com.zxq.iov.cloud.sp.vp.service.IEventService;
import com.zxq.iov.cloud.sp.vp.service.IRvcService;
import com.zxq.iov.cloud.sp.vp.service.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 安防服务 远程控制API实现类
 */
@Service
public class RvcApiImpl extends BaseApi implements IRvcApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(RvcApiImpl.class);

	@Autowired
	private IRvcService rvcService;
	@Autowired
	private IEventService eventService;

	@Override
	public Long requestControl(String requestClient, Long userId, String vin, String command,
			Map<String, Object> parameters) throws ServLayerException {
		AssertRequired("userId,vin,command", userId, vin, command);
		OtaDto otaDto = new OtaDto(getTboxByVin(vin).getTboxId(), vin, Constants.AID_RVC, 1);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("command", Constants.RVC_CMD_CODE.get(command));
		paramMap.put("cancelFlag", 0);
		Map<Integer, byte[]> tboxConfig = null;
		if (null != parameters) {
			tboxConfig = convertConfig2Tbox(parameters);
			if (command.equals(Constants.RVC_CMD_VEHICLE_UNLOCK)) {
				paramMap.put("unlockSilentFlag", parameters.get("silent_flag"));
			}
		}
		Event event = new EventAssembler().fromOtaDto(otaDto);
		event.setParamMap(paramMap);
		eventService.start(event);
		Long controlCommandId;
		if (!event.isRetry()) {
			controlCommandId = rvcService
					.requestControl(requestClient, userId, getTboxByVin(vin), command, parameters, event.getId())
					.getId();
			event.setResult(controlCommandId);
			eventService.end(event);
		} else {
			controlCommandId = Long.parseLong(event.getResult().toString());
		}
		otaDto.setEventId(event.getId());
		sendQueue(otaDto,
				new RvcDto(BinaryAndHexUtil.hexStringToByte(Constants.RVC_CMD_CODE.get(command)), tboxConfig));
		return controlCommandId;
	}

	@Override
	public void cancelControl(String requestClient, Long userId, String vin, String command) throws ServLayerException {
		AssertRequired("userId,vin,command", userId, vin, command);
		OtaDto otaDto = new OtaDto(getTboxByVin(vin).getTboxId(), vin, Constants.AID_RVC, 1);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("command", Constants.RVC_CMD_CODE.get(command));
		paramMap.put("cancelFlag", 1);
		Event event = new EventAssembler().fromOtaDto(otaDto);
		event.setParamMap(paramMap);
		eventService.start(event);
		if (!event.isRetry()) {
			rvcService.cancelControl(requestClient, userId, getTboxByVin(vin), command);
			eventService.end(event);
		}
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("cancel", true);
		otaDto.setEventId(event.getId());
		sendQueue(otaDto, new RvcDto(BinaryAndHexUtil.hexStringToByte(Constants.RVC_CMD_CODE.get(command)),
				convertConfig2Tbox(parameter)));
	}

	@Override
	public void updateControlStatus(OtaDto otaDto, byte[] rvcStatus, Integer failureType, VehiclePosDto vehiclePosDto,
			List<VehicleStatusDto> vehicleStatusDtos) throws ServLayerException {
		AssertRequired("otaDto,rvcStatus", otaDto, rvcStatus);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("status", rvcStatus);
		Event event = new EventAssembler().fromOtaDto(otaDto);
		eventService.start(event);
		if (!event.isRetry()) {
			rvcService.updateControlStatus(getTboxById(otaDto.getTboxId()), rvcStatus, failureType,
					new VehiclePosDtoAssembler().fromDto(vehiclePosDto),
					new VehicleStatusDtoAssembler().fromDtoList(vehicleStatusDtos), event.getId());
			eventService.end(event);
		}

	}

	@Override
	public RvcStatusDto getControlStatus(Long controlCommandId, String vin, Long userId) throws ServLayerException {
		AssertRequired("controlCommandId,vin", controlCommandId, vin);
		return new RvcStatusDtoAssembler()
				.toDto(rvcService.getControlStatus(controlCommandId, userId, getTboxByVin(vin)));
	}

	/**
	 * 将请求的命令配置参数转换成TBOX能识别的参数
	 *
	 * @return
	 */
	private Map<Integer, byte[]> convertConfig2Tbox(Map<String, Object> parameters) throws ServLayerException {
		Map<Integer, byte[]> result = new HashMap<>();
		Iterator iterator = parameters.keySet().iterator();
		String key, val = null;
		Integer keyId;
		while (iterator.hasNext()) {
			key = iterator.next().toString();
			keyId = Constants.RVC_CMD_PARAM_ID.get(key);
			val = Constants.RVC_CMD_PARAM_VALUE.get(key + "_" + parameters.get(key));
			if (StringUtils.isBlank(val)) {
				throw new ServLayerException(ExceptionConstants.WRONG_CONTROL_CMD_PARAM);
			}
			result.put(keyId, BinaryAndHexUtil.hexStringToByte(val));
		}
		return result;
	}

}