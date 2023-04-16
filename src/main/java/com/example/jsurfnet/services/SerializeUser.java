package com.example.jsurfnet.services;

import com.example.jsurfnet.singleton.CurrentUser;

import java.io.*;

public class SerializeUser {
    public void Serialize() throws IOException {

        String Filename = "userprofiles/"+ CurrentUser.getInstance().getUsername() + ".ser";
        FileOutputStream fileout = new FileOutputStream(Filename);
        ObjectOutputStream out = new ObjectOutputStream(fileout);
        out.writeObject(CurrentUser.getInstance());
        fileout.close();
        System.out.println("User info saved");
    }

    public void deserialize(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream("userprofiles/"+filename+".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            CurrentUser currentUser = (CurrentUser) in.readObject();
            in.close();
            fileIn.close();
            CurrentUser.setInstance(currentUser);
            System.out.println(CurrentUser.getInstance().getUsername());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
