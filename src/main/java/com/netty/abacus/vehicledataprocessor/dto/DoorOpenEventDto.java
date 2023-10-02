package com.netty.abacus.vehicledataprocessor.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoorOpenEventDto {
    private long deviceId;
    private int doorStatus; // 0 for Closed, 1 for Open
    private double latitude;
    private double longitude;
    private Date timestamp;
}
