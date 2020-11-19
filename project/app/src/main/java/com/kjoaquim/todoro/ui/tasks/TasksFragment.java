package com.kjoaquim.todoro.ui.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kjoaquim.todoro.R;

import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;
    private RecyclerView tasksRecycler;
    private TasksRecyclerViewAdapter tasksRecyclerViewAdapter;
    private List<Task> tasks;

    final int REQUEST_CODE = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        tasks = new ArrayList<>();
        tasksRecycler = (RecyclerView)root.findViewById(R.id.tasks_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        tasksRecycler.setLayoutManager(linearLayoutManager);
        tasksRecyclerViewAdapter = new TasksRecyclerViewAdapter(tasks);
        tasksRecycler.setAdapter(tasksRecyclerViewAdapter);
        tasksRecycler.setHasFixedSize(false);

        final FloatingActionButton fab = (FloatingActionButton)root.findViewById(R.id.tasks_fab);
        fab.setOnClickListener(v -> {
            NewTaskDialogFragment newTaskDialogFragment = new NewTaskDialogFragment();
            newTaskDialogFragment.setTargetFragment(this, REQUEST_CODE);
            newTaskDialogFragment.show(getParentFragmentManager(), "new_task");
        });

        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String taskName = data.getStringExtra(
                    NewTaskDialogFragment.TASK_NAME);
            String taskDesc = data.getStringExtra(
                    NewTaskDialogFragment.TASK_DESC);
            String taskPriority = data.getStringExtra(
                    NewTaskDialogFragment.TASK_PRIORITY);

            Task newTask = new Task(taskName, taskDesc, taskPriority);
            tasksRecyclerViewAdapter.addCard(newTask);
            tasksRecyclerViewAdapter.notifyItemInserted(tasksRecyclerViewAdapter.tasks.size() - 1);
        }
    }

}