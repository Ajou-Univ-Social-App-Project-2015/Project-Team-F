package com.sap.team_f.cook;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

/**
 * Created by KyungTack on 2015-11-26.
 */
public class MessageActivity extends ActionBarActivity {
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private ParseObject user;

    private ListView mainNavList;
    private String[] navItems = {"나의 찜","나의 레시피","방명록","설정"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessageactivity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("방명록");

        mainNavList = (ListView)findViewById(R.id.messageNavList); // Navigation Drawer 설정
        mainNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        mainNavList.setOnItemClickListener(new DrawerItemClickListener());
       /* mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);*/

        dlDrawer = (DrawerLayout)findViewById(R.id.message_drawer_layout); // Drawer 버튼
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        dlDrawer.setDrawerListener(dtToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("search");

        TextView idText = (TextView)findViewById(R.id.idText);
        idText.setText(id);

        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
            query.whereEqualTo("username", id);
            user = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        final Button replySearchBtn = (Button)findViewById(R.id.myReplySearchBtn);
        replySearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText replySearchText = (EditText)findViewById(R.id.myReplySearchText);
                if(replySearchText.getText().toString().equals("")){
                    Toast.makeText(MessageActivity.this,"검색할 ID를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                String id = replySearchText.getText().toString();
                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
                    query.whereEqualTo("username", id);
                    Log.v("Id", id);
                    Log.v("Id",query.getFirst().toString());
                    user = query.getFirst();
                    Intent intent = new Intent(MessageActivity.this,MessageActivity.class);
                    intent.putExtra("search", user.getString("username"));
                    replySearchText.setText("");
                    startActivity(intent);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(MessageActivity.this, "존재하는 ID를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(user.getJSONArray("Comment").length()>0)
        {
            ListView list = (ListView) findViewById(R.id.myReplyList);
            ReplyAdapter adapter = new ReplyAdapter(this, R.layout.reply, user.getJSONArray("Comment"));
            list.setAdapter(adapter);
        }


        Button replyAddBtn = (Button)findViewById(R.id.myReplyAddBtn);
        replyAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TextView replyText = (TextView) findViewById(R.id.myReplyText);
                if (replyText.getText().toString().equals("")) {
                    Toast.makeText(MessageActivity.this, "덧글을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                final JSONArray replies = user.getJSONArray("Comment");
                JSONArray reply = new JSONArray();
                reply.put(StartActivity.currentUser.getUsername());
                reply.put(StartActivity.currentUser.get("Nickname"));
                reply.put(replyText.getText().toString());
                replies.put(reply);
                replyText.setText("");
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
                query.getInBackground(user.getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject userObj, ParseException e) {
                        if (e == null) {
                            userObj.put("Comment", replies);
                            userObj.saveInBackground();
                            user = userObj;
                            ListView list = (ListView) findViewById(R.id.myReplyList);
                            ReplyAdapter adapter = new ReplyAdapter(MessageActivity.this, R.layout.reply, user.getJSONArray("Comment"));
                            list.setAdapter(adapter);
                        }
                    }
                });
            }
        });

    }

    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState(); }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(dtToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            switch (position) {
                case 0:
                    Intent intent = new Intent(MessageActivity.this,BookmarkActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    intent = new Intent(MessageActivity.this,MyRecipeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    break;
                case 3:
                    intent = new Intent(MessageActivity.this,SettingActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            dlDrawer.closeDrawer(mainNavList);
        }
    } // 드로우 리스너 리스트뷰 클릭시
}
