package com.example.jsurfnet.utils;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class MongoDriver {

    private static final ConnectionString connectionString = new ConnectionString(""); //Replace this with the connection string
    private static final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

    private static final MongoClient mc = MongoClients.create(settings);
    private static final MongoDatabase database = mc.getDatabase("JSurfNet");

    public static MongoDatabase getMongo() {
        return database;
    }

    public static MongoClient getClient() {
        return mc;
    }
}