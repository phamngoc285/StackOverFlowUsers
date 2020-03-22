package com.phamngoc.sofusers.ViewHolders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.phamngoc.sofusers.Helpers.DateTimeHelper;
import com.phamngoc.sofusers.Model.ReputationHistory;
import com.phamngoc.sofusers.R;

import java.net.ContentHandler;

public class ReputationHistoryViewHolder extends BaseViewHolder  {

    Context context;

    TextView reputationType;
    TextView reputationChangePoint;
    TextView changeTime;
    TextView post;

    public ReputationHistoryViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
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
                    reputationType.setText(reputationHistory.Type.replace('_', ' '));
                }

                if(reputationHistory.Change >= 0){
                    reputationChangePoint.setTextColor(context.getResources().getColor(R.color.good_change_color));
                    reputationChangePoint.setText("+" + String.valueOf(reputationHistory.Change));
                }
                else{
                    reputationChangePoint.setTextColor(context.getResources().getColor(R.color.bad_change_color));
                    reputationChangePoint.setText(String.valueOf(reputationHistory.Change));
                }



                if(reputationHistory.CreatedAt > 0){
                    String createdDate = DateTimeHelper.GetDateFromTimeStand(reputationHistory.CreatedAt);
                    changeTime.setText(createdDate);
                }

                if(reputationHistory.PostId != null && !reputationHistory.PostId.isEmpty()){
                    post.setText("#" + reputationHistory.PostId);
                }
            }
        }
        catch (Exception e){

        }

    }
}
