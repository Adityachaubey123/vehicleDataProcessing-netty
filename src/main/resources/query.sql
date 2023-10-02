CREATE DATABASE vehicleData;
USE vehicleData;

CREATE TABLE DoorOpenEvent (
    deviceId INT AUTO_INCREMENT PRIMARY KEY,
    doorStatus INT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    timestamp DATETIME NOT NULL
);

CREATE TABLE LocationEvent (
    deviceId INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    vehicleSpeed DOUBLE NOT NULL,
    timestamp DATETIME NOT NULL
);

CREATE TABLE SpeedAlertEvent (
    deviceId INT AUTO_INCREMENT PRIMARY KEY,
    vehicleSpeed DOUBLE NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    timestamp DATETIME NOT NULL
);
