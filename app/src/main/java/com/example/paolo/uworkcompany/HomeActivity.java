package com.example.paolo.uworkcompany;

import android.content.Intent;
import android.content.SharedPreferences;
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

        getDisplayName();

/*
        currentUser = findViewById(R.id.currentUser);


        mUser = mAuth.getCurrentUser();
        currentUser.setText(mUser.toString());
*/
        mAuth = FirebaseAuth.getInstance();
    }

    public void findWorker(View view) {
        // TODO: make a function to call new intent
        startActivity(new Intent(this, RequestActivity.class));
        finish();
    }

    public void logout(View view){
        // Logout the current user
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    private void getDisplayName(){
        SharedPreferences preferences = getSharedPreferences(SignUpActivity.CHAT_PREF, MODE_PRIVATE);
        String user = preferences.getString(SignUpActivity.DISPLAY_NAME_KEY, null);
        currentUser.setText(user);
        if(currentUser  == null) currentUser.setText("Anonymous");

    }
}
