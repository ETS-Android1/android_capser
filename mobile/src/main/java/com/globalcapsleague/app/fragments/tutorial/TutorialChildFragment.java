package com.globalcapsleague.app.fragments.tutorial;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globalcapsleagueapp.R;

public class TutorialChildFragment extends Fragment {

    private String textValue;
    private int imageId;
    private TextView text;
    private ImageView image;

    public TutorialChildFragment(){
        super(R.layout.tutorial_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text = view.findViewById(R.id.tutorial_text);
        image = view.findViewById(R.id.tutorial_image);
        text.setText(textValue);
        image.setImageResource(imageId);
    }

    public void setTextAndImage(String text, int image){
        textValue = text;
        imageId=image;
    }
}
