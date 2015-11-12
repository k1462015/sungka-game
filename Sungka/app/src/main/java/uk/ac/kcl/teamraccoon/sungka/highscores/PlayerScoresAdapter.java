package uk.ac.kcl.teamraccoon.sungka.highscores;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.kcl.teamraccoon.sungka.R;

public class PlayerScoresAdapter extends RecyclerView.Adapter<PlayerScoresAdapter.ViewHolder> {
    private ArrayList<String[]> scoresData;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvScores;
        public TextView tvDate;

        public ViewHolder(View v) {
            super(v);
            tvScores = (TextView) v.findViewById(R.id.score_text_rv);
            tvDate = (TextView) v.findViewById(R.id.score_date_rv);
        }
    }

    public PlayerScoresAdapter(ArrayList<String[]> scoresData) {
        this.scoresData = scoresData;
    }

    @Override
    public PlayerScoresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_player_score_rv, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvScores.setText(scoresData.get(position)[0]);
        String date = scoresData.get(position)[1];
        if(date.isEmpty()) { date = "No date"; }
        holder.tvDate.setText(date);
    }

    @Override
    public int getItemCount() {
        if(scoresData == null) { return 0; }
        return scoresData.size();
    }
}
