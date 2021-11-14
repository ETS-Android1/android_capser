package com.globalcapsleague.app.activity.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globalcapsleagueapp.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Post> posts;

    public NewsAdapter(List<Post> posts){
        this.posts = posts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post,parent,false);
            return new PostViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((PostViewHolder)holder).getTitle().setText(posts.get(position).getTitle());
            ((PostViewHolder)holder).getSignature().setText(posts.get(position).getSignature());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView signature;

        public PostViewHolder(View itemView){
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.post_title);
            signature = (TextView) itemView.findViewById(R.id.post_signature);
        }

        public TextView getTitle(){
            return title;
        }
        public TextView getSignature(){
            return signature;
        }
    }




}
