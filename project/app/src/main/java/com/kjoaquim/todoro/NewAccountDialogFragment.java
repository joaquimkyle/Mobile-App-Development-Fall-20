package com.kjoaquim.todoro;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class NewAccountDialogFragment extends DialogFragment {
    private Toolbar toolbar;
    private TextView emailView;
    private TextView passwordView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.new_account_dialog, container, false);

        toolbar = root.findViewById(R.id.new_account_dialog_toolbar);

        emailView = root.findViewById(R.id.emailField);
        passwordView = root.findViewById(R.id.passwordField);

        return root;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("Create Account");
        toolbar.inflateMenu(R.menu.new_data_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            String email = emailView.getText().toString();
            String password = passwordView.getText().toString();
            if(!email.isEmpty() && !password.isEmpty()) {
                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.linkWithCredential(credential)
                        .addOnCompleteListener(getActivity(), task -> {
                            if (task.isSuccessful()) {
                                Log.d("AUTH", "linkWithCredential:success");
                                UserProfileChangeRequest profileChangeRequest =
                                        new UserProfileChangeRequest.Builder()
                                        .setDisplayName(email)
                                        .build();
                                user.updateProfile(profileChangeRequest);

                                Map<String, Object> userData = new HashMap<>();
                                userData.put("uid", user.getUid());
                                String userName = email;
                                userData.put("fullName", userName);
                                FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(user.getUid())
                                        .set(userData)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Firestore", "DocumentSnapshot written.");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w("Firestore", "Error writing document", e);
                                        });
                            } else {
                                Log.w("AUTH", "linkWithCredential:failure", task.getException());
                            }
                        });
                dismiss();
            }
            return true;
        });
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
