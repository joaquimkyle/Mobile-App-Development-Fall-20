package com.kjoaquim.todoro.ui.tasks;

import com.kjoaquim.todoro.ui.tasks.subtasks.SubTask;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private String name;
    private String description;
    private String priority;
    private List<SubTask> subTaskList;

    Task(String name, String description, String priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.subTaskList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }

}
