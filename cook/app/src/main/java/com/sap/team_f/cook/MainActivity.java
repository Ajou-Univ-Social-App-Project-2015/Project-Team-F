package com.sap.team_f.cook;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private CharSequence mTitle;
    private ListView mainNavList;
    private FrameLayout flContainer;
    private String[] navItems = {"나의 찜","나의 레시피","방명록"};

    private boolean isMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        isMember = getIntent().getBooleanExtra("isMember",true);

        if(isMember)
        {
            navItems = new String[3];
            navItems[0]="나의 찜";
            navItems[1]="나의 레시피";
            navItems[2]="방명록";
        }
        else
        {
            navItems = new String[1];
            navItems[0]="회원가입";
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("오늘은 내가 요리사");

        TabHost tabHost = (TabHost) findViewById(R.id.mainTabhost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1");
        tabSpec.setContent(R.id.mainAll);
        tabSpec.setIndicator("전체"); // tab의 lable 값
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab2");
        tabSpec.setContent(R.id.mainKor);
        tabSpec.setIndicator("한식");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab3");
        tabSpec.setContent(R.id.mainJap);
        tabSpec.setIndicator("일식");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("text4");
        tabSpec.setContent(R.id.mainChn);
        tabSpec.setIndicator("중식");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("text5");
        tabSpec.setContent(R.id.mainEng);
        tabSpec.setIndicator("양식");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("text6");
        tabSpec.setContent(R.id.mainEtc);
        tabSpec.setIndicator("기타");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);
        mainNavList = (ListView)findViewById(R.id.mainNavList);
        flContainer = (FrameLayout)findViewById(R.id.mainContainer);
        mainNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        mainNavList.setOnItemClickListener(new DrawerItemClickListener());
       /* mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);*/
        mTitle = getTitle();

        dlDrawer = (DrawerLayout)findViewById(R.id.main_drawer_layout);
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

        // Set up the drawer.
        /*mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));*/
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
                    if(!isMember)
                    {
                        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    mTitle = getString(R.string.title_section1);
                    break;
                case 1:
                    mTitle = getString(R.string.title_section2);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section3);
                    break;
            }
            dlDrawer.closeDrawer(mainNavList);
        }
    }
}
