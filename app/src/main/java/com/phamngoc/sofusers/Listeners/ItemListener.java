package com.phamngoc.sofusers.Listeners;

import android.view.View;

public interface ItemListener {
    void onItemClicked(View view, int position);

    void onItemBookMarked(View view, int position);
}
