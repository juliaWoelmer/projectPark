USE arborParkerDB;

CREATE TABLE Spot (
	SpotId INTEGER auto_increment,
    UserId INTEGER,
    Open BOOLEAN DEFAULT 1,
    LotName VARCHAR(50),
    NumberInLot INTEGER,
    Tier VARCHAR(50),
    VanAccessible BOOLEAN DEFAULT 0,
    UofMPermitRequired BOOLEAN DEFAULT 0,
    IsIllegallyOccupied BOOLEAN DEFAULT 0,
    IsLegallyOccupied BOOLEAN DEFAULT 0,
    IsBlocked BOOLEAN DEFAULT 0,
    PRIMARY KEY (SpotId),
    FOREIGN KEY (UserId) REFERENCES User(UserId),
    CHECK (NOT (IsIllegallyOccupied = 1 AND (IsLegallyOccupied = 1 OR IsBlocked = 1))),
    CHECK (NOT (IsLegallyOccupied = 1 AND (IsIllegallyOccupied = 1 OR IsBlocked = 1))),
    CHECK (NOT (IsBlocked = 1 AND (IsIllegallyOccupied = 1 OR IsLegallyOccupied = 1)))
);