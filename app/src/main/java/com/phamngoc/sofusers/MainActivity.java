package com.phamngoc.sofusers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.phamngoc.sofusers.Model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    RecyclerView mUserList;
    UserAdapter mUserAdapter;
    List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserList = findViewById(R.id.user_list);

        users = new ArrayList<>();
        users.add(new User("user01", "", "", "", ""));
        users.add(new User("user02", "", "", "", ""));
        users.add(new User("user03", "", "", "", ""));
        users.add(new User("user04", "", "", "", ""));

        mUserAdapter = new UserAdapter(users);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mUserList.setLayoutManager(layoutManager);
        mUserList.setAdapter(mUserAdapter);
    }
}
