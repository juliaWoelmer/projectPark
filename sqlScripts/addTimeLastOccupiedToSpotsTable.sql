USE heroku_a69b82598923256;

ALTER TABLE Spot
ADD TimeLastOccupied DATETIME DEFAULT NULL;