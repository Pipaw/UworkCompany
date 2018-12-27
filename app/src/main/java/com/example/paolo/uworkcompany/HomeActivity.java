package com.example.paolo.uworkcompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    TextView currentUser;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentUser = findViewById(R.id.currentUser);
        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();
        currentUser.setText(mUser.toString());

    }

    public void findWorker(View view) {
        // TODO: make a function to call new intent


    }

    public void logout(View view){
        // Logout the current user
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }
}
