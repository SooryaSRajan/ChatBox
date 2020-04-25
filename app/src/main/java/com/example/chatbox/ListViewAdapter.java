package com.example.chatbox;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import static com.example.chatbox.Constants.CHATS;
import static com.example.chatbox.Constants.TIME_STAMP;
import static com.example.chatbox.Constants.USER_NAME;


public class ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap> list;
    Activity activity;

    public ListViewAdapter(Activity activity, ArrayList<HashMap> list) {
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
        TextView txtSecond;
        TextView txtThird;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_user, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.chat_text);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.user_name);
            holder.txtThird = (TextView) convertView.findViewById(R.id.time_stamp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position);
        holder.txtFirst.setText((Integer) map.get(CHATS));
        holder.txtSecond.setText((Integer) map.get(USER_NAME));
        holder.txtThird.setText((Integer) map.get(TIME_STAMP));


        return convertView;
    }
}
