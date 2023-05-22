package com.example.quizzy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class LeaderboardsAdapter extends RecyclerView.Adapter<LeaderboardsAdapter.LeaderboardViewHolder>{


    Context context;
    ArrayList<User> users;

    public LeaderboardsAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        User user = users.get(position);
        System.out.println("user");

        holder.nameText.setText(user.getName());
        holder.coinsText.setText(String.valueOf(user.getCoins()));
        holder.indexText.setText(String.format("#%d", position+1));

        Glide.with(context)
                .load(user.getProfile())
                .into(holder.PlayerImage);
        System.out.println("I am Here!!!");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView indexText, nameText, coinsText;
        ImageView PlayerImage;
        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);

            PlayerImage = itemView.findViewById(R.id.imageView7);
            indexText = itemView.findViewById(R.id.index);
            nameText = itemView.findViewById(R.id.name);
            coinsText = itemView.findViewById(R.id.coins);

        }
    }
}
