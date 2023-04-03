package com.example.jsurfnet.utils;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class MongoDriver {

    private static ConnectionString connectionString = new ConnectionString(""); //Replace this with the connection string

    private static MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

    private static MongoClient mc = MongoClients.create(settings);
    private static MongoDatabase database = mc.getDatabase("JSurfNet");

    public static MongoDatabase getMongo() {
        return database;
    }

    public static MongoClient getClient() {
        return mc;
    }
}