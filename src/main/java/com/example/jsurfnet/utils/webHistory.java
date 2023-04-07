package com.example.jsurfnet.utils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.Binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class webHistory implements java.io.Serializable {

    private List<History> userHistory = new ArrayList<>();

    public void appendHistory(String url) {
        History h = new History(url);
        userHistory.add(0, h);
        new Thread(() -> {
            MongoDriver.getMongo().getCollection("history").updateOne(Filters.eq("user", CurrentUser.getInstance().getUsername()), Updates.set("history", this.getSerialized()));
        }).start();
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

    public static webHistory getUserHistory() throws Exception {
        MongoCollection<Document> collection = MongoDriver.getMongo().getCollection("history");
        FindIterable<Document> iterable = collection.find(Filters.and(
                Filters.eq("user", CurrentUser.getInstance().getUsername())));
        byte[] data = iterable.first().get("history", Binary.class).getData();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (webHistory) ois.readObject();
    }

    public List<History> getList() {
        return userHistory;
    }

    public void clear() {
        userHistory.clear();
        MongoDriver.getMongo().getCollection("history").updateOne(Filters.eq("user", CurrentUser.getInstance().getUsername()), Updates.set("history", this.getSerialized()));
    }
}
