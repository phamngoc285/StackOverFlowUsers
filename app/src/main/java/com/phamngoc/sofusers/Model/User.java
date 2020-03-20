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
    public String reputation;
    @SerializedName("location")
    public String location;
    @SerializedName("last_access_date")
    public String lastAccessDate;

    public User(){

    }

    public User(String name, String avatar, String reputation, String location, String lastAccessDate) {
        this.name = name;
        this.avatar = avatar;
        this.reputation = reputation;
        this.location = location;
        this.lastAccessDate = lastAccessDate;
    }
}
