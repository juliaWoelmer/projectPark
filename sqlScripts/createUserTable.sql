USE heroku_a69b82598923256;

CREATE TABLE User (
	UserId INTEGER auto_increment,
    Username VARCHAR(50) NOT NULL,
    Password VARCHAR(20) NOT NULL,
    PRIMARY KEY (UserId)
);