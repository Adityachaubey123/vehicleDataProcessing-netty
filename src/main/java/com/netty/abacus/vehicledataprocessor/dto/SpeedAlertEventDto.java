package com.netty.abacus.vehicledataprocessor.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeedAlertEventDto {
    private long deviceId;
    private double vehicleSpeed;
    private double latitude;
    private double longitude;
    private Date timestamp;
}
