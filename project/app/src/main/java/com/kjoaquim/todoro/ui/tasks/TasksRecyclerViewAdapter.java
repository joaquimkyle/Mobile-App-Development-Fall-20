package com.kjoaquim.todoro.ui.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.kjoaquim.todoro.R;

import org.w3c.dom.Text;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskViewHolder>{

    List<Task> tasks;

    TasksRecyclerViewAdapter(List<Task> tasks){
        this.tasks = tasks;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView taskCard;
        TextView taskName;
        TextView taskDescription;

        TaskViewHolder(View itemView) {
            super(itemView);
            //attach member views to views in the card layout
            taskCard = (CardView)itemView.findViewById(R.id.task_card);
            taskName = (TextView)itemView.findViewById(R.id.task_name);
            taskDescription = (TextView)itemView.findViewById(R.id.task_description);
        }
    }

    public void addCard(Task task) {
        tasks.add(task);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position) {
        taskViewHolder.taskName.setText(tasks.get(position).getName());
        taskViewHolder.taskDescription.setText(tasks.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
