package com.example.chatbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.chatbox.Constants.EMAIL_STATE;
import static com.example.chatbox.Constants.SHARED_PREFERENCE;

public class email_register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        try {
            if (preferences.contains(EMAIL_STATE)) {
                Intent intent = new Intent(this, OTP_Number.class);
                startActivity(intent);
                finish();
            }
        }
        catch (Exception e){
            Log.e("Shared preference register", "onCreate: If condition error");
        }


        setContentView(R.layout.activity_email_register);
        Button signup = findViewById(R.id.signup);
        Button login = findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(email_register.this, email_create.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(email_register.this, email_login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
