package com.phamngoc.sofusers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;

import com.phamngoc.sofusers.Listeners.PaginationListener;
import com.phamngoc.sofusers.Model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.phamngoc.sofusers.Listeners.PaginationListener.PAGE_START;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

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

        mUserAdapter = new UserAdapter(users);

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
        final ArrayList<User> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    itemCount++;
                    User postItem = new User("user "+ itemCount, "", "", "", "");
                    items.add(postItem);
                }
                /**
                 * manage progress view
                 */
                if (currentPage != PAGE_START) mUserAdapter.removeLoading();
                users.addAll(items);
                swipeRefresh.setRefreshing(false);
                // check weather is last page or not
                if (currentPage < totalPage) {
                    mUserAdapter.addLoading();
                } else {
                    isLastPage = true;
                }
                isLoading = false;
            }
        }, 3000);
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mUserAdapter.clear();
        GetUsers();

    }
}
