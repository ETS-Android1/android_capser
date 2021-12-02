package com.globalcapsleague.app.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.models.GameFromApiDto;
import com.globalcapsleague.app.models.GameListObject;
import com.globalcapsleague.enums.GameType;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {

    private List<GameListObject> objects;
    private Context context;
    boolean isExpanded = false;
    int expandedPosition = 0;

    public GameListAdapter(@NonNull Context context, @NonNull List<GameListObject> objects) {
        this.objects = objects;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(objects.get(position).getContent() instanceof GameFromApiDto){
            return R.layout.game_layout;
        } else {
            return R.layout.list_header;
        }
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType,parent,false);
        return new GameViewHolder(view);
    }

    private String greenRed(boolean green){
        if(green){
            return "<font color='#339933" +
                    "+'>";
        } else {
            return "<font color='red'>";
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        if(getItemViewType(position) == R.layout.game_layout) {
            GameFromApiDto gameDto = (GameFromApiDto) objects.get(position).getContent();

            if(expandedPosition==position && isExpanded && !gameDto.getGameType().equals(GameType.DOUBLES)) {
                holder.hiddenView.setVisibility(View.VISIBLE);
            } else {
                holder.hiddenView.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(l -> {
                Log.i("Click","click");
                int previousExpanded = expandedPosition;
                expandedPosition = holder.getAdapterPosition();
                if(previousExpanded == expandedPosition){
                    isExpanded=!isExpanded;
                    notifyItemChanged(previousExpanded);
                } else {
                    isExpanded = true;
                    notifyItemChanged(previousExpanded);
                    notifyItemChanged(expandedPosition);
                }

            });

            holder.players.setText(gameDto.getTeam1Name() + " vs " + gameDto.getTeam2Name());
            holder.gameTypeImage.setImageResource(GameFromApiDto.getGameImageId(gameDto.getGameType()));
            holder.gameType.setText(GameFromApiDto.getGameString(gameDto.getGameType()));


            if (!gameDto.getGameType().equals(GameType.DOUBLES)) {
                holder.gameResult.setText(gameDto.getPlayer1Stats().getScore() + " : " + gameDto.getPlayer2Stats().getScore());
                int player1Sinks = gameDto.getPlayer1Stats().getSinks();
                int player2Sinks = gameDto.getPlayer2Stats().getSinks();

                int player1Rebuttals = gameDto.getPlayer1Stats().getRebuttals();
                int player2Rebuttals = gameDto.getPlayer2Stats().getRebuttals();

                float player1PointsChange = gameDto.getPlayer1Stats().getPointsChange();
                float player2PointsChange = gameDto.getPlayer2Stats().getPointsChange();

                holder.gameSinks.setText(Html.fromHtml( greenRed(player1Sinks > player2Sinks) + player1Sinks + "</font> : " + greenRed(player1Sinks < player2Sinks)+ player2Sinks + "</font>"
                        ,Html.FROM_HTML_MODE_COMPACT));

                holder.gameRebuttals.setText(Html.fromHtml( greenRed(player1Rebuttals > player2Rebuttals) + player1Rebuttals + "</font> : " + greenRed(player1Rebuttals < player2Rebuttals)+ player2Rebuttals + "</font>"
                        ,Html.FROM_HTML_MODE_COMPACT));

                holder.gamePointsChange.setText(Html.fromHtml( greenRed(player1PointsChange > player2PointsChange) +  String.format("%.02f", player1PointsChange)
                                + "</font> : " + greenRed(player1PointsChange < player2PointsChange)+  String.format("%.02f", player2PointsChange) + "</font>"
                        ,Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.gameResult.setText(gameDto.getTeam1Score() + " : " + gameDto.getTeam2Score());
            }
            holder.gameDate.setText(gameDto.getTime().toLocaleString());
        } else {
            holder.headerTitle.setText((String)objects.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    protected static class GameViewHolder extends RecyclerView.ViewHolder{
        TextView gameResult;
        TextView players;
        TextView gameType;
        TextView gameDate;
        TextView gamePointsChange;
        TextView gameRebuttals;
        TextView gameSinks;
        ImageView gameTypeImage;
        ConstraintLayout hiddenView;

        TextView headerTitle;

        public GameViewHolder(View itemView){
            super(itemView);
            gameResult = (TextView) itemView.findViewById(R.id.game_result);
            gamePointsChange = (TextView) itemView.findViewById(R.id.game_points_change);
            gameRebuttals = (TextView) itemView.findViewById(R.id.game_rebuttals);
            gameSinks = (TextView) itemView.findViewById(R.id.game_sinks);
            players = (TextView) itemView.findViewById(R.id.game_versus);
            gameDate = (TextView) itemView.findViewById(R.id.game_date);
            gameType = (TextView) itemView.findViewById(R.id.game_game_type);
            headerTitle = (TextView) itemView.findViewById(R.id.list_heading);
            gameTypeImage = (ImageView) itemView.findViewById(R.id.game_type_image);
            hiddenView = (ConstraintLayout) itemView.findViewById(R.id.game_hidden_container);
        }
    }
}
