package com.phamngoc.sofusers.ViewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.phamngoc.sofusers.Constants.BookmarkStatus;
import com.phamngoc.sofusers.Helpers.DateTimeHelper;
import com.phamngoc.sofusers.Listeners.ItemListener;
import com.phamngoc.sofusers.Model.User;
import com.phamngoc.sofusers.R;
import com.squareup.picasso.Picasso;

public class UserViewHolder extends BaseViewHolder {
    Context context;
    TextView userName;
    TextView reputation;
    TextView location;
    TextView lastActive;
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
                BookmarkStatus result = listener.onItemBookMarkClicked(v, getAdapterPosition());
                if(result == BookmarkStatus.Bookmarked){
                    bookmark.setImageResource(R.mipmap.bookmarked);
                }
                else if(result == BookmarkStatus.NotBookmarked){
                    bookmark.setImageResource(R.mipmap.bookmark);
                }
            }
        });
    }

    private void InitViews(){
        userName = itemView.findViewById(R.id.username);
        reputation = itemView.findViewById(R.id.reputation);
        location = itemView.findViewById(R.id.location);
        lastActive = itemView.findViewById(R.id.lastActive);
        avatar = itemView.findViewById(R.id.avatar);
        bookmark = itemView.findViewById(R.id.bookmark);
    }

    @Override
    public void OnBind(Object item) {
        User user = (User)item;

        if(user.name != null&& !user.name.isEmpty()){
            userName.setText(user.name);
        }


        reputation.setText(String.valueOf(user.reputation));


        if(user.location != null && !user.location.isEmpty()){
            location.setText(user.location);
        }

        if(user.avatar != null && !user.avatar.isEmpty()){
            //avatar.setImageURI(new Uri(user.avatar));
            Picasso.with(context).load(user.avatar).into(avatar);
        }

        if(user.isBookmarked){
            bookmark.setImageResource(R.mipmap.bookmarked);
        }
        else{
            bookmark.setImageResource(R.mipmap.bookmark);
        }

        String daylastactive = DateTimeHelper.GetDateFromTimeStand(user.lastAccessDate);
        lastActive.setText(daylastactive);
    }
}
