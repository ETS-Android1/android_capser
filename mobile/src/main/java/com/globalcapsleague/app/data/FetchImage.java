package com.globalcapsleague.app.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchImage implements Runnable {

    private String hash;
    private Success success;

    public FetchImage(String hash,Success success) {
        this.hash = hash;
        this.success = success;
    }

    @Override
    public void run() {
        Bitmap image = null;
        try {
            InputStream inputStream = new URL("https://www.gravatar.com/avatar/${hash}?s=160&d=https%3A%2F%2Fglobalcapsleague.com%2FdefaultProfile.png".replace("${hash}", hash)).openStream();
            image = BitmapFactory.decodeStream(inputStream);
            success.run(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static interface Success {
        void run(Bitmap bitmap);
    }
}
