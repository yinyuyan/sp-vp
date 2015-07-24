package com.zxq.iov.cloud.sp.vp.api.impl;

import com.saicmotor.telematics.framework.core.exception.ServLayerException;
import com.zxq.iov.cloud.sp.vp.api.IJourneyService;
import com.zxq.iov.cloud.sp.vp.api.IStatusService;
import com.zxq.iov.cloud.sp.vp.api.dto.OtaDto;
import com.zxq.iov.cloud.sp.vp.api.dto.status.VehiclePosDto;
import com.zxq.iov.cloud.sp.vp.api.dto.status.VehicleStatusDto;
import com.zxq.iov.cloud.sp.vp.common.Constants;
import com.zxq.iov.cloud.sp.vp.dao.config.ITboxDaoService;
import com.zxq.iov.cloud.sp.vp.dao.journey.IJourneyDaoService;
import com.zxq.iov.cloud.sp.vp.entity.journey.Journey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 安防 行程服务实现类
 *
 * @author 叶荣杰
 * create date 2015-6-9 14:19
 * modify date 2015-7-24 11:15
 * @version 0.9, 2015-7-24
 */
@Service
@Qualifier("journeyService")
public class JourneyServiceImpl extends BaseService implements IJourneyService {

    @Autowired
    private IJourneyDaoService journeyDaoService;
    @Autowired
    private ITboxDaoService tboxDaoService;
    @Autowired
    @Qualifier("statusService")
    private IStatusService statusService;

    private static final Integer RUNNING_STATUS = 1;
    private static final Integer END_STATUS = 2;

    @Override
    public void startJourney(OtaDto otaDto, Date startTime, Integer tboxJourneyId, Integer keyId)
            throws Exception {
        Journey journey = findJourneyByTboxJourneyIdAndTboxId(tboxJourneyId, otaDto.getTboxId());
        if(null == journey) {
            Long ownerId = 1L; // 从主数据获得
            journey = new Journey(tboxJourneyId, otaDto.getTboxId(), ownerId,
                    tboxDaoService.findVinById(otaDto.getTboxId()));
            journey.setStartTime(startTime);
            journey.setKeyId(keyId);
            journey.setStatus(RUNNING_STATUS);
            journeyDaoService.createJourney(journey);
        }
        else {
            journey.setStartTime(startTime);
            journey.setKeyId(keyId);
            journeyDaoService.updateJourney(journey);
        }
    }

    @Override
    public void updateJourney(OtaDto otaDto, Integer tboxJourneyId, Integer instFuelConsumption,
                              VehiclePosDto vehiclePosDto) throws Exception {
        Journey journey = findJourneyByTboxJourneyIdAndTboxId(tboxJourneyId, otaDto.getTboxId());
        if(null == journey) {
            Long ownerId = 1L; // 从主数据获得
            journey = new Journey(tboxJourneyId, otaDto.getTboxId(), ownerId,
                    tboxDaoService.findVinById(otaDto.getTboxId()));
            journey.setStatus(RUNNING_STATUS);
            journeyDaoService.createJourney(journey);
        }
        List<VehicleStatusDto> vehicleStatusDtos = new ArrayList<>();
        vehicleStatusDtos.add(new VehicleStatusDto("instFuelConsumption", instFuelConsumption));
        statusService.updateVehicleStatus(otaDto, Constants.VEHICLE_INFO_SOURCE_JOURNEY,
                journey.getId(), vehiclePosDto, vehicleStatusDtos, null);
    }

    @Override
    public void endJourney(OtaDto otaDto, VehiclePosDto startVehiclePosDto, VehiclePosDto endVehiclePosDto,
                           Integer tboxJourneyId, Integer distance, Integer avgSpeed, Integer fuelEco,
                           Integer odometer, Integer fuelLevelPrc, Integer fuelLevelDisp, Integer fuelRange)
            throws Exception {
        Journey journey = findJourneyByTboxJourneyIdAndTboxId(tboxJourneyId, otaDto.getTboxId());
        if(null == journey) {
            Long ownerId = 1L; // 从主数据获得
            journey = new Journey(tboxJourneyId, otaDto.getTboxId(), ownerId,
                    tboxDaoService.findVinById(otaDto.getTboxId()));
            journey.setStatus(END_STATUS);
            journeyDaoService.createJourney(journey);
        }
        else {
            if(null != journey.getStartTime() && startVehiclePosDto.getGpsTime().before(journey.getStartTime())) {
                journey.setStartTime(startVehiclePosDto.getGpsTime());
            }
            journey.setEndTime(endVehiclePosDto.getGpsTime());
            journey.setDistance(distance);
            journey.setAvgSpeed(avgSpeed);
            journey.setFuelConsumption(fuelEco);
            journey.setStatus(END_STATUS);
            journeyDaoService.updateJourney(journey);
        }
        journey.setStartVehicleInfoId(statusService.updateVehicleStatus(otaDto, Constants.VEHICLE_INFO_SOURCE_JOURNEY,
                journey.getId(), startVehiclePosDto, null, null));
        journey.setEndVehicleInfoId(statusService.updateVehicleStatus(otaDto, Constants.VEHICLE_INFO_SOURCE_JOURNEY,
                journey.getId(), endVehiclePosDto, null, null));
        journeyDaoService.updateJourney(journey);
    }

    /**
     * 根据tboxJourneyId获得Journey对象，并增加业务验证
     * @param tboxJourneyId
     * @return
     */
    private Journey findJourneyByTboxJourneyIdAndTboxId(Integer tboxJourneyId, Long tboxId)
            throws ServLayerException {
        AssertRequired("tboxJourneyId", tboxJourneyId);
        return journeyDaoService.findJourneyByTboxJourneyIdAndTboxId(tboxJourneyId, tboxId);
    }
}
