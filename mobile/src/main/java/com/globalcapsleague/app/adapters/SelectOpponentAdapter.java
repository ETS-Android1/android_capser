package com.globalcapsleague.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.models.OpponentObject;

import java.util.List;

public class SelectOpponentAdapter extends ArrayAdapter<OpponentObject> {


    public SelectOpponentAdapter(@NonNull Context context, int resource, @NonNull List<OpponentObject> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_item,null);
        }

        OpponentObject opponentObject = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(opponentObject.getName());
        return convertView;
    }

}
