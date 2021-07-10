package com.ssb.ssbapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssb.ssbapp.R;

public class DailyReportViewHolder extends RecyclerView.ViewHolder {

    public  TextView name , got, gave, traygot, traygave ;

    public DailyReportViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        got = itemView.findViewById(R.id.got);
        gave= itemView.findViewById(R.id.gave);
        traygot= itemView.findViewById(R.id.traygot);
        traygave = itemView.findViewById(R.id.traygave);

    }
}
