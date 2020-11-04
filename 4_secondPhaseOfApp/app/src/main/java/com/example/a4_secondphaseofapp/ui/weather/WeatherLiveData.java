package com.example.a4_secondphaseofapp.ui.weather;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

public class WeatherLiveData extends LiveData<WeatherModel> implements EventListener<DocumentSnapshot> {

    private DocumentReference documentReference;
    private WeatherModel weatherDataTemp = new WeatherModel();
    public MutableLiveData<WeatherModel> weatherData = new MutableLiveData<>();
    private ListenerRegistration listenerRegistration = () -> {};

    public WeatherLiveData(DocumentReference documentReference) {
        this.documentReference = documentReference;
        weatherData.setValue(new WeatherModel());

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    onEvent(document, (FirebaseFirestoreException) task.getException());
                    Log.d("firestore", "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d("firestore", "No such document");
                }
            } else {
                Log.d("firestore", "get failed with ", task.getException());
            }
        });
    }

    @Override
    protected void onActive() {
        listenerRegistration = documentReference.addSnapshotListener(this);
        super.onActive();
    }

    @Override
    protected void onInactive() {
        listenerRegistration.remove();
        super.onInactive();
    }

    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if(documentSnapshot != null && documentSnapshot.exists()) {
            weatherDataTemp.setLon(Double.parseDouble(Objects.requireNonNull(documentSnapshot.get("lon")).toString()));
            weatherDataTemp.setLat(Double.parseDouble(Objects.requireNonNull(documentSnapshot.get("lat")).toString()));
            weatherDataTemp.setTemp(Objects.requireNonNull(documentSnapshot.get("temp")).toString());
            weatherDataTemp.setIcon(Objects.requireNonNull(documentSnapshot.get("icon")).toString());
            weatherDataTemp.setName(Objects.requireNonNull(documentSnapshot.get("name")).toString());

            weatherData.setValue(weatherDataTemp);
        } else {
            Log.d("WeatherRepository", "error");
        }
    }
}
