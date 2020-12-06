package com.kjoaquim.todoro.ui.tasks;

import androidx.lifecycle.ViewModel;


import java.util.ArrayList;
import java.util.List;

public class TasksViewModel extends ViewModel {
    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}