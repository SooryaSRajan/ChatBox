package com.example.chatbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.chatbox.Constants.EMAIL_STATE;
import static com.example.chatbox.Constants.PASSWORD_STATE;
import static com.example.chatbox.Constants.SHARED_PREFERENCE;
import static com.example.chatbox.Constants.SHARED_PREFERENCE_NAME;

public class email_create extends AppCompatActivity {
    private String email_string, password1_string, password2_string;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_create);

        Button submit = findViewById(R.id.create);
        final EditText email = findViewById(R.id.email_id);
        final EditText password1 = findViewById(R.id.password_1);
        final EditText password2 = findViewById(R.id.password_2);
        final TextView alert = findViewById(R.id.alert_email);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_string = email.getText().toString().trim();
                password1_string = password1.getText().toString();
                password2_string = password2.getText().toString();

                if (email_string.isEmpty()) {
                    Toast.makeText(email_create.this, "Email Empty!", Toast.LENGTH_SHORT).show();
                    alert.setText("*");
                } else if (password1_string.isEmpty() || password2_string.isEmpty()) {
                    Toast.makeText(email_create.this, "Password fields empty", Toast.LENGTH_SHORT).show();
                } else if (!password2_string.contains(password1_string)) {
                    Toast.makeText(email_create.this, "Incorrect Passwords in Fields", Toast.LENGTH_SHORT).show();
                } else {
                    registerEmail();
                }
            }
        });
    }

    public void registerEmail() {
        final SharedPreferences preferences = this.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mAuth.createUserWithEmailAndPassword(email_string, password1_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(email_create.this, "Email Registration Complete", Toast.LENGTH_SHORT).show();

                    try {
                        SharedPreferences.Editor myEdit = preferences.edit();
                        myEdit.putString(EMAIL_STATE, email_string);
                        myEdit.putString(PASSWORD_STATE, password1_string);

                        myEdit.commit();
                    } catch (Exception e) {
                        Log.e("shared_pref_tag", e.toString());
                    }
                    Intent intent = new Intent(email_create.this, Setup.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                        Toast.makeText(email_create.this, "Email Already Exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
