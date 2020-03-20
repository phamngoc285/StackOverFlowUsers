package com.phamngoc.sofusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.phamngoc.sofusers.Adapters.ReputationHistoryAdapter;
import com.phamngoc.sofusers.Constants.Parameters;
import com.phamngoc.sofusers.Model.ReputationHistory;
import com.phamngoc.sofusers.Model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReputationActivity extends AppCompatActivity {

    TextView userName;
    TextView reputation;
    TextView location;
    ImageView avatar;
    ImageView bookmark;
    RecyclerView reputationList;
    ReputationHistoryAdapter reputatonAdapter;
    List<ReputationHistory> reputationChanges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reputation);

        InitViews();
        GetParameters();
        reputationChanges = new ArrayList<>();
        List<ReputationHistory> items = new ArrayList<>();
        for(int i =0; i <5; i++){
            items.add(new ReputationHistory("Vote up", String.valueOf(i), "", ""));
        }

        reputatonAdapter = new ReputationHistoryAdapter(reputationChanges);
        reputationList.setAdapter(reputatonAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reputationList.setLayoutManager(layoutManager);
        reputationChanges.addAll(items);
        reputatonAdapter.notifyDataSetChanged();

    }

    private void InitViews(){
        userName = findViewById(R.id.username);
        reputation = findViewById(R.id.reputation);
        location = findViewById(R.id.location);
        avatar = findViewById(R.id.avatar);
        bookmark = findViewById(R.id.bookmark);
        reputationList = findViewById(R.id.reputation_list);
    }

    private void GetParameters(){
        Intent intent = getIntent();
        String userid = intent.getStringExtra(Parameters.USERID);
        String username = intent.getStringExtra(Parameters.USERNAME);
        String useravatar = intent.getStringExtra(Parameters.USERAVATAR);

        if(username != null && !username.isEmpty()){
            userName.setText(username);
        }

        if(username != null && !username.isEmpty()) {
            Picasso.with(getApplicationContext()).load(useravatar).into(avatar);
        }
    }
}
