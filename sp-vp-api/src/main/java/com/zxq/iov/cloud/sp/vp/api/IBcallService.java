package com.zxq.iov.cloud.sp.vp.api;

import com.zxq.iov.cloud.sp.vp.api.dto.OtaDto;
import com.zxq.iov.cloud.sp.vp.api.dto.bcall.BcallRecordDto;
import com.zxq.iov.cloud.sp.vp.api.dto.status.VehicleAlertDto;
import com.zxq.iov.cloud.sp.vp.api.dto.status.VehiclePosDto;

import java.util.List;


/**
 * 安防服务 bCall接口
 * @author 叶荣杰
 * create date 2015-6-10 16:47
 * modify date 2015-6-25 9:54
 * @version 0.4, 2015-6-25
 */
public interface IBcallService {

    /**
     * 开始bCall
     * @param otaDto                OTA传输对象
     * @param vehiclePosDtos        车辆GPS位置传输对象列表
     * @param bcallType             呼叫方式
     * @param tboxBatteryStatus     TBOX电池状态
     * @param vehicleBatteryStatus  车辆电池状态
     * @param vehicleAlertDtos      车辆报警传输对象列表
     * @return                      bCall通话传输对象
     */
    BcallRecordDto startBcall(OtaDto otaDto, List<VehiclePosDto> vehiclePosDtos, Integer bcallType,
                              Integer tboxBatteryStatus, Integer vehicleBatteryStatus,
                              List<VehicleAlertDto> vehicleAlertDtos);

    /**
     * 请求bCall状态
     * @param vin                   车辆唯一码
     */
    void requestBcallStatus(String vin);

    /**
     * 更新bCall状态
     * @param otaDto                OTA传输对象
     * @param vehiclePosDtos        车辆GPS位置传输对象列表
     * @param bcallType             呼叫方式
     * @param tboxBatteryStatus     TBOX电池状态
     * @param vehicleBatteryStatus  车辆电池状态
     * @param vehicleAlertDtos      车辆报警传输对象列表
     * @return                      呼叫对象ID
     */
    Long updateBcall(OtaDto otaDto, List<VehiclePosDto> vehiclePosDtos, Integer bcallType,
                     Integer tboxBatteryStatus, Integer vehicleBatteryStatus,
                     List<VehicleAlertDto> vehicleAlertDtos);

    /**
     * 请求挂断通话
     * @param vin                   车辆唯一码
     */
    void requestHangUp(String vin);

    /**
     * 请求车辆回拨
     * @param vin                   车辆唯一码
     * @param callNumber            呼叫号码
     */
    void requestCallBack(String vin, String callNumber);

    /**
     * 响应车辆回拨请求
     * @param otaDto                OTA传输对象
     * @param callbackAccepted      是否接受回拨
     * @param rejectReason          拒绝理由
     */
    void responseCallBack(OtaDto otaDto, Boolean callbackAccepted, Integer rejectReason);

    /**
     * 请求结束bCall
     * @param vin                   车辆唯一码
     */
    void requestCloseBcall(String vin);

    /**
     * 结束bCall
     * @param otaDto                OTA传输对象
     */
    void closeBcall(OtaDto otaDto);
}
