package com.phamngoc.sofusers.Model;

import com.google.gson.annotations.SerializedName;

public class ReputationHistory {

    @SerializedName("reputation_history_type")
    public String Type;
    @SerializedName("reputation_change")
    public int Change;
    @SerializedName("creation_date")
    public long CreatedAt;
    @SerializedName("post_id")
    public String PostId;

    public ReputationHistory() {
    }

    public ReputationHistory(String reputationType, int change, long createdAt, String postId) {
        Type = reputationType;
        Change = change;
        CreatedAt = createdAt;
        PostId = postId;
    }
}
