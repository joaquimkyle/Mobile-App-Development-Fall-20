package com.kjoaquim.todoro.ui.tasks.subtasks;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kjoaquim.todoro.R;

import com.kjoaquim.todoro.ui.tasks.EditTaskDialogFragment;
import com.kjoaquim.todoro.ui.tasks.Task;
import com.kjoaquim.todoro.ui.tasks.TasksViewHolder;
import com.kjoaquim.todoro.ui.tasks.TasksViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class SubTasksFragment extends DialogFragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference users = db.collection("users");
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirestoreRecyclerAdapter subTasksRecyclerViewAdapter;
    private Toolbar toolbar;
    private TasksViewModel tasksViewModel;
    private String taskName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.subtask_dialog, container, false);

        taskName = getArguments().getString("TASK_NAME");

        Query query = users
                .document(user.getUid())
                .collection("tasks")
                .document(taskName)
                .collection("subtasks");
        FirestoreRecyclerOptions<SubTask> options = new
                FirestoreRecyclerOptions.Builder<SubTask>()
                .setQuery(query, SubTask.class)
                .build();
        subTasksRecyclerViewAdapter = new FirestoreRecyclerAdapter<SubTask, SubTaskViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SubTaskViewHolder holder, int position, @NonNull SubTask model) {
                holder.subTaskName.setText(model.getName());
                holder.subTaskCheck.setChecked(model.isCompleted());
                holder.subTaskCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    this.getItem(position).setCompleted(isChecked);
                    tasksViewModel.setSubTask(taskName, this.getItem(position));
                });
                holder.subTaskName.setOnLongClickListener(v -> { // subtask option menu popup
                    PopupMenu popupMenu = new PopupMenu(getContext(), holder.subTaskName);
                    popupMenu.getMenuInflater()
                            .inflate(R.menu.subtask_options_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(item -> { //handling menu options
                        if (item.getItemId() == R.id.action_edit_subtask) {
                            //TODO: handle edit subtask option
                            final EditText input = new EditText(getActivity());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            input.setText(model.getName());
                            new MaterialAlertDialogBuilder(getContext())
                                    .setTitle("Edit SubTask")
                                    .setView(input)
                                    .setNeutralButton("cancel", (dialog, which) -> {
                                        dialog.cancel();
                                    })
                                    .setPositiveButton("save", (dialog, which) -> {
                                        tasksViewModel.renameSubTask(taskName, model, input.getText().toString());
                                    })
                                    .show();
                        }
                        else if (item.getItemId() == R.id.action_delete_subtask) {
                            new MaterialAlertDialogBuilder(getContext())
                                    .setMessage("Are you sure you want to delete this subtask?")
                                    .setNegativeButton("cancel", (dialog, which) -> {
                                        dialog.cancel();
                                    })
                                    .setPositiveButton("delete", (dialog, which) -> {
                                        tasksViewModel.deleteSubTask(taskName, model.getName());
                                    })
                                    .show();
                        }
                        return true;
                    });

                    popupMenu.show();
                    return true;
                });
            }

            @NonNull
            @Override
            public SubTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtask, parent, false);
                SubTaskViewHolder subTaskViewHolder = new SubTaskViewHolder(view);
                return subTaskViewHolder;
            }
        };
        RecyclerView subTasksRecycler = root.findViewById(R.id.subtasks_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        subTasksRecycler.setLayoutManager(linearLayoutManager);
        subTasksRecycler.setAdapter(subTasksRecyclerViewAdapter);
        subTasksRecycler.setHasFixedSize(false);

        MaterialAlertDialogBuilder fabDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
        final FloatingActionButton fab = root.findViewById(R.id.subtasks_fab);
        fab.setOnClickListener(v -> {
            //new subtask dialog
            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            fabDialogBuilder.setTitle("New SubTask");
            fabDialogBuilder.setView(input);
            fabDialogBuilder.setNeutralButton("cancel", (dialog, which) -> {
                dialog.cancel();
                });
            fabDialogBuilder.setPositiveButton("save", (dialog, which) -> {
                SubTask subTask = new SubTask(input.getText().toString(), false);
                tasksViewModel.setSubTask(taskName, subTask);
                subTasksRecyclerViewAdapter.notifyDataSetChanged();
                });
            fabDialogBuilder.show();
        });

        toolbar = root.findViewById(R.id.subtask_dialog_toolbar);

        return root;
    }

    public static class SubTaskViewHolder extends RecyclerView.ViewHolder {
        MaterialCheckBox subTaskCheck;
        TextView subTaskName;

        SubTaskViewHolder(View itemView) {
            super(itemView);
            //attach member views to views in the layout
            subTaskCheck = itemView.findViewById(R.id.subtask_check);
            subTaskName = itemView.findViewById(R.id.subtask_name);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(taskName);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL,
                R.style.Theme_MaterialComponents_Light_NoActionBar);
    }

    @Override
    public void onStart() {
        super.onStart();
        subTasksRecyclerViewAdapter.startListening();
        /*Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        subTasksRecyclerViewAdapter.stopListening();
    }
}
