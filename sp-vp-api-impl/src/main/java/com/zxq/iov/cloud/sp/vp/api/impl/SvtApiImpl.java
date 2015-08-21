package com.zxq.iov.cloud.sp.vp.api.impl;

import com.saicmotor.telematics.framework.core.exception.ServLayerException;
import com.zxq.iov.cloud.sp.vp.api.ISvtApi;
import com.zxq.iov.cloud.sp.vp.api.dto.OtaDto;
import com.zxq.iov.cloud.sp.vp.api.dto.status.VehicleStatusDto;
import com.zxq.iov.cloud.sp.vp.api.dto.svt.*;
import com.zxq.iov.cloud.sp.vp.api.impl.assembler.EventAssembler;
import com.zxq.iov.cloud.sp.vp.api.impl.assembler.status.VehiclePosDtoAssembler;
import com.zxq.iov.cloud.sp.vp.api.impl.assembler.status.VehicleStatusDtoAssembler;
import com.zxq.iov.cloud.sp.vp.api.impl.assembler.svt.StolenAlarmDtoAssembler;
import com.zxq.iov.cloud.sp.vp.common.Constants;
import com.zxq.iov.cloud.sp.vp.service.IEventService;
import com.zxq.iov.cloud.sp.vp.service.ISvtService;
import com.zxq.iov.cloud.sp.vp.service.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 安防 被盗追踪服务实现类
 *
 * @author 叶荣杰
 * create date 2015-6-15 13:03
 * modify date 2015-8-11 10:40
 * @version 0.9, 2015-8-11
 */
@Service
public class SvtApiImpl extends BaseApi implements ISvtApi {

    @Autowired
    private ISvtService svtService;
    @Autowired
    private IEventService eventService;

