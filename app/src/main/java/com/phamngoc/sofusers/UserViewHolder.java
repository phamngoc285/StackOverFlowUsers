package com.phamngoc.sofusers;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    TextView userName;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.username);
    }
}
