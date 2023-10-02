package com.netty.abacus.vehicledataprocessor.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "speed_alert_event")
public class SpeedAlertEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long deviceId;
    private double vehicleSpeed;
    private double latitude;
    private double longitude;
    private Date timestamp;
}
