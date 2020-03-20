package com.phamngoc.sofusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.phamngoc.sofusers.Constants.Parameters;
import com.squareup.picasso.Picasso;

public class ReputationActivity extends AppCompatActivity {

    TextView userName;
    TextView reputation;
    TextView location;
    ImageView avatar;
    ImageView bookmark;
    RecyclerView reputationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reputation);

        InitViews();
        GetParameters();
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