    @Override
    public void alarm(OtaDto otaDto, List<StolenAlarmDto> stolenAlarmDtos) throws ServLayerException {
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            StolenAlarmDtoAssembler stolenAlarmDtoAssembler = new StolenAlarmDtoAssembler();
            VehiclePosDtoAssembler posDtoAssembler = new VehiclePosDtoAssembler();
            for(StolenAlarmDto stolenAlarmDto : stolenAlarmDtos) {
                svtService.alarm(otaDto.getTboxId(), stolenAlarmDtoAssembler.fromDto(stolenAlarmDto),
                        posDtoAssembler.fromDto(stolenAlarmDto.getVehiclePosDto()), event.getId());
            }
            eventService.end(event);
        }
    }

    @Override
    public void updateTrack(OtaDto otaDto, List<TrackDto> trackDtos) throws ServLayerException {
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            List<VehicleStatusDto> vehicleStatusDtos = new ArrayList<>();
            VehicleStatusDtoAssembler statusDtoAssembler = new VehicleStatusDtoAssembler();
            VehiclePosDtoAssembler posDtoAssembler = new VehiclePosDtoAssembler();
            for(TrackDto trackDto : trackDtos) {
                vehicleStatusDtos.add(new VehicleStatusDto("gnssSpeed", trackDto.getGnssSpeed()));
                vehicleStatusDtos.add(new VehicleStatusDto("gsmAntConnected", trackDto.isGsmAntConnected()?1:0));
                vehicleStatusDtos.add(new VehicleStatusDto("gnssAntConnected", trackDto.isGnssAntConnected()?1:0));
                vehicleStatusDtos.add(new VehicleStatusDto("vehicleBatteryConnected", trackDto.isVehicleBatteryConnected()?1:0));
                vehicleStatusDtos.add(new VehicleStatusDto("intBattV", trackDto.getIntBattV()));
                vehicleStatusDtos.add(new VehicleStatusDto("vehicleAlarmStatus", trackDto.getVehicleAlarmStatus()));
                vehicleStatusDtos.add(new VehicleStatusDto("engineStatus", trackDto.getEngineStatus()));
                vehicleStatusDtos.add(new VehicleStatusDto("powerMode", trackDto.getPowerMode()));
                vehicleStatusDtos.add(new VehicleStatusDto("lastKeySeen", trackDto.getLastKeySeen()));
                vehicleStatusDtos.add(new VehicleStatusDto("fuelLevelPrc", trackDto.getFuelLevelPrc()));
                vehicleStatusDtos.add(new VehicleStatusDto("fuelRange", trackDto.getFuelRange()));
                vehicleStatusDtos.add(new VehicleStatusDto("canBusActive", trackDto.isCanBusActive()?1:0));
                vehicleStatusDtos.add(new VehicleStatusDto("lastCanBusActiveityTime", (int)(trackDto.getLastCanBusActivityTime().getTime()/1000)));
                vehicleStatusDtos.add(new VehicleStatusDto("ttnTrackPoint", trackDto.getTtnTrackPoint()));
                svtService.updateTrack(otaDto.getTboxId(), statusDtoAssembler.fromDtoList(vehicleStatusDtos),
                        posDtoAssembler.fromDto(trackDto.getVehiclePosDto()), event.getId());
                vehicleStatusDtos.clear();
            }
            eventService.end(event);
        }
    }

    @Override
    public void requestTrackSetting(String vin, Integer trackInterval, Integer tracks) throws ServLayerException {
        AssertRequired("vin", vin);
        OtaDto otaDto = new OtaDto(getTboxId(vin), vin, Constants.AID_SVT, 3);
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            eventService.end(event);
        }
        otaDto.setEventId(event.getId());
        sendQueue(otaDto, new TrackSettingReqDto(trackInterval, tracks));
    }

    @Override
    public void requestSingleTrack(String vin) throws ServLayerException {
        AssertRequired("vin", vin);
        OtaDto otaDto = new OtaDto(getTboxId(vin), vin, Constants.AID_SVT, 4);
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            eventService.end(event);
        }
        otaDto.setEventId(event.getId());
        sendQueue(otaDto);
    }

    @Override
    public void requestCloseAlarm(String vin) throws ServLayerException {
        AssertRequired("vin", vin);
        OtaDto otaDto = new OtaDto(getTboxId(vin), vin, Constants.AID_SVT, 5);
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            eventService.end(event);
        }
        otaDto.setEventId(event.getId());
        sendQueue(otaDto);
    }

    @Override
    public void responseCloseAlarm(OtaDto otaDto, Boolean allAlarmClosed,
                                   List<StolenAlarmDto> stolenAlarmDtos) throws ServLayerException {
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            if(allAlarmClosed) {
                eventService.end(event);
            }
            else {
                eventService.error(getVin(otaDto), getCode(otaDto), 1, event.getId());
            }
        }
    }

    @Override
    public void requestAuthKey(String vin, Integer keyId) throws ServLayerException {
        AssertRequired("vin,keyId", vin, keyId);
        OtaDto otaDto = new OtaDto(getTboxId(vin), vin, Constants.AID_SVT, 7);
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            eventService.end(event);
        }
        otaDto.setEventId(event.getId());
        sendQueue(otaDto, new AuthKeyReqDto(keyId));
    }

    @Override
    public void responseAuthKey(OtaDto otaDto, Boolean keyIsAccepted,
                                Integer failureReason) throws ServLayerException {
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            eventService.end(event);
        }
    }

    @Override
    public void requestImmobilise(String vin, Integer immoStatus) throws ServLayerException {
        AssertRequired("vin,immoStatus", vin, immoStatus);
        OtaDto otaDto = new OtaDto(getTboxId(vin), vin, Constants.AID_SVT, 9);
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            eventService.end(event);
        }
        otaDto.setEventId(event.getId());
        sendQueue(otaDto, new ImmobiliseReqDto(immoStatus));
    }

    @Override
    public void responseImmobilise(OtaDto otaDto, Integer immoStatus,
                                   Integer failureReason) throws ServLayerException {
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            eventService.end(event);
        }
    }

    @Override
    public void requestUpdateProtectStrategy(String vin, Date startTime, Date endTime,
                                             List<ProtectStrategySettingDto> protectStrategySettingDtos)
            throws ServLayerException {
        AssertRequired("vin", vin);
        OtaDto otaDto = new OtaDto(getTboxId(vin), vin, Constants.AID_SVT, 11);
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            eventService.end(event);
        }
        otaDto.setEventId(event.getId());
        sendQueue(otaDto, new UpdateProtectStrategyReqDto(startTime, endTime, protectStrategySettingDtos));
    }

    @Override
    public void responseUpdateProtectStrategy() {
        // 此处输入有问题，待确认
    }

    @Override
    public void requestAlarm(String vin) throws ServLayerException {
        AssertRequired("vin", vin);
        OtaDto otaDto = new OtaDto(getTboxId(vin), vin, Constants.AID_SVT, 13);
        Event event = new EventAssembler().fromOtaDto(otaDto);
        eventService.start(event);
        if(!event.isRetry()) {
            eventService.end(event);
        }
        otaDto.setEventId(event.getId());
        sendQueue(otaDto);
    }
}