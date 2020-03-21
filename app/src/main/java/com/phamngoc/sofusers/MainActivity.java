package com.phamngoc.sofusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.phamngoc.sofusers.Adapters.UserAdapter;
import com.phamngoc.sofusers.Constants.Parameters;
import com.phamngoc.sofusers.Helpers.DBHelper;
import com.phamngoc.sofusers.Services.RetrofitClientServices;
import com.phamngoc.sofusers.Services.RetrofitClientInstance;
import com.phamngoc.sofusers.Listeners.ItemListener;
import com.phamngoc.sofusers.Listeners.PaginationListener;
import com.phamngoc.sofusers.Model.GetUserListResponse;
import com.phamngoc.sofusers.Model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.phamngoc.sofusers.Constants.APIConstants.API_KEY;
import static com.phamngoc.sofusers.Constants.APIConstants.STACKOVERFLOW;
import static com.phamngoc.sofusers.Listeners.PaginationListener.PAGE_START;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener , ItemListener {

    RecyclerView mUserList;
    Button mAllBookmaredButton;
    UserAdapter mUserAdapter;
    SwipeRefreshLayout swipeRefresh;
    List<User> users;
    List<String> bookmarkedIds;

    DBHelper dbHelper;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAllBookmaredButton = findViewById(R.id.allBookmarkedButton);
        mAllBookmaredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage != PAGE_START && !isLastPage) mUserAdapter.removeLoading();
                mUserAdapter.clear();
                List<User> bookmarkedusers = dbHelper.GetAllBookmarked();
                users.addAll(bookmarkedusers);
            }
        });

        mUserList = findViewById(R.id.user_list);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);

        dbHelper = new DBHelper(this);
        bookmarkedIds = new ArrayList<>();
        users = new ArrayList<>();

        //must get bookmarkedID BEFORE get users
        GetBookmaredIDsInLocal();
        GetUsers();

        mUserAdapter = new UserAdapter(users, this, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mUserList.setLayoutManager(layoutManager);
        mUserList.setAdapter(mUserAdapter);

        mUserList.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                GetUsers();
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
    }

    private void GetBookmaredIDsInLocal(){
        bookmarkedIds = dbHelper.GetAllBookmarkedUserIDs();
    }

    private void GetUsers() {
        if(isLoading) return;
        isLoading = true;

        RetrofitClientServices service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientServices.class);
        Call<GetUserListResponse> call = service.GetUsers(String.valueOf(currentPage), String.valueOf(PaginationListener.PAGE_SIZE), STACKOVERFLOW, API_KEY);
        call.enqueue(new Callback<GetUserListResponse>() {
            @Override
            public void onResponse(Call<GetUserListResponse> call, Response<GetUserListResponse> response) {
                swipeRefresh.setRefreshing(false);
                if (currentPage != PAGE_START && !isLastPage) mUserAdapter.removeLoading();
                GetUserListResponse result = response.body();
                for (User user: result.Users) {
                    if(IsUserBookmarked(user)){
                        user.isBookmarked = true;
                    }
                }
                users.addAll(result.Users);
                currentPage++;
                if(result.HasMore){
                    mUserAdapter.addLoading();
                }
                else{
                    isLastPage = true;
                }

                mUserAdapter.addLoading();
                isLoading = false;
            }

            @Override
            public void onFailure(Call<GetUserListResponse> call, Throwable t) {
                if (currentPage != PAGE_START) mUserAdapter.removeLoading();
                swipeRefresh.setRefreshing(false);
                Toast.makeText(MainActivity.this, t.getMessage() /*"Opps, something went wrong...Please try later!"*/, Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
    }

    private boolean IsUserBookmarked(User user){
        return bookmarkedIds.contains(user.id);
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mUserAdapter.clear();
        //must get bookmarkedID BEFORE get users
        GetBookmaredIDsInLocal();
        GetUsers();

    }

    @Override
    public void onItemClicked(View view, int position) {
        Toast.makeText(this, "Item clicked", Toast.LENGTH_LONG).show();

        User item = users.get(position);

        Intent reputationIntent = new Intent(MainActivity.this, ReputationActivity.class);
        reputationIntent.putExtra(Parameters.USER_ID, item.id);
        reputationIntent.putExtra(Parameters.USER_NAME, item.name);
        reputationIntent.putExtra(Parameters.USER_AVATAR, item.avatar);
        reputationIntent.putExtra(Parameters.USER_REPUTATION, item.reputation);
        reputationIntent.putExtra(Parameters.USER_LOCATION, item.location);
        reputationIntent.putExtra(Parameters.USER_LAST_ACCESS_DATE, item.lastAccessDate);
        reputationIntent.putExtra(Parameters.IS_USER_BOOKMARKED, item.isBookmarked);
        MainActivity.this.startActivity(reputationIntent);
    }

    @Override
    public void onBookMarkClicked(View view, int position) {
        User user = users.get(position);
        if(!user.isBookmarked){
            dbHelper.BookMarkUser(user);
            user.isBookmarked = true;
            mUserAdapter.notifyItemChanged(position);
            Toast.makeText(this, "Bookmark saved", Toast.LENGTH_LONG).show();
        }
        else {
            dbHelper.RemoveBookmard(user.id);
            user.isBookmarked = false;
            bookmarkedIds.remove(user.id);
            mUserAdapter.notifyItemChanged(position);
            Toast.makeText(this, "Bookmark removed", Toast.LENGTH_LONG).show();
        }

    }
}
