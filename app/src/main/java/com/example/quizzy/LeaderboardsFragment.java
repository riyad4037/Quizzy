package com.example.quizzy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class LeaderboardsFragment extends Fragment {

    RecyclerView rankRecyclerView;

    public LeaderboardsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RankView = inflater.inflate(R.layout.fragment_leaderboards, container, false);

        rankRecyclerView = RankView.findViewById(R.id.recyclerViewRank);

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final ArrayList<User> users = new ArrayList<>();
        System.out.println("hello");



        database.collection("User Collection").orderBy("coins", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                    User user = snapshot.toObject(User.class);
                    System.out.println(user);
                    users.add(user);
                    System.out.println(users);
                }
                new LeaderboardsAdapter(getContext(), users).notifyDataSetChanged();
            }
        });

        rankRecyclerView.setAdapter(new LeaderboardsAdapter(getContext(), users));
        rankRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return RankView;
    }
}