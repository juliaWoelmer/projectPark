USE heroku_a69b82598923256;

ALTER TABLE User ADD CONSTRAINT UNIQUE(Username);

ALTER TABLE User ADD FirstName VARCHAR(50);

ALTER TABLE User ADD LastName VARCHAR(50);

ALTER TABLE User ADD Email VARCHAR(100);

ALTER TABLE User ADD AllowStairs TINYINT(1) DEFAULT 0;

ALTER TABLE User ADD ColorTheme VARCHAR(50) DEFAULT "Day";