package com.phamngoc.sofusers.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserListResponse {
    @SerializedName("items")
    public List<User> Users;

    @SerializedName("has_more")
    public boolean HasMore;

    @SerializedName("quota_remaining")
    public int QuotaRemaining;

    @SerializedName("quota_max")
    public int QuotaMax;
}
