package com.example.chatbox;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.concurrent.TimeUnit;

import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.chatbox.Constants.NAME_STATE;
import static com.example.chatbox.Constants.SHARED_PREFERENCE;
import static com.example.chatbox.Constants.SHARED_PREFERENCE_NAME;
import static com.example.chatbox.Constants.USER_NAME;

public class Setup extends AppCompatActivity {

    private static final String TAG = "Error In Main";
    public String name;
    public boolean flag = false;
    public int a =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        try{
            if(preferences.contains(NAME_STATE)){
                Intent intent = new Intent(Setup.this, ChatRoom_Inflate.class);
                startActivity(intent);
                finish();
            }
        }
        catch (Exception e){}
        setContentView(R.layout.content_setup);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final Button user_button = findViewById(R.id.submit_button);
        final TextView alert = findViewById(R.id.alert);
        final EditText edit = findViewById(R.id.user_name);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = edit.getText().toString().trim();
                Toast.makeText(Setup.this,USER_NAME, Toast.LENGTH_SHORT).show();

                if(USER_NAME.contains("not_null")){
                    Toast.makeText(Setup.this, "Name Already Exists!", Toast.LENGTH_SHORT).show();
                }

                else if (name.length() == 0) {
                    Toast.makeText(Setup.this, "Name Field Empty!", Toast.LENGTH_SHORT).show();
                    alert.setText("*");
                } else if (name.length() < 6) {
                    Toast.makeText(Setup.this, "Name Length Less Than 6 characters", Toast.LENGTH_SHORT).show();
                    alert.setText("*");
                }

                else{

                    alert.setText(" ");
                    Toast.makeText(Setup.this, "Your name has been registered", Toast.LENGTH_SHORT).show();
                    name = name.trim();
                    ref.child("users").child(user.getUid()).child("NAME").setValue(name);
                    USER_NAME = name;

                    SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = preferences.edit();
                    myEdit.putString(NAME_STATE, name);
                    myEdit.commit();

                    Intent intent = new Intent(Setup.this, ChatRoom_Inflate.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    }

