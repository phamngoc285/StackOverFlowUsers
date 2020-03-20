package com.phamngoc.sofusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.phamngoc.sofusers.Helpers.GetSOFUserListService;
import com.phamngoc.sofusers.Helpers.RetrofitClientInstance;
import com.phamngoc.sofusers.Listeners.ItemListener;
import com.phamngoc.sofusers.Listeners.PaginationListener;
import com.phamngoc.sofusers.Model.GetUserListResponse;
import com.phamngoc.sofusers.Model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static com.phamngoc.sofusers.Listeners.PaginationListener.PAGE_START;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener , ItemListener {

    RecyclerView mUserList;
    UserAdapter mUserAdapter;
    SwipeRefreshLayout swipeRefresh;
    List<User> users;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserList = findViewById(R.id.user_list);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);

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
                if(isLoading) return;
                isLoading = true;
                currentPage++;
                GetUsers();
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });
    }

    private void GetUsers() {

        GetSOFUserListService service = RetrofitClientInstance.getRetrofitInstance().create(GetSOFUserListService.class);
        Call<GetUserListResponse> call = service.GetUsers(String.valueOf(currentPage + 1), String.valueOf(PaginationListener.PAGE_SIZE), "stackoverflow");
        call.enqueue(new Callback<GetUserListResponse>() {
            @Override
            public void onResponse(Call<GetUserListResponse> call, Response<GetUserListResponse> response) {
                swipeRefresh.setRefreshing(false);
                if (currentPage != PAGE_START) mUserAdapter.removeLoading();
                GetUserListResponse result = response.body();
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
                    items.add(new User("Dummy", "https://www.gravatar.com/avatar/24780fb6df85a943c7aea0402c843737?s=128&d=identicon&r=PG", "", "", "" ));
                }

                users.addAll(items);
                mUserAdapter.notifyDataSetChanged();
            }
        });
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
    }

    @Override
    public void onItemBookMarked(View view, int position) {
        Toast.makeText(this, "Item book marked", Toast.LENGTH_LONG).show();
    }
}
