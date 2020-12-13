package com.kjoaquim.todoro.ui.routines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoutinesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RoutinesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the routines screen. Here is where users would be able to " +
                "manage tasks to be completed daily. These could be presented to the user" +
                "in scripted events as the user works through intervals on the Work screen, " +
                "encouraging them to maintain their habits.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}