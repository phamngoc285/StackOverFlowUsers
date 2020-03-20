package com.phamngoc.sofusers.Model;

import com.google.gson.annotations.SerializedName;

public class ReputationHistory {

    @SerializedName("reputation_history_type")
    public String Type;
    @SerializedName("reputation_change")
    public String Change;
    @SerializedName("creation_date")
    public String CreatedAt;
    @SerializedName("post_id")
    public String PostId;

    public ReputationHistory() {
    }

    public ReputationHistory(String reputationType, String change, String createdAt, String postId) {
        Type = reputationType;
        Change = change;
        CreatedAt = createdAt;
        PostId = postId;
    }
}
