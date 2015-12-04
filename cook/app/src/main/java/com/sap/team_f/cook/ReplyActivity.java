package com.sap.team_f.cook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by KyungTack on 2015-12-01.
 */
public class ReplyActivity extends ActionBarActivity {
    Item food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replyactivity);

        food = MainActivity.food;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(food.getName());


        if(food.getReply().length()>0)
        {
            ListView list = (ListView) findViewById(R.id.replyList);
            ReplyAdapter adapter = new ReplyAdapter(this, R.layout.reply, food.getReply());
            list.setAdapter(adapter);
        }

        Button replyAddBtn = (Button)findViewById(R.id.replyAddBtn);
        replyAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StartActivity.currentUser == null) {
                    Toast.makeText(ReplyActivity.this, "로그인 해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                TextView replyText = (TextView) findViewById(R.id.replyText);
                if (replyText.getText().toString().equals("")) {
                    Toast.makeText(ReplyActivity.this, "덧글을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONArray replies = food.getReply();
                JSONArray reply = new JSONArray();
                reply.put(StartActivity.currentUser.getUsername());
                reply.put(StartActivity.currentUser.get("Nickname"));
                reply.put(replyText.getText().toString());
                replies.put(reply);
                replyText.setText("");
                food.setReply(replies);
                ParseQuery<ParseObject> query;
                if (food.getType().equals("한식"))
                    query = ParseQuery.getQuery("Korean");
                else if (food.getType().equals("중식"))
                    query = ParseQuery.getQuery("China");
                else if (food.getType().equals("일식"))
                    query = ParseQuery.getQuery("Japan");
                else if (food.getType().equals("양식"))
                    query = ParseQuery.getQuery("English");
                else
                    query = ParseQuery.getQuery("Etc");
                // Retrieve the object by id
                query.getInBackground(food.getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject foodObj, ParseException e) {
                        if (e == null) {
                            foodObj.put("Reply", food.getReply());
                            foodObj.saveInBackground();
                            food = new Item(foodObj, food.getType());
                            MainActivity.load();
                            ListView list = (ListView) findViewById(R.id.replyList);
                            ReplyAdapter adapter = new ReplyAdapter(ReplyActivity.this, R.layout.reply,food.getReply());
                            list.setAdapter(adapter);
                        }
                    }
                });
            }
        });
    }
}
