package com.example.jsurfnet.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.awt.image.BufferedImage;
import net.sf.image4j.codec.ico.ICODecoder;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;


public class Icon {
    private URL url;
    private Image i;

    private ImageView iv;


    public Icon(String u) {
        List<BufferedImage> images = null;
        try {
            url = new URL(u);
        }
        catch (MalformedURLException e) {
            try {
                url =  new URL("https://google.com/search?q=" + u);
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        String path = "https://" + url.getHost() + "/favicon.ico";
        if (new File("./icons/" + url.getHost() + ".png").exists()) {
            i = new Image(new File("./icons/" + url.getHost() + ".png").toURI().toString());
        }
        else {
            try {
                InputStream istr = new URL(path).openStream();
                images = ICODecoder.read(istr);
                System.out.println("Request being sent");
                ImageIO.write(images.get(0), "png", new File("./icons/" + url.getHost() + ".png"));
                i = new Image(new File("./icons/" + url.getHost() + ".png").toURI().toString());
            }
            catch (Exception e) {
                i = new Image(new File("./icons/favicon-standard.png").toURI().toString());
            }
        }
        iv = new ImageView(i);
        iv.setFitHeight(16);
        iv.setFitWidth(16);
    }
    public ImageView getImage() {
        return iv;
    }

}
