package com.example.chatbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.chatbox.Constants.EMAIL_STATE;
import static com.example.chatbox.Constants.NAME;
import static com.example.chatbox.Constants.NAME_STATE;
import static com.example.chatbox.Constants.NUMBER_STATE;
import static com.example.chatbox.Constants.OTHER_ID;
import static com.example.chatbox.Constants.OTHER_NAME;
import static com.example.chatbox.Constants.SHARED_PREFERENCE;
import static com.example.chatbox.Constants.USER_ID;
import static com.example.chatbox.Constants.USER_NAME;

public class ChatRoom_Inflate extends AppCompatActivity {
   public ArrayList<String> list = new ArrayList();
   public ArrayList<String> list1 = new ArrayList();
   private ValueEventListener eventListener;
   private FirebaseDatabase database = FirebaseDatabase.getInstance();
   private DatabaseReference mRef;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room__inflate);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final ListView list_view = findViewById(R.id.chat_room_list_view);
        mRef = database.getReference();
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    list1.clear();
                    for (DataSnapshot ds : dataSnapshot.child("users").getChildren()) {
                        String name = ds.child("NAME").getValue(String.class);
                        if (!ds.getKey().contains(auth.getUid())) {
                            //Toast.makeText(ChatRoom_Inflate.this, ds.child("phone").getValue(String.class), Toast.LENGTH_SHORT).show();
                            if(contactExists(getApplicationContext(), ds.child("phone").getValue(String.class).trim())){
                                list.add(name);
                                list1.add(ds.getKey().trim());
                            }

                      }
                        else{
                            USER_NAME = name;
                        }
                }
                    adapter = new ArrayAdapter<>(ChatRoom_Inflate.this,R.layout.chat_room_user,R.id.user_name_list, list);
                    list_view.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.addValueEventListener(eventListener);

        try {
            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                    Intent intent = new Intent(ChatRoom_Inflate.this,MainActivity.class);
                    intent.putExtra(OTHER_ID,list1.get(position));
                    OTHER_NAME = list.get(position);
                    startActivity(intent);
                }
            });
        }
        catch (Exception e){
            Log.e("ItemClick",e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_room_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout").setMessage("Are you sure you want to logout of your account?")
                    .setPositiveButton("Yes?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            mAuth.signOut();

                            SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = preferences.edit();
                            myEdit.remove(EMAIL_STATE);
                            myEdit.remove(NAME_STATE);
                            myEdit.remove(NUMBER_STATE);
                            myEdit.commit();
                            Intent intent = new Intent(ChatRoom_Inflate.this, email_register.class);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("No",null).create();
            builder.show();
        }

        else if (item.getItemId() == R.id.change_name) {
            Intent intent = new Intent(ChatRoom_Inflate.this, Change_Name.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean contactExists(Context context, String number) {
        if (number != null) {
            ContentResolver cr = context.getContentResolver();
            Cursor curContacts = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            while (curContacts.moveToNext()) {
                String contactNumber = curContacts.getString(curContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (number.contains(contactNumber)) {
                    Toast.makeText(context, number, Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure You Want To Exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("No",null).create();
        builder.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRef.removeEventListener(eventListener);
    }
}


