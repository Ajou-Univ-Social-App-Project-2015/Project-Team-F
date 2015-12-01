package com.sap.team_f.cook;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by KyungTack on 2015-11-26.
 */
public class MyRecipeActivity extends ActionBarActivity {
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private ListView mainNavList;
    private String[] navItems = {"나의 찜","나의 레시피","방명록"};

    private ArrayList<Item> data = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myrecipeactivity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("나의 레시피");

        mainNavList = (ListView)findViewById(R.id.myRecipeNavList); // Navigation Drawer 설정
        mainNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        mainNavList.setOnItemClickListener(new DrawerItemClickListener());
       /* mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);*/

        dlDrawer = (DrawerLayout)findViewById(R.id.myrecipe_drawer_layout); // Drawer 버튼
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


        data.addAll(MainActivity.datas);
        for(int i=data.size()-1;i>=0;--i)
        {
            if(!data.get(i).getId().equals(StartActivity.currentUser.getUsername()))
            {
                data.remove(i);
            }
        }
        ListView list = (ListView)findViewById(R.id.recipeList);
        itemAdapter adapter = new itemAdapter(this,R.layout.item,data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyRecipeActivity.this, FoodActivity.class);
                MainActivity.food = data.get(position);
                startActivity(intent);
            }
        });

        Button addBtn = (Button)findViewById(R.id.recipeAddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyRecipeActivity.this,AddRecipeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        data.clear();
        data.addAll(MainActivity.datas);
        for(int i=data.size()-1;i>=0;--i)
        {
            if(!data.get(i).getId().equals(StartActivity.currentUser.getUsername()))
            {
                data.remove(i);
            }
        }
        ListView list = (ListView)findViewById(R.id.recipeList);
        itemAdapter adapter = new itemAdapter(this,R.layout.item,data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyRecipeActivity.this, FoodActivity.class);
                MainActivity.food = data.get(position);
                startActivity(intent);
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
                    Intent intent = new Intent(MyRecipeActivity.this,BookmarkActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    break;
                case 2:
                    intent = new Intent(MyRecipeActivity.this,MessageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            dlDrawer.closeDrawer(mainNavList);
        }
    } // 드로우 리스너 리스트뷰 클릭시

}
