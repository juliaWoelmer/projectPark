USE sys;

CREATE TABLE User (
	UserId INTEGER,
    Username VARCHAR(50) NOT NULL,
    Password VARCHAR(20) NOT NULL,
    PRIMARY KEY (UserId)
);