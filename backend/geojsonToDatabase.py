import mysql.connector
import geojson
from geojson import FeatureCollection



def readInGeoJSON():
    mydb = mysql.connector.connect(
        host="arbor-parker-db.cfw2azkxjlv2.us-east-1.rds.amazonaws.com",
        user="admin",
        password="arborParker495",
        database="arborParkerDB"
    )
    mycursor = mydb.cursor()
    with open("parkingmap.geojson") as parkingSpotsFile:
        parkingSpots = geojson.load(parkingSpotsFile)
        for spot in parkingSpots['features']:
            spotInfo = spot['properties']
            lotName = spotInfo['Lot']
            numberInLot = spotInfo['Spot']
            tier = spotInfo['Tier']
            vanAccessible = 0 if spotInfo['Van_access'] == "no" else 1
            UofMPermitRequired = 0 if spotInfo['UofM_permit_req'] == "no" else 1
            val = (1, lotName, numberInLot, tier, vanAccessible, UofMPermitRequired)
            sql = "INSERT INTO Spot (Open, LotName, NumberInLot, Tier, VanAccessible, UofMPermitRequired) VALUES (%s, %s, %s, %s, %s, %s)"
            mycursor.execute(sql, val)
            mydb.commit()
            spotId = mycursor.lastrowid
            spotInfo['SpotId'] = spotId
            spotInfo['Open'] = 1
    
    with open("parkingmap.geojson", "w") as parkingSpotsFile:
        geojson.dump(parkingSpots, parkingSpotsFile, indent=2)  
    return

readInGeoJSON()