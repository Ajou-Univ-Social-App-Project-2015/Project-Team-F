package com.sap.team_f.cook;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;


public class SearchActivity extends ActionBarActivity{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private ListView SearchNavList;
    private String[] navItems = {"나의 찜","나의 레시피","방명록"};

    private boolean isMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity);

        isMember = getIntent().getBooleanExtra("isMember",true);
        String search = getIntent().getStringExtra("Search");
        EditText searchText = (EditText)findViewById(R.id.searchSearchText);
        searchText.setText(search);
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

        Button searchBtn = (Button)findViewById(R.id.searchSearchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText)findViewById(R.id.searchSearchText);
                String search = searchText.getText().toString();
                if(search==null)
                {
                    Toast.makeText(SearchActivity.this,"검색 단어를 입력해 주세요",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                    intent.putExtra("Search", search);
                    intent.putExtra("isMember",isMember);
                    startActivity(intent);
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("오늘은 내가 요리사");

        SearchNavList = (ListView)findViewById(R.id.searchNavList);
        SearchNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        SearchNavList.setOnItemClickListener(new DrawerItemClickListener());
       /* mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);*/

        dlDrawer = (DrawerLayout)findViewById(R.id.search_drawer_layout);
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
                        Intent intent = new Intent(SearchActivity.this, SignupActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            dlDrawer.closeDrawer(SearchNavList);
        }
    }
}
