package com.kjoaquim.todoro.ui.tasks.subtasks;

public class SubTask {
    private String mName;
    private boolean mCompleted;

    public SubTask() { }

    public SubTask(String name, Boolean completed) {
        this.mName = name;
        this.mCompleted = completed;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }
}
