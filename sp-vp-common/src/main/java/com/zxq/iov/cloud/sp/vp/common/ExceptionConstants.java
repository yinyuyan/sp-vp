package com.zxq.iov.cloud.sp.vp.common;

/**
 * 安防 异常常量类
 *
 * @author 叶荣杰
 * create date 2015-7-24 12:48
 * modify date
 * @version 0.1, 2015-7-24
 */
public class ExceptionConstants {

	public static final String PARAMS_IS_NULL = "1"; // 参数为空
	public static final String HAS_CHILD = ""; // 存在子元素
	public static final String AID_NOT_MATCH = ""; // AID不匹配
	public static final String MID_NOT_MATCH = ""; // MID不匹配
	public static final String START_CODE_NOT_MATCH = "15000"; // 没有符合的启动代码
	public static final String PRE_NOT_FIND = "15001"; // 没找到前置
	public static final String CYCLE_LIMIT = "15002"; // 达到循环上限
	public static final String WRONG_CONTROL_CMD = "15100"; // 错误的控制命令
	public static final String WRONG_VEHICLE_STATUS = "15101"; // 错误的车辆状态类型
}