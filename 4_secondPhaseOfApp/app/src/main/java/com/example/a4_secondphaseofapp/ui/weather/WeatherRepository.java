package com.example.a4_secondphaseofapp.ui.weather;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class WeatherRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public WeatherLiveData getFireStoreLiveData(String path) {
        DocumentReference documentReference = firebaseFirestore
                .collection("weatherData")
                .document(path);

        return new WeatherLiveData(documentReference);
    }

    public DocumentReference getDocument(String path) {
        return firebaseFirestore.collection("weatherData").document(path);
    }

}
