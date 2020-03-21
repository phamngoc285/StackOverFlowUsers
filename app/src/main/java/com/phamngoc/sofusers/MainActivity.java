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
        bookmarkedIds = dbHelper.GetAllBookmarkedUserIDs();
        users = new ArrayList<>();
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
                    if(IsUserBookmared(user)){
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

                List<User> items = new ArrayList<>();
                for(int i =0; i <5; i++){
                    items.add(new User("Dummy", "https://www.gravatar.com/avatar/24780fb6df85a943c7aea0402c843737?s=128&d=identicon&r=PG", Double.valueOf("0"), "", Double.valueOf("0") ));
                }

                users.addAll(items);
                mUserAdapter.notifyDataSetChanged();
            }
        });
    }

    private boolean IsUserBookmared(User user){
        if(bookmarkedIds.contains(user.id))
        {
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mUserAdapter.clear();
        GetUsers();

    }

    @Override
    public void onItemClicked(View view, int position) {
        Toast.makeText(this, "Item clicked", Toast.LENGTH_LONG).show();

        User item = users.get(position);

        Intent reputationIntent = new Intent(MainActivity.this, ReputationActivity.class);
        reputationIntent.putExtra(Parameters.USERID, item.id);
        reputationIntent.putExtra(Parameters.USERNAME, item.name);
        reputationIntent.putExtra(Parameters.USERAVATAR, item.avatar);
        MainActivity.this.startActivity(reputationIntent);
    }

    @Override
    public void onItemBookMarked(View view, int position) {
        Toast.makeText(this, "Item book marked", Toast.LENGTH_LONG).show();
        dbHelper.BookMarkUser(users.get(position));
    }
}
