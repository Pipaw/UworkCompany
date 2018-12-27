package com.example.paolo.uworkcompany;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    //Adding member variables
    private AutoCompleteTextView mEmailText;
    private EditText mPasswordText;
    private TextView mTextView;
    private Button mButton;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Connecting member variable to the assets
        mEmailText = findViewById(R.id.loginEmailText);
        mPasswordText = findViewById(R.id.loginPasswordText);
        mTextView = findViewById(R.id.signUpText);
        mButton = findViewById(R.id.loginButton);


        //Register textview onClick method
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                finish();
                startActivity(mIntent);
            }
        });
        //Authentication Initialization
        mAuth = FirebaseAuth.getInstance();
    }
    //Click and other Events

    public void login(View view){
        attemptLogin();
    }



    //Own functionality

    private void attemptLogin(){

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if(email.equals("") || password.equals(""))return;
        Toast.makeText(this, "Login in progress...", Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("UworkComapny", "signInWithEmail() Complete "+ task.isSuccessful());
                if(!task.isSuccessful()){
                    Log.d("UworkCompany", "Problem Signing In " + task.getException());
                    showDialogError("There was a Problem signing in");
                }else {
                    HomeActivity();
                }
            }
        });
    }

    //Home Activity Intent
    private void HomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }

    private void showDialogError(String message){
        new AlertDialog.Builder(this).setTitle("Error").setMessage(message).setPositiveButton
                (android.R.string.ok, null).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}
