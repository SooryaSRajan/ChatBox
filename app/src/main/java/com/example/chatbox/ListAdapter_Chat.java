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

import static com.example.chatbox.Constants.CHATS;
import static com.example.chatbox.Constants.NAME;
import static com.example.chatbox.Constants.TIME_STAMP;
import static com.example.chatbox.Constants.USER_NAME;


public class ListAdapter_Chat extends BaseAdapter {

    public ArrayList<HashMap> list;
    Activity activity;
    int layout;

    public ListAdapter_Chat(Activity activity, ArrayList<HashMap> list) {
        super();
        this.activity = activity;
        this.list = list;
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

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        HashMap map = list.get(position);

        convertView = inflater.inflate(R.layout.chat_room_user, null);
        holder = new ViewHolder();
        holder.txtFirst = convertView.findViewById(R.id.user_name_list);
        convertView.setTag(holder);


        holder.txtFirst.setText(map.get(NAME).toString());

        return convertView;
    }
}
