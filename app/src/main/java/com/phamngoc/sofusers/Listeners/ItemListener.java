package com.phamngoc.sofusers.Listeners;

import android.view.View;

import com.phamngoc.sofusers.Constants.BookmarkStatus;

public interface ItemListener {
    void onItemClicked(View view, int position);

    BookmarkStatus onItemBookMarkClicked(View view, int position);
}
