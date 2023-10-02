package com.netty.abacus.vehicledataprocessor.service;

import java.util.Date;

import com.netty.abacus.vehicledataprocessor.dto.DoorOpenEventDto;
import com.netty.abacus.vehicledataprocessor.dto.LocationEventDto;
import com.netty.abacus.vehicledataprocessor.dto.SpeedAlertEventDto;

public interface DataProcessorService {

	public void dataProcessor();

	public SpeedAlertEventDto speedAlertEventInput(long deviceId, double latitude, double longitude, Date timestampStr,
			double vehicleSpeed);

	public DoorOpenEventDto doorOpenEventInput(long deviceId, int doorStatus, double latitude, double longitude,
			Date timestampStr);

	public LocationEventDto locationEventInput(long deviceId, double latitude, double longitude, double vehicleSpeed,
			Date timestamp);

}