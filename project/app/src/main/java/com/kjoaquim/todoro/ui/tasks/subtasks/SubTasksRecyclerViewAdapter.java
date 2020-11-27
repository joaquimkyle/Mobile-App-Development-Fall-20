package com.kjoaquim.todoro.ui.tasks.subtasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.kjoaquim.todoro.R;
import com.kjoaquim.todoro.ui.tasks.TasksViewModel;


public class SubTasksRecyclerViewAdapter extends RecyclerView.Adapter<SubTasksRecyclerViewAdapter.SubTaskViewHolder> {

    private TasksViewModel tasksViewModel;
    private int taskPosition;

    SubTasksRecyclerViewAdapter(TasksViewModel tasksViewModel, int taskPosition){
        this.tasksViewModel = tasksViewModel;
        this.taskPosition = taskPosition;
    }

    public static class SubTaskViewHolder extends RecyclerView.ViewHolder {
        MaterialCheckBox subTask;

        SubTaskViewHolder(View itemView) {
            super(itemView);
            //attach member views to views in the layout
            subTask = itemView.findViewById(R.id.subtask_check);
        }
    }

        public void addSubTask(SubTask subTask) {
            tasksViewModel.getTasks().get(taskPosition).getSubTaskList().add(subTask);
        }

        @NonNull
        @Override
        public SubTasksRecyclerViewAdapter.SubTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtask, parent, false);
            SubTasksRecyclerViewAdapter.SubTaskViewHolder subTaskViewHolder = new SubTasksRecyclerViewAdapter.SubTaskViewHolder(view);
            return subTaskViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SubTasksRecyclerViewAdapter.SubTaskViewHolder subTaskViewHolder, int position) {
            subTaskViewHolder.subTask.setText(tasksViewModel.getTasks().get(taskPosition).getSubTaskList().get(position).getName());
            subTaskViewHolder.subTask.setChecked(tasksViewModel.getTasks().get(taskPosition).getSubTaskList().get(position).isCompleted());
            subTaskViewHolder.subTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tasksViewModel.getTasks().get(taskPosition).getSubTaskList().get(position).setCompleted(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return tasksViewModel.getTasks().get(taskPosition).getSubTaskList().size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
}
