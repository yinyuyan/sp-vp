/*
 * Licensed to SAICMotor,Inc. under the terms of the SAICMotor
 * Software License version 1.0.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * ----------------------------------------------------------------------------
 * Date             Author      Version        Comments
 * 2015-05-13       荣杰         1.0            Initial Version
 * 2015-07-30       荣杰         1.1
 *
 * com.zxq.iov.cloud.sp.vp.entity.status.VehiclePos
 *
 * sp - sp-vp-model
 */

package com.zxq.iov.cloud.sp.vp.entity.status;

import com.saicmotor.telematics.framework.core.dal.entity.MyBaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 安防服务 车辆位置信息实体类
 */
@Entity()
@Table(name = "TB_VEHICLE_POS")
public class VehiclePos extends MyBaseEntity<Long> implements Serializable {

	private static final String SEQ_NAME = "seq_tb_vehicle_pos";

	@Id
	@Column(name = "ID", nullable = false, updatable = false, length = 20)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = SEQ_NAME)
	@TableGenerator(name = SEQ_NAME, table = MY_SEQ_TABLE, allocationSize = 1,
			pkColumnName = "pk_name", valueColumnName = "current_value", pkColumnValue = SEQ_NAME)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICLE_INFO_ID", nullable = false)
	private VehicleInfo vehicleInfo;

	@Column(name = "LONGITUDE", nullable = false, precision = 11, scale = 0)
	private Integer longitude;

	@Column(name = "LATITUDE", nullable = false, precision = 11, scale = 0)
	private Integer latitude;

	@Column(name = "ALTITUDE", nullable = false, precision = 11, scale = 0)
	private Integer altitude;

	@Column(name = "HEADING", precision = 3, scale = 0)
	private Integer heading;

	@Column(name = "SPEED", precision = 3, scale = 0)
	private Integer speed;

	@Column(name = "HDOP", precision = 3, scale = 0)
	private Integer hdop;

	@Column(name = "SATELLITES", precision = 2, scale = 0)
	private Integer satellites;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GPS_TIME", length = 7)
	private Date gpsTime;

	@Column(name = "GPS_STATUS", precision = 2, scale = 0)
	private Integer gpsStatus;

	public VehiclePos() {
	}

	public VehiclePos(Integer longitude, Integer latitude, Integer altitude, Integer heading, Integer speed,
			Integer hdop, Integer satellites, Date gpsTime, Integer gpsStatus) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.heading = heading;
		this.speed = speed;
		this.hdop = hdop;
		this.satellites = satellites;
		this.gpsTime = gpsTime;
		this.gpsStatus = gpsStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VehicleInfo getVehicleInfo() {
		return vehicleInfo;
	}

	public void setVehicleInfo(VehicleInfo vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}

	public Integer getLongitude() {
		return longitude;
	}

	public void setLongitude(Integer longitude) {
		this.longitude = longitude;
	}

	public Integer getLatitude() {
		return latitude;
	}

	public void setLatitude(Integer latitude) {
		this.latitude = latitude;
	}

	public Integer getAltitude() {
		return altitude;
	}

	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}

	public Integer getHeading() {
		return heading;
	}

	public void setHeading(Integer heading) {
		this.heading = heading;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public Integer getHdop() {
		return hdop;
	}

	public void setHdop(Integer hdop) {
		this.hdop = hdop;
	}

	public Integer getSatellites() {
		return satellites;
	}

	public void setSatellites(Integer satellites) {
		this.satellites = satellites;
	}

	public Date getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(Date gpsTime) {
		this.gpsTime = gpsTime;
	}

	public Integer getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(Integer gpsStatus) {
		this.gpsStatus = gpsStatus;
	}
}
