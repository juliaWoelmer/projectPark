USE arborParkerDB;

CREATE TABLE ParkingLot (
	ParkingLotId INTEGER auto_increment,
    Name VARCHAR(50),
    Address VARCHAR(250),
    PRIMARY KEY (ParkingLotId)
);