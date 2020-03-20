package com.phamngoc.sofusers.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phamngoc.sofusers.Listeners.ItemListener;
import com.phamngoc.sofusers.Model.User;
import com.phamngoc.sofusers.R;
import com.squareup.picasso.Picasso;

public class UserViewHolder extends BaseViewHolder {
    Context context;
    TextView userName;
    TextView reputation;
    TextView location;
    ImageView avatar;
    ImageView bookmark;

    ItemListener listener;

    public UserViewHolder(@NonNull View itemView, Context context, final ItemListener itemListener) {
        super(itemView);
        InitViews();

        this.context = context;
        this.listener = itemListener;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(v, getAdapterPosition());
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bookmark.setBackground(R.color.colorAccent);
                listener.onItemBookMarked(v, getAdapterPosition());
            }
        });
    }

    private void InitViews(){
        userName = itemView.findViewById(R.id.username);
        reputation = itemView.findViewById(R.id.reputation);
        location = itemView.findViewById(R.id.location);
        avatar = itemView.findViewById(R.id.avatar);
        bookmark = itemView.findViewById(R.id.bookmark);
    }

    @Override
    public void OnBind(User user) {
        if(user.name != null){
            userName.setText(user.name);
        }

        if(user.reputation != null){
            reputation.setText(user.reputation);
        }

        if(user.location != null){
            location.setText(user.location);
        }

        if(user.avatar != null){
            //avatar.setImageURI(new Uri(user.avatar));
            Picasso.with(context).load(user.avatar).into(avatar);
        }

    }
}
