package com.globalcapsleague.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.models.GameDto;
import com.globalcapsleague.app.models.GameFromApiDto;
import com.globalcapsleague.app.models.OpponentObject;
import com.globalcapsleague.enums.GameType;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    private List<GameFromApiDto> gameDtos;
    private Context context;

    public GameListAdapter(@NonNull Context context, @NonNull List<GameFromApiDto> objects) {
        gameDtos = objects;
        this.context = context;
    }


    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_layout,parent,false);
        GameViewHolder gameViewHolder = new GameViewHolder(view);
        return gameViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameFromApiDto gameDto = gameDtos.get(position);

        holder.players.setText(gameDto.getTeam1Name() + " vs " + gameDto.getTeam2Name());
        holder.gameTypeImage.setImageResource(GameFromApiDto.getGameImageId(gameDto.getGameType()));
        holder.gameType.setText(GameFromApiDto.getGameString(gameDto.getGameType()));
        if(!gameDto.getGameType().equals(GameType.DOUBLES)) {
            holder.gameResult.setText(gameDto.getPlayer1Stats().getScore() + " : " + gameDto.getPlayer2Stats().getScore());
        }  else {
            holder.gameResult.setText(gameDto.getTeam1Score() + " : " + gameDto.getTeam2Score());
        }
        holder.gameDate.setText(gameDto.getTime().toLocaleString());
    }

    @Override
    public int getItemCount() {
        return gameDtos.size();
    }

    protected class GameViewHolder extends RecyclerView.ViewHolder{
        TextView gameResult;
        TextView players;
        TextView gameType;
        TextView gameDate;
        ImageView gameTypeImage;

        public GameViewHolder(View itemView){
            super(itemView);
            gameResult = (TextView) itemView.findViewById(R.id.game_result);
            players = (TextView) itemView.findViewById(R.id.game_versus);
            gameDate = (TextView) itemView.findViewById(R.id.game_date);
            gameType = (TextView) itemView.findViewById(R.id.game_game_type);
            gameTypeImage = (ImageView) itemView.findViewById(R.id.game_type_image);
        }
    }
}
