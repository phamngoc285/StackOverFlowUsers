package com.phamngoc.sofusers.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("account_id")
    public String id;
    @SerializedName("display_name")
    public String name;
    @SerializedName("profile_image")
    public String avatar;
    @SerializedName("reputation")
    public double reputation;
    @SerializedName("location")
    public String location;
    @SerializedName("last_access_date")
    public long lastAccessDate;

    public boolean isBookmarked;


    public User(){

    }

    public User(String id, String name, String avatar, double reputation, String location, long lastAccessDate) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.reputation = reputation;
        this.location = location;
        this.lastAccessDate = lastAccessDate;
    }

    public User(String name, String avatar, double reputation, String location, long lastAccessDate) {
        this.name = name;
        this.avatar = avatar;
        this.reputation = reputation;
        this.location = location;
        this.lastAccessDate = lastAccessDate;
    }

    public User(String id, String name, String avatar, Double reputation, String location, long lastAccessDate, boolean isBookmarked) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.reputation = reputation;
        this.location = location;
        this.lastAccessDate = lastAccessDate;
        this.isBookmarked = isBookmarked;
    }
}
