package com.netty.abacus.vehicledataprocessor.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates an all-argument constructor
public class LocationEventDto {
    private long deviceId;
    private double latitude;
    private double longitude;
    private double vehicleSpeed;
    private Date timestamp;
}
