package com.kjoaquim.todoro.ui.tasks.subtasks;

public class SubTask {
    private String name;
    private boolean isCompleted;

    SubTask(String name) {
        this.name = name;
        this.isCompleted = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
