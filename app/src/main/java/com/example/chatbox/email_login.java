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
import static com.example.chatbox.Constants.PASSWORD_STATE;
import static com.example.chatbox.Constants.SHARED_PREFERENCE;
import static com.example.chatbox.Constants.USER_NAME;

public class email_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        try {
            if (preferences.contains(EMAIL_STATE) && preferences.contains(PASSWORD_STATE)) {
                String email = preferences.getString(EMAIL_STATE, "null");
                String password = preferences.getString(PASSWORD_STATE, "null");
                loginFirebase(email.trim(), password.trim(),1);
            }

        }
        catch (Exception e){
            Log.e("Shared preference register", "onCreate: If condition error");
        }
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
                myEdit.putString(PASSWORD_STATE, password);

                myEdit.commit();
            }

            else{
                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase database =FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();

                ValueEventListener listener2 = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.child("users").getChildren()){
                            if (ds.getKey().contains(mAuth.getUid())){
                                USER_NAME=ds.child("NAME").getValue(String.class);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                ref.addValueEventListener(listener2);
            }

            Intent intent = new Intent(email_login.this, ChatRoom_Inflate.class);
            startActivity(intent);
            finish();
         }
        }
    });

}
}
