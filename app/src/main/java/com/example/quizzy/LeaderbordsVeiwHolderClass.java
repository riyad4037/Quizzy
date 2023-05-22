package com.example.quizzy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderbordsVeiwHolderClass extends RecyclerView.ViewHolder {
    TextView indexText, nameText, coinsText;
    ImageView PlayerImage;
    public LeaderbordsVeiwHolderClass(@NonNull View itemView) {
        super(itemView);

        PlayerImage = itemView.findViewById(R.id.imageView7);
        indexText = itemView.findViewById(R.id.index);
        nameText = itemView.findViewById(R.id.name);
        coinsText = itemView.findViewById(R.id.coins);
    }
}
