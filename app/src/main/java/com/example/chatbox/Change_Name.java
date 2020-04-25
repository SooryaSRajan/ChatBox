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

public class Change_Name extends AppCompatActivity {

    private static final String TAG = "Error In Main";
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setup);
        TextView title = findViewById(R.id.user_name_text);
        title.setText("Change Your User Name");
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
                    Toast.makeText(Change_Name.this, "Name Field Empty!", Toast.LENGTH_SHORT).show();
                    alert.setText("*");
                }
                else {
                    if(name.length()<6){
                        Toast.makeText(Change_Name.this, "Name Length Less Than 6 characters", Toast.LENGTH_SHORT).show();
                        alert.setText("*");
                    }
                    else{
                        alert.setText(" ");
                        Toast.makeText(Change_Name.this, "Your name has been changed", Toast.LENGTH_SHORT).show();
                        name = name.trim();
                        USER_NAME = name;
                        myEdit.putString(SHARED_PREFERENCE_NAME,name);
                        myEdit.commit();
                        Intent intent = new Intent(Change_Name.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
