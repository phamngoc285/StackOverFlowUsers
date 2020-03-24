package com.phamngoc.sofusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phamngoc.sofusers.Adapters.ReputationHistoryAdapter;
import com.phamngoc.sofusers.Constants.BookmarkStatus;
import com.phamngoc.sofusers.Constants.Parameters;
import com.phamngoc.sofusers.Helpers.DBHelper;
import com.phamngoc.sofusers.Helpers.DateTimeHelper;
import com.phamngoc.sofusers.Listeners.PaginationListener;
import com.phamngoc.sofusers.Model.GetReputationResponse;
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

import static com.phamngoc.sofusers.Constants.APIConstants.API_KEY;
import static com.phamngoc.sofusers.Constants.APIConstants.STACKOVERFLOW;
import static com.phamngoc.sofusers.Listeners.PaginationListener.PAGE_START;

public class ReputationActivity extends AppCompatActivity {
    TextView userNameTV;
    TextView reputationTV;
    TextView locationTV;
    TextView lasAccessDateTV;
    ImageView avatarIV;
    ImageView bookmarkIV;
    RecyclerView reputationList;
    LinearLayout noreputationview;
    ReputationHistoryAdapter reputatonAdapter;
    List<ReputationHistory> reputationChanges;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    User user;
    //user position in list
    int userposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reputation);
        setTitle("User's reputation history");
        InitViews();
        GetParameters();
        reputationChanges = new ArrayList<>();

        reputatonAdapter = new ReputationHistoryAdapter(reputationChanges, this);
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
    }

    private void InitViews(){
        userNameTV = findViewById(R.id.username);
        reputationTV = findViewById(R.id.reputation);
        locationTV = findViewById(R.id.location);
        avatarIV = findViewById(R.id.avatar);
        bookmarkIV = findViewById(R.id.bookmark);
        reputationList = findViewById(R.id.reputation_list);
        lasAccessDateTV = findViewById(R.id.lastActive);
        noreputationview = findViewById(R.id.no_reputation_view);
        bookmarkIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookMarkClicked(user);
            }
        });
    }

    private void GetParameters(){
        Intent intent = getIntent();
        String userid = intent.getStringExtra(Parameters.USER_ID);
        String username = intent.getStringExtra(Parameters.USER_NAME);
        String useravatar = intent.getStringExtra(Parameters.USER_AVATAR);
        String userlocation = intent.getStringExtra(Parameters.USER_LOCATION);
        double reputation = intent.getDoubleExtra(Parameters.USER_REPUTATION, 0);
        long lastaccessdate = intent.getLongExtra(Parameters.USER_LAST_ACCESS_DATE, 0);
        boolean isbookmarked = intent.getBooleanExtra(Parameters.IS_USER_BOOKMARKED, false);
        userposition = intent.getIntExtra(Parameters.USER_POSITION, -1);

        user = new User(userid, username, useravatar, reputation, userlocation, lastaccessdate, isbookmarked);

        if(username != null && !username.isEmpty()){
            userNameTV.setText(username);
        }

        if(useravatar != null && !useravatar.isEmpty()) {
            Picasso.with(getApplicationContext()).load(useravatar).into(avatarIV);
        }

        if(userlocation != null && !userlocation.isEmpty()){
            locationTV.setText(userlocation);
        }

        reputationTV.setText(String.valueOf(reputation));

        if(isbookmarked){
            bookmarkIV.setImageResource(R.mipmap.bookmarked);
        }

        String daylastactive = DateTimeHelper.GetDateFromTimeStand(lastaccessdate);
        lasAccessDateTV.setText(daylastactive);
    }

    public void BookMarkClicked(User user) {
        if(userposition >= 0){
            BookmarkStatus result = MainActivity.itemListener.onItemBookMarkClicked(null, userposition);

            if(result == BookmarkStatus.Bookmarked){
                user.isBookmarked = true;
                bookmarkIV.setImageResource(R.mipmap.bookmarked);
            }
            else if(result == BookmarkStatus.NotBookmarked){
                user.isBookmarked = false;
                bookmarkIV.setImageResource(R.mipmap.bookmark);
            }
        }
    }

    void ShowReputation(){
        if(reputationChanges != null && reputationChanges.size() > 0){
            reputationList.setVisibility(View.VISIBLE);
            noreputationview.setVisibility(View.GONE);
        }
        else {
            reputationList.setVisibility(View.GONE);
            noreputationview.setVisibility(View.VISIBLE);
        }
    }

    private void GetUserReputation() {
        if(isLoading) return;
        isLoading = true;

        RetrofitClientServices service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientServices.class);
        Call<GetReputationResponse> call = service.GetUserReputation(user.id, String.valueOf(currentPage), String.valueOf(PaginationListener.PAGE_SIZE), STACKOVERFLOW, API_KEY);
        call.enqueue(new Callback<GetReputationResponse>() {
            @Override
            public void onResponse(Call<GetReputationResponse> call, Response<GetReputationResponse> response) {
                //swipeRefresh.setRefreshing(false);

                //remove loading before add items
                if (currentPage != PAGE_START) reputatonAdapter.removeLoading();
                GetReputationResponse result = response.body();
                if(result != null && result.ReputationChanges != null && result.ReputationChanges.size() > 0){
                    reputationChanges.addAll(result.ReputationChanges);
                    currentPage++;
                }
                else {
                    Toast.makeText(ReputationActivity.this, "Couldn't load more reputation history.", Toast.LENGTH_SHORT).show();
                }


                if(result.HasMore){
                    reputatonAdapter.addLoading();
                }
                else{
                    isLastPage = true;
                }

                isLoading = false;
                ShowReputation();
            }

            @Override
            public void onFailure(Call<GetReputationResponse> call, Throwable t) {
                if (currentPage != PAGE_START ) reputatonAdapter.removeLoading();
                //swipeRefresh.setRefreshing(false);
                Toast.makeText(ReputationActivity.this, t.getMessage() /*"Opps, something went wrong...Please try later!"*/, Toast.LENGTH_SHORT).show();
                isLoading = false;
                ShowReputation();
            }
        });
    }

}
