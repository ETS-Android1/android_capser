package com.globalcapsleague.app.lists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globalcapsleagueapp.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MenuObject> menuObjects;

    public ListAdapter(List<MenuObject> menuObjects){
        this.menuObjects = menuObjects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == R.layout.list_heading){
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_heading,parent,false);
             return new HeadingViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element,parent,false);
            return new ElementViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(menuObjects.get(position).getType().equals(MenuObject.MenuObjectType.ELEMENT)){
            return R.layout.list_element;
        } else{
            return R.layout.list_heading;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)==R.layout.list_heading){
            ((HeadingViewHolder)holder).getTextView().setText(menuObjects.get(position).getText());
        } else {
            ((ElementViewHolder)holder).getTextView().setText(menuObjects.get(position).getText());
            if(menuObjects.get(position).getDrawable()!=0) {
                ((ElementViewHolder) holder).getImageView().setImageResource(menuObjects.get(position).getDrawable());
            }
            if(menuObjects.get(position).getOnClickListener() != null) {
                ((ElementViewHolder) holder).getConstraintLayout().setOnClickListener(menuObjects.get(position).getOnClickListener());
            }
        }

    }

    @Override
    public int getItemCount() {
        return menuObjects.size();
    }

    public static class HeadingViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public HeadingViewHolder(View itemView){
            super(itemView);


            textView = (TextView) itemView.findViewById(R.id.heading);
        }

        public TextView getTextView(){
            return textView;
        }
    }



    public static class ElementViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ConstraintLayout constraintLayout;
        private ImageView imageView;

        public ElementViewHolder(View itemView){
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.elementText);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.element_layout);
            imageView = (ImageView) itemView.findViewById(R.id.element_image);
        }

        public TextView getTextView(){
            return textView;
        }
        public ConstraintLayout getConstraintLayout(){return constraintLayout;}
        public ImageView getImageView(){return imageView;}

    }
}
