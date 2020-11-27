package com.kjoaquim.todoro.ui.tasks.subtasks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kjoaquim.todoro.R;

import com.kjoaquim.todoro.ui.tasks.TasksViewModel;

import org.jetbrains.annotations.NotNull;


public class SubTasksFragment extends DialogFragment {

    private Toolbar toolbar;
    private TasksViewModel tasksViewModel;
    private RecyclerView subTasksRecycler;
    private SubTasksRecyclerViewAdapter subTasksRecyclerViewAdapter;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private int taskPosition;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.subtask_dialog, container, false);

        taskPosition = getArguments().getInt("TASK_POSITION");
        subTasksRecycler = root.findViewById(R.id.subtasks_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        subTasksRecycler.setLayoutManager(linearLayoutManager);
        subTasksRecyclerViewAdapter = new SubTasksRecyclerViewAdapter(tasksViewModel, taskPosition);
        subTasksRecycler.setAdapter(subTasksRecyclerViewAdapter);
        subTasksRecycler.setHasFixedSize(false);
        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());

        toolbar = root.findViewById(R.id.subtask_dialog_toolbar);

        final FloatingActionButton fab = root.findViewById(R.id.subtasks_fab);
        fab.setOnClickListener(v -> {
            //new subtask dialog
            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            materialAlertDialogBuilder.setTitle("New SubTask");
            materialAlertDialogBuilder.setView(input);
            materialAlertDialogBuilder.setNeutralButton("cancel", (dialog, which) -> {
                dialog.cancel();
                });
            materialAlertDialogBuilder.setPositiveButton("save", (dialog, which) -> {
                SubTask subTask = new SubTask(input.getText().toString());
                subTasksRecyclerViewAdapter.addSubTask(subTask);
                });
            materialAlertDialogBuilder.show();
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(tasksViewModel.getTasks().get(taskPosition).getName());
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

}
