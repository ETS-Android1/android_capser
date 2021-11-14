package com.globalcapsleague.app.activity.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.lists.CustomScrollingLayoutCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = new Intent();
        intent.setAction("com.example.globalcapsleagueapp.GAME_FROM_MOBILE");
        sendBroadcast(intent);

        setContentView(R.layout.activity_news);
        RequestQueue queue = Volley.newRequestQueue(this);

        WearableRecyclerView recyclerView = findViewById(R.id.news_recycler_view);
        String url = "https://globalcapsleague.com/api/dashboard/posts";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("res", response);

                findViewById(R.id.progressBar).setVisibility(View.GONE);
                List<Post> posts = new ArrayList<>();


                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        Post post = new Post();
                        post.setTitle(jsonArray.getJSONObject(i).get("title").toString());
                        post.setSignature(jsonArray.getJSONObject(i).get("signature").toString());
                        posts.add(post);
                    }
                    recyclerView.invalidate();
                    recyclerView.setAdapter(new NewsAdapter(posts));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", "err");
            }
        });

        queue.add(stringRequest);


        recyclerView.setLayoutManager(new WearableLinearLayoutManager(this, new CustomScrollingLayoutCallback()));

        recyclerView.requestFocus();


        recyclerView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_SCROLL &&
                        event.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)
                ) {
                    // Don't forget the negation here
                    float delta = -event.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                            ViewConfigurationCompat.getScaledVerticalScrollFactor(
                                    ViewConfiguration.get(getApplicationContext()), getApplicationContext()
                            );

                    // Swap these axes to scroll horizontally instead
                    ((RecyclerView) v).smoothScrollBy(0, Math.round(delta));

                    return true;
                }
                return false;
            }
        });
    }

}

