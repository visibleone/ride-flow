db = db.getSiblingDB('driverdb');

db.createUser({
    user: "driverappuser",
    pwd: "driverappsecret",
    roles: [{ role: "readWrite", db: "driverdb" }]
});

// db.myCollection.insertOne({ initialized: true });