package com.example.chatbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.chatbox.Constants.*;

public class MainActivity extends AppCompatActivity {
    EditText chat_text = findViewById(R.id.text_Box);
    //public ArrayList<HashMap> list;
    //ListView list_view = findViewById(R.id.List_ViewChatBox);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chat_text.setText(USER_NAME);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inflater,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_name) {
            Intent intent = new Intent(MainActivity.this, Change_Name.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageButton send = findViewById(R.id.send_button);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //   USER_CHAT=chat_text.getText().toString();
                   // if(!USER_CHAT.isEmpty()){
                    //populator(1);

                    //}
            }
        });
    }
/*
    public void populator(int a){
        list = new ArrayList<>();

        HashMap temp = new HashMap();

        if(a==1){
            temp.put(CHATS,USER_CHAT);
            temp.put(NAME,USER_NAME);
            temp.put(TIME_STAMP,"11:20");
            list.add(temp);
            ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, list);
            list_view.setAdapter(adapter);
        }

    }*/
}
