package com.kjoaquim.todoro.ui.tasks;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kjoaquim.todoro.R;
import com.kjoaquim.todoro.ui.tasks.subtasks.SubTasksFragment;


public class TasksFragment extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference users = db.collection("users");

    private FirestoreRecyclerAdapter tasksRecyclerViewAdapter;
    private TasksViewModel tasksViewModel;
    private Fragment thisFrag = this;


    final int NEW_TASK = 0;
    final int EDIT_TASK = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = users
                .document(user.getUid())
                .collection("tasks");
        FirestoreRecyclerOptions<Task> options = new
                FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();
        tasksRecyclerViewAdapter = new FirestoreRecyclerAdapter<Task, TasksViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TasksViewHolder holder, int position, @NonNull Task model) {
                int cardColor = getCardColor(model.getPriority());
                holder.taskName.setText(model.getName());
                holder.taskDescription.setText(model.getDescription());
                holder.colorCard.setCardBackgroundColor(cardColor);
                holder.taskCard.setOnClickListener(v -> { //handle press on cardview
                    //open subtasks
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Bundle bundle = new Bundle(1);
                    bundle.putString("TASK_NAME", model.getName());
                    SubTasksFragment subTasksFragment = new SubTasksFragment();
                    subTasksFragment.setArguments(bundle);
                    subTasksFragment.show(activity.getSupportFragmentManager(), "subtasks");
                });
                holder.taskOptions.setOnClickListener(v -> { //task option menu popup
                    PopupMenu popupMenu = new PopupMenu(getContext(), holder.taskOptions);
                    popupMenu.getMenuInflater()
                            .inflate(R.menu.task_options_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(item -> { //handling menu options
                        if (item.getItemId() == R.id.action_edit_task) {
                            EditTaskDialogFragment editTaskDialogFragment = new EditTaskDialogFragment(model);
                            editTaskDialogFragment.setTargetFragment(thisFrag, EDIT_TASK);
                            editTaskDialogFragment.show(thisFrag.getParentFragmentManager(), "edit_task");
                        }
                        else if (item.getItemId() == R.id.action_delete_task) {
                            new MaterialAlertDialogBuilder(getContext())
                                    .setMessage("Are you sure you want to delete this task?")
                                    .setNegativeButton("cancel", (dialog, which) -> {
                                        dialog.cancel();
                                    })
                                    .setPositiveButton("delete", (dialog, which) -> {
                                        tasksViewModel.deleteTask(model.getName());
                                    })
                                    .show();
                        }
                        return true;
                    });

                    popupMenu.show();
                });
            }

            @NonNull
            @Override
            public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
                TasksViewHolder tasksViewHolder = new TasksViewHolder(view);
                return tasksViewHolder;
            }
        };
        RecyclerView tasksRecycler = root.findViewById(R.id.tasks_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        tasksRecycler.setLayoutManager(linearLayoutManager);
        tasksRecycler.setAdapter(tasksRecyclerViewAdapter);
        tasksRecycler.setHasFixedSize(false);

        final FloatingActionButton fab = root.findViewById(R.id.tasks_fab);
        fab.setOnClickListener(v -> {
            NewTaskDialogFragment newTaskDialogFragment = new NewTaskDialogFragment();
            newTaskDialogFragment.setTargetFragment(this, NEW_TASK);
            newTaskDialogFragment.show(getParentFragmentManager(), "new_task");
        });

        return root;
    }

    public int getCardColor(String taskPriority) {
        if(taskPriority.equals("Low")) {
            return ResourcesCompat.getColor(getResources(), R.color.low_priority, null);
        }
        else if(taskPriority.equals("Medium")) {
            return ResourcesCompat.getColor(getResources(), R.color.medium_priority, null);
        }
        else if(taskPriority.equals("High")) {
            return ResourcesCompat.getColor(getResources(), R.color.high_priority, null);
        }
        else return ResourcesCompat.getColor(getResources(), R.color.white, null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String taskName = data.getStringExtra(
                    NewTaskDialogFragment.TASK_NAME);
            String taskDesc = data.getStringExtra(
                    NewTaskDialogFragment.TASK_DESC);
            String taskPriority = data.getStringExtra(
                    NewTaskDialogFragment.TASK_PRIORITY);

            if (requestCode == NEW_TASK) {
                Task newTask = new Task(taskName, taskDesc, taskPriority);
                tasksViewModel.setTask(newTask);
            }
            else if (requestCode == EDIT_TASK) {
                String oldName = data.getStringExtra(
                        EditTaskDialogFragment.OLD_NAME);
                Task newTask = new Task(taskName, taskDesc, taskPriority);
                tasksViewModel.remakeTask(oldName, newTask);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        tasksRecyclerViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        tasksRecyclerViewAdapter.stopListening();
    }

}