package com.kjoaquim.todoro.ui.tasks;

public class Task {

    private String mName;
    private String mDescription;
    private String mPriority;

    public Task() { }

    public Task(String name, String description, String priority) {
        this.mName = name;
        this.mDescription = description;
        this.mPriority = priority;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getPriority() {
        return mPriority;
    }

    public void setPriority(String priority) {
        this.mPriority = priority;
    }

}
