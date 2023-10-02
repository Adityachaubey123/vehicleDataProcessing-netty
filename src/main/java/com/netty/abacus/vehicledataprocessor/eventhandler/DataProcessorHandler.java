package com.netty.abacus.vehicledataprocessor.eventhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netty.abacus.vehicledataprocessor.dto.DoorOpenEventDto;
import com.netty.abacus.vehicledataprocessor.dto.LocationEventDto;
import com.netty.abacus.vehicledataprocessor.dto.SpeedAlertEventDto;
import com.netty.abacus.vehicledataprocessor.model.DoorOpenEvent;
import com.netty.abacus.vehicledataprocessor.model.LocationEvent;
import com.netty.abacus.vehicledataprocessor.model.SpeedAlertEvent;
import com.netty.abacus.vehicledataprocessor.repository.DoorEventRepository;
import com.netty.abacus.vehicledataprocessor.repository.LocationEventRepository;
import com.netty.abacus.vehicledataprocessor.repository.SpeedEventRepository;

@Component
public class DataProcessorHandler extends ChannelInboundHandlerAdapter {

	@Autowired
	private SpeedEventRepository speedAlertEventRepository;

	@Autowired
	private DoorEventRepository doorEventRepository;

	@Autowired
	private LocationEventRepository locationEventRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws ParseException {
		if (msg instanceof ByteBuf) {
			ByteBuf byteBuf = (ByteBuf) msg;
			String jsonString = byteBuf.toString(CharsetUtil.UTF_8);

			// Process the incoming JSON data
			processJsonData(jsonString);

			// Release the ByteBuf
			byteBuf.release();
		} else {
			// Handle non-ByteBuf data if needed
			ctx.fireChannelRead(msg);
		}
	}

	public void processJsonData(String jsonString) throws ParseException {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonString);

			long deviceId = jsonNode.get("device_id").asLong();
			double latitude = jsonNode.get("latitude").asDouble();
			double longitude = jsonNode.get("longitude").asDouble();
			double vehicleSpeed = jsonNode.get("vehicle_speed").asDouble();
			int doorStatus = jsonNode.get("door_status").asInt();
			String timestampStr = jsonNode.get("timestamp").asText();
			Date timestamp = parseDate(timestampStr.toString());

			if (vehicleSpeed > 90) {
				SpeedAlertEventDto speedDto = speedAlertEventInput(deviceId, latitude, longitude, timestamp, vehicleSpeed);
				SpeedAlertEvent speedEvent = modelMapper.map(speedDto, SpeedAlertEvent.class);
				speedAlertEventRepository.save(speedEvent);
			}

			if (doorStatus == 1) {
				DoorOpenEventDto doorDto = doorOpenEventInput(deviceId, doorStatus, latitude, longitude, timestamp);
				DoorOpenEvent doorEvent = modelMapper.map(doorDto, DoorOpenEvent.class);
				doorEventRepository.save(doorEvent);
			}
			LocationEventDto locationDto = locationEventInput(deviceId, latitude, longitude, vehicleSpeed, timestamp);
			LocationEvent locationEvent = modelMapper.map(locationDto, LocationEvent.class);
			locationEventRepository.save(locationEvent);

		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	private Date parseDate(String timestampStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.parse(timestampStr);
	}

	
	public SpeedAlertEventDto speedAlertEventInput(long deviceId, double latitude, double longitude, Date timestampStr, double vehicleSpeed) {
		SpeedAlertEventDto speedDto = new SpeedAlertEventDto();
		speedDto.setDeviceId(deviceId);
		speedDto.setLatitude(latitude);
		speedDto.setLongitude(longitude);
		speedDto.setTimestamp(timestampStr);
		speedDto.setVehicleSpeed(vehicleSpeed);
		return speedDto;
	}

	
	public DoorOpenEventDto doorOpenEventInput(long deviceId, int doorStatus, double latitude, double longitude, Date timestampStr) {
		DoorOpenEventDto doorDto = new DoorOpenEventDto();
		doorDto.setDeviceId(deviceId);
		doorDto.setDoorStatus(doorStatus);
		doorDto.setLatitude(latitude);
		doorDto.setLongitude(longitude);
		doorDto.setTimestamp(timestampStr);
		return doorDto;
	}

	
	public LocationEventDto locationEventInput(long deviceId, double latitude, double longitude, double vehicleSpeed, Date timestamp) {
		LocationEventDto locationDto = new LocationEventDto();
		locationDto.setDeviceId(deviceId);
		locationDto.setLatitude(latitude);
		locationDto.setLongitude(longitude);
		locationDto.setVehicleSpeed(vehicleSpeed);
		locationDto.setTimestamp(timestamp);
		return locationDto;
	}
}
