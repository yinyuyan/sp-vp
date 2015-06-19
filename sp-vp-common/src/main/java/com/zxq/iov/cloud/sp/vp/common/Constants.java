package com.zxq.iov.cloud.sp.vp.common;

import java.util.Map;
import java.util.TreeMap;

/**
 * 安防 常量类
 *
 * @author 叶荣杰
 * create date 2015-6-12 11:31
 * modify date 2015-6-19 15:22
 * @version 0.4, 2015-6-19
 */
public class Constants {

	public static final Integer VEHICLE_INFO_SOURCE_JOURNEY = 1;
	public static final Integer VEHICLE_INFO_SOURCE_BCALL = 2;
	public static final Integer VEHICLE_INFO_SOURCE_ECALL = 3;
	public static final Integer VEHICLE_INFO_SOURCE_ICALL = 4;
	public static final Integer VEHICLE_INFO_SOURCE_SVT = 5;
	public static final Integer VEHICLE_INFO_SOURCE_RVC = 6;

	public static final Integer RVC_CMD_FIND_MY_CAT = 0;
	public static final Integer RVC_CMD_VEHICLE_LOCK = 1;
	public static final Integer RVC_CMD_VEHICLE_UNLOCK = 2;
	public static final Integer RVC_CMD_WINDOW_CONTROL = 3;
	public static final Integer RVC_CMD_KEY_MANAGEMENT = 4;
	public static final Integer RVC_CMD_HEATED_SEAT_CONTROL = 5;
	public static final Integer RVC_CMD_CLIMATE_CONTROL = 6;
	public static final Integer RVC_CMD_ENGINE_CONTROL = 11;
	public static final Map<Integer, String> RVC_CMD = new TreeMap<>();
	static {
		RVC_CMD.put(RVC_CMD_FIND_MY_CAT, "定位车辆");
		RVC_CMD.put(RVC_CMD_VEHICLE_LOCK, "锁车门");
		RVC_CMD.put(RVC_CMD_VEHICLE_UNLOCK, "解锁车门");
		RVC_CMD.put(RVC_CMD_WINDOW_CONTROL, "控制车窗");
		RVC_CMD.put(RVC_CMD_KEY_MANAGEMENT, "管理钥匙");
		RVC_CMD.put(RVC_CMD_HEATED_SEAT_CONTROL, "加热座椅");
		RVC_CMD.put(RVC_CMD_CLIMATE_CONTROL, "控制空调");
		RVC_CMD.put(RVC_CMD_ENGINE_CONTROL, "控制引擎");
	}

	public static final Integer RVC_STATUS_PENDING = 0;
	public static final Integer RVC_STATUS_ACTIVE = 1;
	public static final Integer RVC_STATUS_COMPLETED = 2;
	public static final Integer RVC_STATUS_FAILED = 3;

	public static final String AID_CONFIGURATION = "100";
	public static final String AID_RVC = "111";
	public static final String AID_ECALL = "902";
	public static final String AID_BCALL = "903";
	public static final String AID_ICALL = "904";
}
