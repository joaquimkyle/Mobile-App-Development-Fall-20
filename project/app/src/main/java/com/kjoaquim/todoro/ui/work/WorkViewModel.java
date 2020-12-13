package com.kjoaquim.todoro.ui.work;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WorkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the work screen. This is where users would be able to work on" +
                "tasks for set intervals of time. These intervals could be configured in each" +
                "user's preferences, where a few presets would be provided and custom profiles" +
                "could be created.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}