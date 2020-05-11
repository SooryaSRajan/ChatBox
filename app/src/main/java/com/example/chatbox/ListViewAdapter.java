package com.example.chatbox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.chatbox.Constants.CHATS;
import static com.example.chatbox.Constants.OTHER_ID;
import static com.example.chatbox.Constants.OTHER_NAME;
import static com.example.chatbox.Constants.TIME_STAMP;
import static com.example.chatbox.Constants.USER_NAME;


public class ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap> list;
    Activity activity;
    int layout;

    public ListViewAdapter(Activity activity, ArrayList<HashMap> list, int layout ) {
        super();
        this.activity = activity;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap map = list.get(position);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

            if(mAuth.getUid().contains(map.get(OTHER_ID).toString()))
            convertView = inflater.inflate(R.layout.listview_user, null);

            else{
                convertView = inflater.inflate(R.layout.listview, null);
            }
            holder = new ViewHolder();
            holder.txtFirst = convertView.findViewById(R.id.chat_text);
            holder.txtSecond = convertView.findViewById(R.id.user_name);
            holder.txtThird = convertView.findViewById(R.id.time_stamp);
            convertView.setTag(holder);


        holder.txtFirst.setText(map.get(CHATS).toString());
        if(mAuth.getUid().contains(map.get(OTHER_ID).toString())) {
            holder.txtSecond.setText(USER_NAME);
        }
        else{
            holder.txtSecond.setText(OTHER_NAME);
        }
        holder.txtThird.setText( map.get(TIME_STAMP).toString());

        return convertView;
    }
}
