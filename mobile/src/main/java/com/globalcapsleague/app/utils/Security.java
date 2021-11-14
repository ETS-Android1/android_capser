package com.globalcapsleague.app.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.Date;

public class Security {

    public static boolean tokenExpired(String token) {
        String body = token.split("\\.")[1];
        Log.i("Head", new String(Base64.getDecoder().decode(body)));

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new String(Base64.getDecoder().decode(body)));


            Log.i("Time", String.valueOf(Long.valueOf(jsonObject.getString("exp")) * 1000));
            Log.i("Time", String.valueOf(new Date().getTime()));
            if (Long.parseLong(jsonObject.getString("exp")) * 1000 < new Date().getTime()) {
                Log.i("Authentication", "Refresh expired");

                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getEmail(String token){
        String body = token.split("\\.")[1];
        Log.i("Head", new String(Base64.getDecoder().decode(body)));

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new String(Base64.getDecoder().decode(body)));

            return jsonObject.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUserId(String token) {
        String body = token.split("\\.")[1];
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new String(Base64.getDecoder().decode(body)));


            return jsonObject.getString("sub");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
