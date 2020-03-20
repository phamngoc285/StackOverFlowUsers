package com.phamngoc.sofusers.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phamngoc.sofusers.Model.ReputationHistory;
import com.phamngoc.sofusers.Model.User;
import com.phamngoc.sofusers.R;
import com.phamngoc.sofusers.ViewHolders.BaseViewHolder;
import com.phamngoc.sofusers.ViewHolders.LoadingViewHolder;
import com.phamngoc.sofusers.ViewHolders.ReputationHistoryViewHolder;
import com.phamngoc.sofusers.ViewHolders.UserViewHolder;

import java.util.List;

public class ReputationHistoryAdapter extends RecyclerView.Adapter<BaseViewHolder>{
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private List<ReputationHistory> reputationChanges;

    public ReputationHistoryAdapter(List<ReputationHistory> reputationChanges) {
        this.reputationChanges = reputationChanges;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_NORMAL){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.reputation_history_cell, parent, false);
            return new ReputationHistoryViewHolder(view);
        } else if(viewType == VIEW_TYPE_LOADING){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.loading_cell, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ReputationHistory history = reputationChanges.get(position);
        if(history != null){
            //holder.userName.setText(user.name);
            holder.OnBind(history);
        }
    }

    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == reputationChanges.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return reputationChanges.size();
    }

    public void clear() {
        reputationChanges.clear();
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        reputationChanges.add(new ReputationHistory());
        notifyItemInserted(reputationChanges.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = reputationChanges.size() - 1;
        ReputationHistory item = reputationChanges.get(position);
        if (item != null) {
            reputationChanges.remove(position);
            notifyItemRemoved(position);
        }
    }
}
