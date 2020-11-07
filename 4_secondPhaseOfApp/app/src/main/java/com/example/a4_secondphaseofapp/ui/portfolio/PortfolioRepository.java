package com.example.a4_secondphaseofapp.ui.portfolio;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class PortfolioRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();

    public PortfolioLiveData getFireStoreLiveData(String path) {
        firebaseFirestore.setFirestoreSettings(settings);
        DocumentReference documentReference = firebaseFirestore
                .collection("portfolioData")
                .document(path);

        return new PortfolioLiveData(documentReference);
    }

    public DocumentReference getDocument(String path) {
        return firebaseFirestore.collection("portfolioData").document(path);
    }
}
