package com.example.chatbox;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.chatbox.Constants.SHARED_PREFERENCE;
import static com.example.chatbox.Constants.SHARED_PREFERENCE_NAME;
import static com.example.chatbox.Constants.USER_NAME;

public class Setup extends AppCompatActivity {

    private static final String TAG = "Error In Main";
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        setContentView(R.layout.content_setup);

        try {
            if (preferences.contains(SHARED_PREFERENCE_NAME)) {
                USER_NAME = preferences.getString(SHARED_PREFERENCE_NAME, "Empty");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e){
            Log.e(TAG, "onCreate: If condition error");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        Button user_button = findViewById(R.id.submit_button);
        final TextView alert = findViewById(R.id.alert);
        final EditText edit = findViewById(R.id.user_name);
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        final SharedPreferences.Editor myEdit = preferences.edit();

        user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edit.getText().toString();
                if(name.length()==0){
                    Toast.makeText(Setup.this, "Name Field Empty!", Toast.LENGTH_SHORT).show();
                    alert.setText("*");
                }
                else {
                    if(name.length()<6){
                        Toast.makeText(Setup.this, "Name Length Less Than 6 characters", Toast.LENGTH_SHORT).show();
                        alert.setText("*");
                    }
                    else{
                        alert.setText(" ");
                        Toast.makeText(Setup.this, "Your name has been registered", Toast.LENGTH_SHORT).show();
                        name = name.trim();
                        USER_NAME = name;
                        myEdit.putString(SHARED_PREFERENCE_NAME,name);
                        myEdit.commit();
                        Intent intent = new Intent(Setup.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
