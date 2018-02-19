package com.example.hvn15.firebaseapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mStatusField;
    private Button mRegisterBtn;
    private Button mLogoutBtn;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mEmailField = (EditText) findViewById(R.id.email_field);
        mPasswordField = (EditText) findViewById(R.id.password_field);
        mRegisterBtn = (Button) findViewById(R.id.register_button);
        mLogoutBtn = (Button) findViewById(R.id.logout_button);
        mLoginBtn = (Button) findViewById(R.id.login_button);
        mStatusField = (EditText) findViewById(R.id.status_field);

        mAuth = FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View view){
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });
        mLogoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signOut();
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            mStatusField.setText("Could not create account, please try again");
                            updateUI(null);
                        }
                    }
                });
    }
    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            mStatusField.setText("login failed");
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusField.setText("login failed");
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mStatusField.setText("user is logged in");

            findViewById(R.id.email_field).setVisibility(View.GONE);
            findViewById(R.id.password_field).setVisibility(View.GONE);
            findViewById(R.id.register_button).setVisibility(View.GONE);
            findViewById(R.id.login_button).setVisibility(View.GONE);
        } else {
            mStatusField.setText("user is not logged in");

            findViewById(R.id.email_field).setVisibility(View.VISIBLE);
            findViewById(R.id.password_field).setVisibility(View.VISIBLE);
            findViewById(R.id.register_button).setVisibility(View.VISIBLE);
            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
        }
    }
    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }
    public void goToMain(View view){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
}
