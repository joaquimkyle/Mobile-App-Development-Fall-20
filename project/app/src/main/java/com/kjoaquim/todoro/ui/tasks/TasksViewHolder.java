package com.kjoaquim.todoro.ui.tasks;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kjoaquim.todoro.R;

public class TasksViewHolder extends RecyclerView.ViewHolder {
    CardView taskCard;
    CardView colorCard;
    ImageButton taskOptions;
    TextView taskName;
    TextView taskDescription;

    TasksViewHolder(View itemView) {
        super(itemView);
        //attach member views to views in the card layout
        taskCard = itemView.findViewById(R.id.task_card);
        colorCard = itemView.findViewById(R.id.color_card);
        taskOptions = itemView.findViewById(R.id.task_options);
        taskName = itemView.findViewById(R.id.task_name);
        taskDescription = itemView.findViewById(R.id.task_description);
    }
}
