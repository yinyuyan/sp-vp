/*
 * Licensed to SAICMotor,Inc. under the terms of the SAICMotor
 * Software License version 1.0.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * ----------------------------------------------------------------------------
 * Date             Author      Version        Comments
 * 2015-06-23       荣杰         1.0            Initial Version
 * 2015-10-20       荣杰         1.2
 *
 * com.zxq.iov.cloud.sp.vp.api.RemoteKeyApiImplTest
 *
 * sp - sp-vp-api-impl
 */

package com.zxq.iov.cloud.sp.vp.api;

import com.saicmotor.telematics.framework.core.exception.ServLayerException;
import com.saicmotor.telematics.framework.core.logger.Logger;
import com.saicmotor.telematics.framework.core.logger.LoggerFactory;
import com.saicmotor.telematics.framework.core.test.BaseServiceTestCase;
import com.zxq.iov.cloud.sp.vp.api.dto.OtaDto;
import com.zxq.iov.cloud.sp.vp.api.dto.key.RemoteKeyDto;
import com.zxq.iov.cloud.sp.vp.common.constants.Constants;
import com.zxq.iov.cloud.sp.vp.common.constants.ExceptionConstants;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 安防服务 电子钥匙API测试类
 */
@Transactional
public class RemoteKeyApiImplTest extends BaseServiceTestCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteKeyApiImplTest.class);

	@Autowired
	private IRemoteKeyApi remoteKeyApi;

	private String vin = "11111111111111111";
	private Long tboxId = 1L;
	private Long userId = 1L;
	String mobile = "13148476145";

	@Test
	@Rollback(false)
	public void testCreateKey() throws Exception {
		remoteKeyApi.createKey(userId, vin);
	}

	/**
	 * 测试授权钥匙
	 *
	 * @throws Exception
	 */
	@Test
	@Rollback(false)
	public void testGrantKey() throws Exception {
		String mobile = "15987459878";
		String vin = "123456";
		Long userId = 10000000002985L;
		Integer privilege = 1;
		remoteKeyApi.grantKey(userId, vin, mobile, new Date(1449680461000L), new Date(1481302861000L), privilege);
	}

	/**
	 * 测试授权钥匙给车主自己
	 */
	@Test
	@Rollback(true)
	public void testGrantKeyToOwnerSelf() {
		String mobile = "11111111111";
		Integer privilege = 0;
		try {
			remoteKeyApi.grantKey(userId, vin, mobile, new Date(1446310861), new Date(1480057932), privilege);
			Assert.fail("no exception thrown");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof ServLayerException);
			Assert.assertTrue(e.getMessage()
					.contains(ExceptionConstants.EXCEPTION_MSG.get(ExceptionConstants.CANNOT_GRANT_OWNER_HIMSELF)));
		}
	}

	@Test
	@Rollback(false)
	public void testUpdateKey() throws Exception {
		Long reference = 31L;
		Integer privilege = 2;
		remoteKeyApi.updateKey(userId, reference, new Date(), new Date(), privilege);
	}

	@Test
	@Rollback(false)
	public void testDisableKey() throws Exception {
		Long reference = 31L;
		remoteKeyApi.disableKey(userId, reference);
	}

	@Test
	@Rollback(false)
	public void testEnableKey() throws Exception {
		Long reference = 31L;
		remoteKeyApi.enableKey(userId, reference);
	}

	@Test
	@Rollback(false)
	public void testRemoveKey() throws Exception {
		Long reference = 31L;
		remoteKeyApi.removeKey(userId, reference);
	}

	@Test
	@Rollback(false)
	public void testResponseWriteKey() throws Exception {
		OtaDto otaDto = new OtaDto(tboxId, Constants.AID_REMOTE_KEY, 2);
		remoteKeyApi.responseWriteKey(otaDto, true, null);
	}

	@Test
	@Rollback(false)
	public void testResponseDeleteKey() throws Exception {
		OtaDto otaDto = new OtaDto(tboxId, Constants.AID_REMOTE_KEY, 4);
		remoteKeyApi.responseDeleteKey(otaDto, true, null);
	}

	@Test
	@Rollback(false)
	public void testKeyAlarm() throws Exception {
		OtaDto otaDto = new OtaDto(tboxId, Constants.AID_REMOTE_KEY, 5);
		remoteKeyApi.keyAlarm(otaDto);
	}

	@Test
	@Rollback(false)
	public void testListUserKey() throws Exception {
		List<RemoteKeyDto> remoteKeyDtos = remoteKeyApi.listUserKey(userId);
		LOGGER.info("list size:" + remoteKeyDtos.size());
		Assert.assertTrue(remoteKeyDtos.size() > 0);
	}

	@Test
	@Rollback(false)
	public void testListVehicleKey() throws Exception {
		List<RemoteKeyDto> remoteKeyDtos = remoteKeyApi.listVehicleKey(userId, vin);
		LOGGER.info("list size:" + remoteKeyDtos.size());
		Assert.assertTrue(remoteKeyDtos.size() > 0);
	}
}
