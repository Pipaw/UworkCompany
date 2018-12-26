package com.example.paolo.uworkcompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    //Adding member variables
    private AutoCompleteTextView mEmailText;
    private EditText mPasswordText;
    private TextView mTextView;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailText = findViewById(R.id.loginEmailText);
        mPasswordText = findViewById(R.id.loginPasswordText);
        mTextView = findViewById(R.id.signUpText);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                finish();
                startActivity(mIntent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

}
