package com.globalcapsleague.app.data;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

public class StringRequestParse extends StringRequest {
    public StringRequestParse(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StringRequestParse(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(
            NetworkResponse response) {

        String strUTF8 = null;
        try {
            strUTF8 = new String(response.data, "UTF-8");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        return Response.success(strUTF8,
                HttpHeaderParser.parseCacheHeaders(response));
    }
}
