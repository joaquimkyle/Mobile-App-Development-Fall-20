package com.kjoaquim.todoro.ui.tasks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.kjoaquim.todoro.R;

import org.jetbrains.annotations.NotNull;

public class NewTaskDialogFragment extends DialogFragment {

    private Toolbar toolbar;
    private TextView taskName;
    private TextView taskDesc;
    private TextView taskPriority;

    public static String TASK_NAME = "TASK_NAME";
    public static String TASK_DESC = "TASK_DESC";
    public static String TASK_PRIORITY = "TASK_PRIORITY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_task_dialog, container, false);

        String[] priorities = new String[] {"Low", "Medium", "High"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.drop_list_item,
                        priorities);

        AutoCompleteTextView editTextFilledExposedDropdown =
                root.findViewById(R.id.taskPriorityAutoComplete);
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setText(priorities[0], false);

        toolbar = root.findViewById(R.id.new_task_dialog_toolbar);

        taskName = root.findViewById(R.id.taskNameField);
        taskDesc = root.findViewById(R.id.taskDescField);
        taskPriority = root.findViewById(R.id.taskPriorityAutoComplete);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("New Task");
        toolbar.inflateMenu(R.menu.new_task_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if(!taskName.getText().toString().isEmpty()) {
                sendData();
                dismiss();
            }
            return true;
        });
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

    private void sendData() {
        Intent intent = new Intent();
        intent.putExtra(TASK_NAME, taskName.getText().toString());
        intent.putExtra(TASK_DESC, taskDesc.getText().toString());
        intent.putExtra(TASK_PRIORITY, taskPriority.getText().toString());
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

}
