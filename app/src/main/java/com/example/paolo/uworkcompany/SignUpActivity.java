package com.example.paolo.uworkcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class SignUpActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmail, mUsername;
    private EditText mPasword,mConfirmPass;

    private FirebaseAuth mAuth;
    public static final String CHAT_PREF = "ChatPrefs", DISPLAY_NAME_KEY = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmail = findViewById(R.id.signUpEmail);
        mUsername = findViewById(R.id.signUpUsername);
        mPasword = findViewById(R.id.signUpPassword);
        mConfirmPass = findViewById(R.id.signUpConfirmPass);

        // Grab an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
    }


    //own functionality
    public void signUp(View view){
        attemptRegistration();
    }


    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmail.setError(null);
        mPasword.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmail.getText().toString();
        String password = mPasword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasword.setError(getString(R.string.error_invalid_password));
            focusView = mPasword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Call create FirebaseUser() here
            createFirebaseUser();
        }
    }
    //Checking if it is a valid email
    private boolean isEmailValid(String email){
        return email.contains("@");
    }

    //Checking the users information validation
    private boolean isPasswordValid(String password){
        String confirmPass = mConfirmPass.getText().toString();
        return confirmPass.equals(password) && password.length() > 6;
    }

    //Create new Firebase user;
    private void createFirebaseUser(){
        String email = mEmail.getText().toString();
        String password = mPasword.getText().toString();
        //Creating user method
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>
                () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Open HomeActivity after successfully signing up
                    Log.d("uWork", "createUser " + task.isSuccessful());
                    saveDisplayName();
                    loginActivityIntent();
                }else{
                    //Error messages If the creation of user failed
                    /*
                    FirebaseAuthException e = (FirebaseAuthException)task.getException();
                    Toast.makeText(SignUpActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    */
                    Log.d("uWork", "User Creation Failed");
                    Log.d("uWork", mPasword.getText().toString());
                    showDialogError("Registration attempt failed!");
                }
            }
        });

    }

    //Save Display name to shared preferences
    private void saveDisplayName(){
        String displayName = mUsername.getText().toString();
        SharedPreferences mShared = getSharedPreferences(CHAT_PREF, 0);
        mShared.edit().putString(DISPLAY_NAME_KEY, displayName).apply();

    }

    //Showing dialog errors
    private void showDialogError(String message){
        new AlertDialog.Builder(this).setTitle("Error").setMessage(message).setPositiveButton(android.R.string.ok, null).show();
    }
    //Opening new Activity(HomeActivity)
    private void loginActivityIntent(){
        Intent mIntent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(mIntent);
    }
}
