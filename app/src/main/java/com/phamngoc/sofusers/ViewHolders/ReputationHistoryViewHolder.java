package com.phamngoc.sofusers.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.phamngoc.sofusers.Model.ReputationHistory;
import com.phamngoc.sofusers.R;

public class ReputationHistoryViewHolder extends BaseViewHolder  {


    TextView reputationType;
    TextView reputationChangePoint;
    TextView changeTime;
    TextView post;

    public ReputationHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        reputationType = itemView.findViewById(R.id.reputation_type);
        reputationChangePoint = itemView.findViewById(R.id.reputation_change);
        changeTime = itemView.findViewById(R.id.reputation_change_time);
        post = itemView.findViewById(R.id.reputation_change_post);
    }

    @Override
    public void OnBind(Object item) {
        try
        {
            ReputationHistory reputationHistory = (ReputationHistory) item;
            if(reputationHistory != null){
                if(reputationHistory.Type != null && !reputationHistory.Type.isEmpty()){
                    reputationType.setText(reputationHistory.Type);
                }

                if(reputationHistory.Change != null && !reputationHistory.Change.isEmpty()){
                    reputationChangePoint.setText(reputationHistory.Change);
                }

                if(reputationHistory.CreatedAt != null && !reputationHistory.CreatedAt.isEmpty()){
                    changeTime.setText(reputationHistory.CreatedAt);
                }

                if(reputationHistory.PostId != null && !reputationHistory.PostId.isEmpty()){
                    post.setText(reputationHistory.PostId);
                }
            }
        }
        catch (Exception e){}

    }
}
