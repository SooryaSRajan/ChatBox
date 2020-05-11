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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.chatbox.Constants.EMAIL_STATE;
import static com.example.chatbox.Constants.NAME_STATE;
import static com.example.chatbox.Constants.NUMBER_STATE;
import static com.example.chatbox.Constants.SHARED_PREFERENCE;
import static com.example.chatbox.Constants.USER_NAME;

public class email_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_email_login);
        Button button = findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText password_entry = findViewById(R.id.password);
                final EditText email_entry = findViewById(R.id.email_id_login);

                String email_string = email_entry.getText().toString().trim();
                String password_string = password_entry.getText().toString().trim();


                if(email_string.isEmpty()){
                    Toast.makeText(email_login.this, "Email Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(password_string.isEmpty()){
                    Toast.makeText(email_login.this, "Password Empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginFirebase(email_string.trim(),password_string.trim(),0);
                }
            }
        });
    }

public void loginFirebase(final String email, final String password, final int flag) {
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            Toast.makeText(email_login.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();

            if(flag==0) {
                SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = preferences.edit();
                myEdit.putString(EMAIL_STATE, email);
                myEdit.putString(NUMBER_STATE, email);
                myEdit.putString(NAME_STATE, email);
                myEdit.commit();
            }
            Intent intent = new Intent(email_login.this, ChatRoom_Inflate.class);
            startActivity(intent);
            finish();
         }
        }
    });

}
}
