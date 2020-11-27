package com.kjoaquim.todoro.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.kjoaquim.todoro.R;
import com.kjoaquim.todoro.ui.tasks.subtasks.SubTasksFragment;


public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskViewHolder>{

    TasksViewModel tasksViewModel;

    TasksRecyclerViewAdapter(TasksViewModel tasksViewModel){
        this.tasksViewModel = tasksViewModel;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView taskCard;
        TextView taskName;
        TextView taskDescription;
        Button subTaskButton;

        TaskViewHolder(View itemView) {
            super(itemView);
            //attach member views to views in the card layout
            taskCard = itemView.findViewById(R.id.task_card);
            taskName = itemView.findViewById(R.id.task_name);
            taskDescription = itemView.findViewById(R.id.task_description);
            subTaskButton = itemView.findViewById(R.id.subtask_button);
        }
    }

    public void addCard(Task task) {
        tasksViewModel.getTasks().add(task);
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
        taskViewHolder.taskName.setText(tasksViewModel.getTasks().get(position).getName());
        taskViewHolder.taskDescription.setText(tasksViewModel.getTasks().get(position).getDescription());
        taskViewHolder.subTaskButton.setOnClickListener(v -> {
            //open subtasks
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Bundle bundle = new Bundle(1);
            bundle.putInt("TASK_POSITION", position);
            SubTasksFragment subTasksFragment = new SubTasksFragment();
            subTasksFragment.setArguments(bundle);
            subTasksFragment.show(activity.getSupportFragmentManager(), "subtasks");
        });
    }

    @Override
    public int getItemCount() {
        return tasksViewModel.getTasks().size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
