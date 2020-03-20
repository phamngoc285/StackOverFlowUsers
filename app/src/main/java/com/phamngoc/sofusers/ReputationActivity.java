package com.phamngoc.sofusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.phamngoc.sofusers.Adapters.ReputationHistoryAdapter;
import com.phamngoc.sofusers.Constants.Parameters;
import com.phamngoc.sofusers.Listeners.PaginationListener;
import com.phamngoc.sofusers.Model.GetReputationResponse;
import com.phamngoc.sofusers.Model.GetUserListResponse;
import com.phamngoc.sofusers.Model.ReputationHistory;
import com.phamngoc.sofusers.Model.User;
import com.phamngoc.sofusers.Services.RetrofitClientInstance;
import com.phamngoc.sofusers.Services.RetrofitClientServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.phamngoc.sofusers.Listeners.PaginationListener.PAGE_START;

public class ReputationActivity extends AppCompatActivity {
    String userid;
    TextView userName;
    TextView reputation;
    TextView location;
    ImageView avatar;
    ImageView bookmark;
    RecyclerView reputationList;
    ReputationHistoryAdapter reputatonAdapter;
    List<ReputationHistory> reputationChanges;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;


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
        reputationList.addOnScrollListener(new PaginationListener(layoutManager){
            @Override
            protected void loadMoreItems() {
                GetUserReputation();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        GetUserReputation();
        /*reputationChanges.addAll(items);
        reputatonAdapter.notifyDataSetChanged();*/

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
        userid = intent.getStringExtra(Parameters.USERID);
        String username = intent.getStringExtra(Parameters.USERNAME);
        String useravatar = intent.getStringExtra(Parameters.USERAVATAR);

        if(username != null && !username.isEmpty()){
            userName.setText(username);
        }

        if(username != null && !username.isEmpty()) {
            Picasso.with(getApplicationContext()).load(useravatar).into(avatar);
        }
    }

    private void GetUserReputation() {
        if(isLoading) return;
        isLoading = true;

        RetrofitClientServices service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientServices.class);
        Call<GetReputationResponse> call = service.GetUserReputation("11683"/*userid*/, String.valueOf(currentPage), String.valueOf(PaginationListener.PAGE_SIZE), "stackoverflow");
        call.enqueue(new Callback<GetReputationResponse>() {
            @Override
            public void onResponse(Call<GetReputationResponse> call, Response<GetReputationResponse> response) {
                //swipeRefresh.setRefreshing(false);

                //remove loading before add items
                if (currentPage != PAGE_START) reputatonAdapter.removeLoading();
                GetReputationResponse result = response.body();
                reputationChanges.addAll(result.ReputationChanges);
                reputatonAdapter.notifyDataSetChanged();
                currentPage++;
                if(result.HasMore){
                    reputatonAdapter.addLoading();
                }
                else{
                    isLastPage = true;
                }

                isLoading = false;
            }

            @Override
            public void onFailure(Call<GetReputationResponse> call, Throwable t) {
                if (currentPage != PAGE_START ) reputatonAdapter.removeLoading();
                //swipeRefresh.setRefreshing(false);
                Toast.makeText(ReputationActivity.this, t.getMessage() /*"Opps, something went wrong...Please try later!"*/, Toast.LENGTH_SHORT).show();
                isLoading = false;

                List<User> items = new ArrayList<>();
                for(int i =0; i <5; i++){
                    items.add(new User("Dummy", "https://www.gravatar.com/avatar/24780fb6df85a943c7aea0402c843737?s=128&d=identicon&r=PG", "", "", "" ));
                }
                
                //users.addAll(items);
                //mUserAdapter.notifyDataSetChanged();
            }
        });
    }
}
