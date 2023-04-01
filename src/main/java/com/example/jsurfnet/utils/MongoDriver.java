package com.example.jsurfnet.utils;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public final class MongoDriver {

    private static MongoClient mc = new MongoClient("localhost", 27017);

    private static MongoDatabase database = mc.getDatabase("JSurNet");

    public static MongoDatabase getMongo() {
        return database;
    }

    public static MongoClient getClient() {
        return mc;
    }
}