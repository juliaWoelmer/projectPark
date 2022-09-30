USE sys;

CREATE TABLE Spot (
	SpotId INTEGER,
    UserId INTEGER,
    ParkingLotId INTEGER,
    IsIllegallyOccupied BOOLEAN DEFAULT 0,
    IsLegallyOccupied BOOLEAN DEFAULT 0,
    IsBlocked BOOLEAN DEFAULT 0,
    PRIMARY KEY (SpotId),
    FOREIGN KEY (UserId) REFERENCES User(UserId),
    FOREIGN KEY (ParkingLotId) REFERENCES ParkingLot(ParkingLotId),
    CHECK (NOT (IsIllegallyOccupied = 1 AND (IsLegallyOccupied = 1 OR IsBlocked = 1))),
    CHECK (NOT (IsLegallyOccupied = 1 AND (IsIllegallyOccupied = 1 OR IsBlocked = 1))),
    CHECK (NOT (IsBlocked = 1 AND (IsIllegallyOccupied = 1 OR IsLegallyOccupied = 1)))
);