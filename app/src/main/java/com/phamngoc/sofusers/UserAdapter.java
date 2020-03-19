package com.phamngoc.sofusers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phamngoc.sofusers.Model.User;
import com.phamngoc.sofusers.ViewHolders.BaseViewHolder;
import com.phamngoc.sofusers.ViewHolders.UserViewHolder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_NORMAL){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.user_cell, parent, false);
            return new UserViewHolder(view);
        } else if(viewType == VIEW_TYPE_LOADING){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.loading_cell, parent, false);
            return new UserViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        User user = users.get(position);
        if(user != null){
            //holder.userName.setText(user.name);
            holder.OnBind(user);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == users.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        users.add(new User());
        notifyItemInserted(users.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = users.size() - 1;
        User item = users.get(position);
        if (item != null) {
            users.remove(position);
            notifyItemRemoved(position);
        }
    }

}
