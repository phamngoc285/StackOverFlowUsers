package com.phamngoc.sofusers.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phamngoc.sofusers.Model.User;
import com.phamngoc.sofusers.R;

public class UserViewHolder extends BaseViewHolder {

    public TextView userName;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.username);
    }

    @Override
    public void OnBind(User user) {
        if(user.name != null)
        userName.setText(user.name);
    }
}
