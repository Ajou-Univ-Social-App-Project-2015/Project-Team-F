package com.sap.team_f.cook;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by KyungTack on 2015-11-30.
 */
public class RecipeAdapter extends ArrayAdapter<Recipe> {
    RecipeAdapter(Context context, ArrayList<Recipe> index){
        super(context, R.layout.recipe,index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe, null);
        }

        TextView text = (TextView)convertView.findViewById(R.id.number);
        text.setText(Integer.toString(getItem(position).getNum()));
        Log.v("index", Integer.toString(getItem(position).getNum()));
        final EditText editText = (EditText)convertView.findViewById(R.id.recipe);
        final int pos = position;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                getItem(pos).setRecipe(editText.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return convertView;
    }
}
