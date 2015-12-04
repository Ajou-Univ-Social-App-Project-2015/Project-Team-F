package com.sap.team_f.cook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by KyungTack on 2015-12-04.
 */
public class SettingActivity extends ActionBarActivity {
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private ListView mainNavList;
    private String[] navItems = {"나의 찜","나의 레시피","방명록","설정"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingactivity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("설정");

        mainNavList = (ListView)findViewById(R.id.settingNavList); // Navigation Drawer 설정
        mainNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        mainNavList.setOnItemClickListener(new DrawerItemClickListener());
       /* mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);*/

        dlDrawer = (DrawerLayout)findViewById(R.id.setting_drawer_layout); // Drawer 버튼
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

        final EditText nickText = (EditText)findViewById(R.id.settingNick);
        nickText.setText(StartActivity.currentUser.getString("Nickname"));
        Button nickBtn = (Button)findViewById(R.id.settingNickBtn);
        nickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = nickText.getText().toString();
                StartActivity.currentUser.put("Nickname", nick);
                StartActivity.currentUser.saveInBackground();
                Toast.makeText(SettingActivity.this,"닉네임이 변경되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        Button passBtn = (Button)findViewById(R.id.settingPassBtn);
        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText currentPass = (EditText)findViewById(R.id.settingCurrentPass);
                EditText changePass = (EditText)findViewById(R.id.settingChangePass);
                EditText changeRePass = (EditText)findViewById(R.id.settingChangeRePass);
                if(!currentPass.getText().toString().equals(StartActivity.password) ||
                        !changePass.getText().toString().equals(changeRePass.getText().toString()))
                {
                    Toast.makeText(SettingActivity.this,"비밀번호가 다릅니다",Toast.LENGTH_SHORT).show();
                    return;
                }
                StartActivity.currentUser.setPassword(changePass.getText().toString());
                StartActivity.currentUser.saveInBackground();
                StartActivity.password = changePass.getText().toString();
                Toast.makeText(SettingActivity.this,"비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        Button removeBtn = (Button)findViewById(R.id.removeBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingActivity.this);
                alert.setTitle("경고창");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StartActivity.currentUser.deleteInBackground();
                        Intent intent = new Intent(SettingActivity.this, StartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        StartActivity.currentUser=null;
                        startActivity(intent);
                        finish();
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setMessage("정말로 회원탈퇴 하시겠습니까?");
                alert.show();
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
                    Intent intent = new Intent(SettingActivity.this,BookmarkActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    intent = new Intent(SettingActivity.this,MyRecipeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    intent = new Intent(SettingActivity.this,MessageActivity.class);
                    intent.putExtra("search",StartActivity.currentUser.getUsername());
                    startActivity(intent);
                    finish();
                    break;
                case 3:
                    break;
            }
            dlDrawer.closeDrawer(mainNavList);
        }
    } // 드로우 리스너 리스트뷰 클릭시
}
