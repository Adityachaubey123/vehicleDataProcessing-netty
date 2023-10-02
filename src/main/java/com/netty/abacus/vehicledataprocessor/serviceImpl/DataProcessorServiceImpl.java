package com.netty.abacus.vehicledataprocessor.serviceImpl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netty.abacus.vehicledataprocessor.dto.DoorOpenEventDto;
import com.netty.abacus.vehicledataprocessor.dto.LocationEventDto;
import com.netty.abacus.vehicledataprocessor.dto.SpeedAlertEventDto;
import com.netty.abacus.vehicledataprocessor.model.DoorOpenEvent;
import com.netty.abacus.vehicledataprocessor.model.LocationEvent;
import com.netty.abacus.vehicledataprocessor.model.SpeedAlertEvent;
import com.netty.abacus.vehicledataprocessor.repository.DoorEventRepository;
import com.netty.abacus.vehicledataprocessor.repository.LocationEventRepository;
import com.netty.abacus.vehicledataprocessor.repository.SpeedEventRepository;
import com.netty.abacus.vehicledataprocessor.service.DataProcessorService;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DataProcessorServiceImpl implements DataProcessorService {
	
    private static final String CSV_FILE_PATH = "C:\\Users\\hp\\Documents\\Aayushi\\vehicledataprocessor\\src\\main\\resources\\data.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Autowired
    private SpeedEventRepository speedAlertEventRepository;
    
    @Autowired
    private DoorEventRepository doorEventRepository;

    @Autowired
    private LocationEventRepository locationEventRepository;
    
 // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void dataProcessor() {
        try (FileReader fileReader = new FileReader(CSV_FILE_PATH);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
            	long deviceId = Long.parseLong(record.get("device_id"));
                double latitude = Double.parseDouble(record.get("latitude"));
                double longitude = Double.parseDouble(record.get("longitude"));
                double vehicleSpeed = Double.parseDouble(record.get("vehicle_speed"));
                int doorStatus = Integer.parseInt(record.get("door_status"));
                String timestampStr = record.get("timestamp");
                Date timestamp = parseDate(timestampStr);

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
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private Date parseDate(String timestampStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.parse(timestampStr);
    }

    @Override
    public SpeedAlertEventDto speedAlertEventInput(long deviceId, double latitude, double longitude, Date timestampStr, double vehicleSpeed) {
        SpeedAlertEventDto speedDto = new SpeedAlertEventDto();
        speedDto.setDeviceId(deviceId);
        speedDto.setLatitude(latitude);
        speedDto.setLongitude(longitude);
        speedDto.setTimestamp(timestampStr);
        speedDto.setVehicleSpeed(vehicleSpeed);
        return speedDto;
    }

    @Override
    public DoorOpenEventDto doorOpenEventInput(long deviceId, int doorStatus, double latitude, double longitude, Date timestampStr) {
        DoorOpenEventDto doorDto = new DoorOpenEventDto();
        doorDto.setDeviceId(deviceId);
        doorDto.setDoorStatus(doorStatus);
        doorDto.setLatitude(latitude);
        doorDto.setLongitude(longitude);
        doorDto.setTimestamp(timestampStr);
        return doorDto;
    }

    @Override
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
