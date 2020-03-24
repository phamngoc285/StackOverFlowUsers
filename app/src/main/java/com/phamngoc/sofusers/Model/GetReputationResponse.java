package com.phamngoc.sofusers.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetReputationResponse {
    @SerializedName("items")
    public List<ReputationHistory> ReputationChanges;

    @SerializedName("has_more")
    public boolean HasMore;

    @SerializedName("quota_remaining")
    public int QuotaRemaining;

    @SerializedName("quota_max")
    public int QuotaMax;
}
