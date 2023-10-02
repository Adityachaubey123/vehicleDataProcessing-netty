package com.netty.abacus.vehicledataprocessor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netty.abacus.vehicledataprocessor.model.SpeedAlertEvent;
import com.netty.abacus.vehicledataprocessor.model.DoorOpenEvent;
import com.netty.abacus.vehicledataprocessor.model.LocationEvent;

import java.util.List;

import com.netty.abacus.vehicledataprocessor.repository.SpeedEventRepository;
import com.netty.abacus.vehicledataprocessor.service.DataProcessorService;
import com.netty.abacus.vehicledataprocessor.repository.DoorEventRepository;
import com.netty.abacus.vehicledataprocessor.repository.LocationEventRepository;

@RestController
@RequestMapping("/api/events")
public class EventController {
	
	@Autowired
	DataProcessorService dataProcessorService;

    @Autowired
    private SpeedEventRepository speedAlertEventRepository;
    
    @Autowired
    private DoorEventRepository doorOpenEventRepository;
    
    @Autowired
    private LocationEventRepository locationEventRepository;

    @GetMapping("/speed-alert")
    public List<SpeedAlertEvent> getAllSpeedAlertEvents() {
    	dataProcessorService.dataProcessor();
        return speedAlertEventRepository.findAll();
    }

    @GetMapping("/door-open")
    public List<DoorOpenEvent> getAllDoorOpenEvents() {
    	dataProcessorService.dataProcessor();
        return doorOpenEventRepository.findAll();
    }

    @GetMapping("/location")
    public List<LocationEvent> getAllLocationEvents() {
    	dataProcessorService.dataProcessor();
        return locationEventRepository.findAll();
    }


}
