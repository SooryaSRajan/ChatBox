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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import static com.example.chatbox.Constants.EMAIL_STATE;
import static com.example.chatbox.Constants.NUMBER_STATE;
import static com.example.chatbox.Constants.SHARED_PREFERENCE;

public class OTP_Number extends AppCompatActivity {
    Button btnGenerateOTP, btnSignIn;
    EditText etPhoneNumber, etOTP;
    String phoneNumber, otp;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        try{
            if(preferences.contains(NUMBER_STATE)){
                Intent intent = new Intent(OTP_Number.this, Setup.class);
                startActivity(intent);
                finish();
            }
        }
        catch (Exception e){

        }
        setContentView(R.layout.activity_otp__number);
        findViews();
        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = etPhoneNumber.getText().toString().trim();
                Toast.makeText(OTP_Number.this, phoneNumber, Toast.LENGTH_SHORT).show();
                if (phoneNumber.isEmpty()) {
                    Toast.makeText(OTP_Number.this, "Phone Number Empty!", Toast.LENGTH_SHORT).show();
                } else if (phoneNumber.length()>10 || phoneNumber.length()<10) {
                    Toast.makeText(OTP_Number.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OTP_Number.this, "Valid no", Toast.LENGTH_SHORT).show();
                    send_OTP();
                }
            }
        });
    }

    private void findViews() {
        btnGenerateOTP=findViewById(R.id.send_otp);
        btnSignIn=findViewById(R.id.verify);
        etPhoneNumber=findViewById(R.id.phone_number);
        etOTP=findViewById(R.id.otp);
    }

    private void send_OTP() {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91"+phoneNumber,          // Phone number to verify
                    20,                // Timeout duration
                    TimeUnit.SECONDS,    // Unit of timeout
                    this,         // Activity (for callback binding)

                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            verificationCode = phoneAuthCredential.getSmsCode();
                            Toast.makeText(OTP_Number.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(OTP_Number.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                            Log.e("OTP fail", e.toString() );
                        }

                        @Override
                        public void onCodeSent(final String s, final PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            btnSignIn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    otp = etOTP.getText().toString().trim();
                                    if (otp.isEmpty()) {
                                        Toast.makeText(OTP_Number.this, "OTP field Empty!", Toast.LENGTH_SHORT).show();
                                    } else if (otp.length() != 6) {
                                        Toast.makeText(OTP_Number.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, otp);
                                        signInWithPhoneAuthCredential(credential);
                                    }

                                }
                            });
                        }
                    });
        }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.child("users").child(mAuth.getUid()).child("phone").setValue(phoneNumber);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = preferences.edit();
        myEdit.putString(NUMBER_STATE, phoneNumber);
        myEdit.commit();

        Intent intent = new Intent(OTP_Number.this, Setup.class);
        startActivity(intent);
        finish();
        /*mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("OTP", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            mAuth.signOut();
                            // ...
                            Toast.makeText(OTP_Number.this, user.getUid(), Toast.LENGTH_SHORT).show();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e("OTP", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });*/
    }
    }


