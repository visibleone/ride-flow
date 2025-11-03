db = db.getSiblingDB('driverdb');

db.createUser({
    user: "driverappuser",
    pwd: "driverappsecret",
    roles: [{ role: "readWrite", db: "driverdb" }]
});

// To initialize the database with some data:
// db.myCollection.insertOne({ initialized: true });