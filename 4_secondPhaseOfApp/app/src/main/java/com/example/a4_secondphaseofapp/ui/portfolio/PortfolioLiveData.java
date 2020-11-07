package com.example.a4_secondphaseofapp.ui.portfolio;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Source;

import java.util.Objects;

public class PortfolioLiveData extends LiveData<PortfolioModel> implements EventListener<DocumentSnapshot> {
    private DocumentReference documentReference;
    private PortfolioModel portfolioDataTemp = new PortfolioModel();
    public MutableLiveData<PortfolioModel> portfolioData = new MutableLiveData<>();
    private ListenerRegistration listenerRegistration = () -> {};

    public PortfolioLiveData(DocumentReference documentReference) {
        this.documentReference = documentReference;
        portfolioData.setValue(new PortfolioModel());

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
            try {
                portfolioDataTemp.setName(Objects.requireNonNull(documentSnapshot.get("name")).toString());
                portfolioDataTemp.setTicker(Objects.requireNonNull(documentSnapshot.get("ticker")).toString());
                portfolioDataTemp.setLogo(Objects.requireNonNull(documentSnapshot.get("logo")).toString());
                portfolioDataTemp.setShareOutstanding(Objects.requireNonNull(documentSnapshot.get("share")).toString());

                portfolioData.setValue(portfolioDataTemp);
            } catch (NullPointerException np) {
                portfolioDataTemp.setName("-");
                portfolioDataTemp.setTicker("-");
                portfolioDataTemp.setLogo("-");
                portfolioDataTemp.setShareOutstanding("-");
            }
        } else {
            Log.d("PortfolioLiveData", "error");
        }
    }
}
