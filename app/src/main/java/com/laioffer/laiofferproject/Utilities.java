package com.laioffer.laiofferproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by weiguang on 9/13/17.
 */

public class Utilities {
    public static String timeTransformer(long m) {
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - m;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        String ret = "";
        if (seconds < 60) {
            return seconds + "seconds ago";
        } else if (minutes < 60) {
            return minutes + "minutes ago";
        } else if (hours < 24) {
            return hours + "hours ago";
        } else {
            return days + "days ago";
        }
    }

    public static Bitmap getBitmapFromURL(String Uri) {
        Bitmap bitmap = null;

        if (bitmap == null) {
            try {
                URL url = new URL(Uri);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // create connection object
                connection.setDoInput(true); // setup parameters
                connection.connect();     // actual connection to the remote object is made.
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error: ", e.getMessage().toString());
            }
        }
        return bitmap;
    }
}
