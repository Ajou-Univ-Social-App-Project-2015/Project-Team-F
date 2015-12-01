package com.sap.team_f.cook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by KyungTack on 2015-12-01.
 */
public class ReplyAdapter extends BaseAdapter {
    Context con;
    LayoutInflater inflacter;
    private JSONArray arI = null;
    int layout;

    public ReplyAdapter(Context context, int alayout, JSONArray aarI){
        con = context;
        inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arI=aarI;
        layout=alayout;
    }
    @Override
    public int getCount() {
        return arI.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return arI.getJSONArray(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = inflacter.inflate(layout, parent, false);
        }
        JSONArray reply = null;
        try {
            reply = arI.getJSONArray(position);
            TextView nickname = (TextView)convertView.findViewById(R.id.nickname);
            nickname.setText(reply.getString(1));
            TextView replytext = (TextView)convertView.findViewById(R.id.reply);
            replytext.setText(reply.getString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
