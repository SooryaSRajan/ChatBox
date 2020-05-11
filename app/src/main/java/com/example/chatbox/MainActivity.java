package com.example.chatbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.example.chatbox.Constants.CHATS;
import static com.example.chatbox.Constants.NAME;
import static com.example.chatbox.Constants.OTHER_ID;
import static com.example.chatbox.Constants.OTHER_NAME;
import static com.example.chatbox.Constants.TIME_STAMP;
import static com.example.chatbox.Constants.USER_ID;
import static com.example.chatbox.Constants.USER_NAME;

public class MainActivity extends AppCompatActivity {
  public ArrayList<HashMap> list = new ArrayList<>();

    ListView list_view;
    public String user_message;
    public String time;
    private ListViewAdapter list_view_adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private ValueEventListener eventListener;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String other_id;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        other_id = intent.getStringExtra(OTHER_ID).trim();

         eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.child("Messages").getChildren()) {
                        String userId = ds.getKey();
                        String chats = ds.child(CHATS).getValue(String.class);
                        String time = ds.child(TIME_STAMP).getValue(String.class);
                        String id_mine = ds.child(USER_ID).getValue(String.class);
                        String id_other = ds.child(OTHER_ID).getValue(String.class);
                        if(id_mine.contains(mAuth.getUid()) && id_mine.contains(other_id)){
                        try {
                                HashMap map = new HashMap();
                                map.put(CHATS, chats);
                                map.put(TIME_STAMP, time);
                                map.put(OTHER_ID, id_other);

                                list.add(map);
                                list_view = findViewById(R.id.List_ViewChatBox);
                                list_view_adapter = new ListViewAdapter(MainActivity.this, list, R.layout.listview);
                                list_view.setAdapter(list_view_adapter);
                                Log.d("TAG", userId);
                                list_view_adapter.notifyDataSetChanged();
                                scrollMyListViewToBottom();
                            } catch (Exception e) {
                                Log.e("", "onDataChange: ", e);
                            }
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "No messages so far!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        ref.addValueEventListener(eventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageButton send = findViewById(R.id.send_button);
        final EditText chat_text = findViewById(R.id.text_Box);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_message = chat_text.getText().toString();
                    if(!user_message.isEmpty()){
                        list_view = findViewById(R.id.List_ViewChatBox);
                        populator(1);
                        list_view_adapter = new ListViewAdapter(MainActivity.this,list,R.layout.listview_user);
                        list_view.setAdapter(list_view_adapter);
                        chat_text.setText("");
                    }
            }
        });

    }

    public void populator(int a){
        if(a==1){

            HashMap temp = new HashMap();
            //temp.put(NAME,USER_NAME);
            temp.put(CHATS,user_message);
            getTime();
            temp.put(TIME_STAMP,time);
            temp.put(USER_ID, mAuth.getUid()+"_"+other_id);
            temp.put(OTHER_ID,mAuth.getUid());
            list.add(temp);
            writeToFirebase(temp);
        }

    }
    public void getTime(){
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        time=dateFormat.format(currentTime);
    }

    public void writeToFirebase(HashMap temp) {
        ref.child("Messages").push().setValue(temp, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(MainActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    private void scrollMyListViewToBottom() {
        list_view.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                list_view.setSelection(list_view_adapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ChatRoom_Inflate.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ref.removeEventListener(eventListener);
    }
}
