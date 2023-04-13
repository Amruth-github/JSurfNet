package com.example.jsurfnet.utils;

import com.example.jsurfnet.controllers.TabsController;
import com.example.jsurfnet.utils.MongoDriver;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.Binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class PasswordManager implements java.io.Serializable {

    private HashMap<String, Credential> url_to_passwords = new HashMap<>();


    public void addCreds(String url, String username, String password) {
        url_to_passwords.put(TabsController.gethost(url), new Credential(username, password));
        new Thread(() -> {
            MongoDriver.getMongo().getCollection("password").updateOne(Filters.eq("user", CurrentUser.getInstance().getUsername()), Updates.set("passwords", this.getSerialized()));
        }).start();
    }

    public void updateCreds(String url, String username, String password) {
        url_to_passwords.put(url.strip(), new Credential(username, password));
        new Thread(() -> {
            MongoDriver.getMongo().getCollection("password").updateOne(Filters.eq("user", CurrentUser.getInstance().getUsername()), Updates.set("passwords", this.getSerialized()));
        }).start();
    }

    public boolean exists(String url) {
        Credential pwd = url_to_passwords.get(TabsController.gethost(url));
        return pwd != null;
    }

    public Credential getCreds(String url) {
        return url_to_passwords.get(TabsController.gethost(url));
    }

    public static PasswordManager getUserPassword() throws Exception {
        MongoCollection<Document> collection = MongoDriver.getMongo().getCollection("password");
        FindIterable<Document> iterable = collection.find(Filters.and(
                Filters.eq("user", CurrentUser.getInstance().getUsername())));
        byte[] data = iterable.first().get("passwords", Binary.class).getData();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        PasswordManager pm = (PasswordManager) ois.readObject();
        return pm;
    }

    public HashMap<String, Credential> getHash() {
        return url_to_passwords;
    }

    public byte[] getSerialized() {
        try {
            // Serialize the PasswordManager object to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
