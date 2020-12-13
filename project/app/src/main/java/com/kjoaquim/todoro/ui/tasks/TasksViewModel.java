package com.kjoaquim.todoro.ui.tasks;

import android.util.Log;

import androidx.lifecycle.ViewModel;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kjoaquim.todoro.ui.tasks.subtasks.SubTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TasksViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference users = db.collection("users");
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Task getTask(String taskName) {
        AtomicReference<Task> userTask = new AtomicReference<>(new Task());
        users.document(user.getUid())
                .collection("tasks")
                .document(taskName)
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       DocumentSnapshot document = task.getResult();
                       userTask.set(new Task(
                               document.get("name").toString(),
                               document.get("description").toString(),
                               document.get("priority").toString()
                       ));
                   } else {
                       Log.d("DB", "Error getting task by name: ", task.getException());
                   }
                });
        return userTask.get();
    }

    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        users.document(user.getUid())
                .collection("tasks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Task userTask = new Task(
                                    document.get("name").toString(),
                                    document.get("description").toString(),
                                    document.get("priority").toString()
                            );
                            taskList.add(userTask);
                        }
                    } else {
                        Log.d("DB", "Error getting user tasks: ", task.getException());
                    }
                });

        return taskList;
    }

    public void setTask(Task task) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", task.getName());
        data.put("description", task.getDescription());
        data.put("priority", task.getPriority());
        users.document(user.getUid())
                .collection("tasks")
                .document(task.getName())
                .set(data);
    }

    public void remakeTask(String taskName, Task newTask) {
        List<SubTask> subTasks = getSubTasks(taskName);
        deleteTask(taskName);
        setTask(newTask);
        for (SubTask subTask : subTasks) {
            setSubTask(newTask.getName(), subTask);
        }
    }

    public void deleteTask(String taskName) {
        users.document(user.getUid())
                .collection("tasks")
                .document(taskName)
                .collection("subtasks")
                .get()
                .addOnCompleteListener(task -> { // delete all subtasks
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()) {
                           deleteSubTask(taskName, document.get("name").toString());
                       }
                   } else {
                       Log.d("DB", "Error getting user subtasks: ", task.getException());
                   }
                });

        users.document(user.getUid())
                .collection("tasks")
                .document(taskName)
                .delete();
    }

    public List<SubTask> getSubTasks(String taskName) {
        List<SubTask> subTaskList = new ArrayList<>();
        users.document(user.getUid())
                .collection("tasks")
                .document(taskName)
                .collection("subtasks")
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()) {
                           SubTask subTask = new SubTask(
                                   document.get("name").toString(),
                                   (Boolean)document.get("completed"));
                           subTaskList.add(subTask);
                       }
                   } else {
                       Log.d("DB", "Error getting user subtasks: ", task.getException());
                   }
                });

        return subTaskList;
    }

    public void setSubTask(String taskName, SubTask subTask) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", subTask.getName());
        data.put("completed", subTask.isCompleted());
        users.document(user.getUid())
                .collection("tasks")
                .document(taskName)
                .collection("subtasks")
                .document(subTask.getName())
                .set(data);
    }

    public void renameSubTask(String taskName, SubTask subTask, String newName) {
        //delete subtask, edit name
        deleteSubTask(taskName, subTask.getName());
        subTask.setName(newName);
        //make new subtask
        setSubTask(taskName, subTask);
    }

    public void deleteSubTask(String taskName, String subTaskName) {
        users.document(user.getUid())
                .collection("tasks")
                .document(taskName)
                .collection("subtasks")
                .document(subTaskName)
                .delete();
    }
}