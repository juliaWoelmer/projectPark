USE heroku_a69b82598923256;

CREATE TABLE Report (
	ReportId INTEGER auto_increment,
    UserId INTEGER,
    SpotId INTEGER,
    isIllegalVehicle BOOLEAN DEFAULT 0,
    IsBlockage  BOOLEAN DEFAULT 0,
    IsResolved BOOLEAN DEFAULT 0,
    TimeOfReport DATETIME NOT NULL,
    PRIMARY KEY (ReportId),
    FOREIGN KEY (UserId) REFERENCES User(UserId),
    FOREIGN KEY (SpotId) REFERENCES Spot(SpotId),
    CHECK (NOT (isIllegalVehicle = 1 AND isBlockage = 1))
);