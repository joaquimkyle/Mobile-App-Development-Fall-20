package com.kjoaquim.todoro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kjoaquim.todoro.ui.tasks.NewTaskDialogFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 5;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_tasks, R.id.navigation_work, R.id.navigation_routines)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) { //if no user is signed in, launch sign-in activity
            List<IdpConfig> providers = Arrays.asList(
                    new IdpConfig.EmailBuilder().build(),
                    new IdpConfig.GoogleBuilder().build(),
                    new IdpConfig.AnonymousBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_SIGN_IN);
        } else { //user is signed in
            //update UI with user data here
        }
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUserMetadata metadata = user.getMetadata();
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                    //new user, create user document
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("uid", user.getUid());
                    String userName = user.getDisplayName();
                    userData.put("fullName", (userName != null) ? userName : "Anon");

                    db.collection("users").document(user.getUid())
                            .set(userData)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "DocumentSnapshot written.");
                            })
                            .addOnFailureListener(e -> {
                                Log.w("Firestore", "Error writing document", e);
                            });
                } else {
                    //existing user
                }
            } else {
                //Sign in failed
                if (response == null) {
                    //Manually cancelled
                    showSnackbar("Sign-in cancelled.");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("No network connection.");
                    return;
                }

                showSnackbar(response.getError().getMessage());
                Log.e("SIGN-IN", "Sign-in error: ", response.getError());
            }
        }
    }

    public void showSnackbar(String message) {
        View contextView = findViewById(R.id.nav_view);
        Snackbar.make(contextView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user.isAnonymous()) { //user has guest account
                new MaterialAlertDialogBuilder(this)
                        .setMessage("Would you like to create an account? If you sign out now," +
                                " all of your data will be lost.")
                        .setNeutralButton("sign out", (dialog, which) -> {
                            signOut();
                        })
                        .setPositiveButton("create account", (dialog, which) -> {
                            NewAccountDialogFragment newAccountDialogFragment =
                                    new NewAccountDialogFragment();
                            newAccountDialogFragment.show(getSupportFragmentManager(), "new_account");
                        })
                        .show();
            } else {
                signOut();
            }

            return true;
        }

        return false;
    }

    public void signOut() {
        AuthUI.getInstance() //sign out
                .signOut(this)
                .addOnCompleteListener(task -> {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                });
    }
}