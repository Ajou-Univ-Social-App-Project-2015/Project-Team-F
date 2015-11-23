package com.sap.team_f.cook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KyungTack on 2015-11-19.
 */
public class itemAdapter extends BaseAdapter {
    Context con;
    LayoutInflater inflacter;
    private ArrayList<Item> arI = null;
    int layout;

    public itemAdapter(Context context, int alayout, ArrayList<Item> aarI){
        con = context;
        inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arI=aarI;
        layout=alayout;
    }

    public class ViewHolder{
        ImageView img;
        TextView title;
        TextView like;
    }

    @Override
    public int getCount() {
        return arI.size();
    }

    @Override
    public Object getItem(int position) {
        return arI.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null) {
            holder = new ViewHolder();
            convertView = inflacter.inflate(layout, parent, false);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.like = (TextView) convertView.findViewById(R.id.like);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        Log.v("URL", arI.get(position).getImage()); // URL 로그 찍어보기
        ImageLoaderTask imageLoaderTask = new ImageLoaderTask(holder.img,arI.get(position).getImage());
        imageLoaderTask.execute();

        holder.title.setText(arI.get(position).getName());

        holder.like.setText(Integer.toString(arI.get(position).getLike()));

        return convertView;
    }
}
