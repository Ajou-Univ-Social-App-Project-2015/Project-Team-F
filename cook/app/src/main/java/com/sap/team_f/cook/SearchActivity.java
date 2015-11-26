package com.sap.team_f.cook;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private ListView SearchNavList;
    private String[] navItems = {"나의 찜","나의 레시피","방명록"};
    private ArrayList<Item> datas = new ArrayList<Item>(); // parse.com에서 읽어온 object들을 저장할 List
    private RadioGroup radio;
    private String[] word;
    private boolean isMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity);

        isMember = getIntent().getBooleanExtra("isMember",true);
        String search = getIntent().getStringExtra("Search");
        word = search.split(" ");
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
                EditText searchText = (EditText) findViewById(R.id.searchSearchText);
                String search = searchText.getText().toString();
                if (search == null) {
                    Toast.makeText(SearchActivity.this, "검색 단어를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                    intent.putExtra("Search", search);
                    intent.putExtra("isMember", isMember);
                    startActivity(intent);
                }
            }
        });

        Button refreshBtn = (Button)findViewById(R.id.searchRefreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
                if(radio.getCheckedRadioButtonId()==R.id.mainAllRec)
                    onSortLike(datas);
                else
                    onSortDay(datas);
                ListView list = (ListView)findViewById(R.id.searchList);
                itemAdapter adapter = new itemAdapter(SearchActivity.this,R.layout.item,datas);
                list.setAdapter(adapter);
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
        datas = MainActivity.datas;
        for(int i=0;i<word.length;++i) {
            for (int j = datas.size() - 1; j >= 0; --j) {
                Log.v("Name",datas.get(j).getName());
                if (Search(word[i], datas.get(j)) == false) {
                    datas.remove(j);
                }
            }
        }
        onSortLike(datas);
        ListView list = (ListView)findViewById(R.id.searchList);
        itemAdapter adapter = new itemAdapter(this,R.layout.item,datas);
        list.setAdapter(adapter);

        radio=(RadioGroup)findViewById(R.id.searchRadioGroup);
        radio.setOnCheckedChangeListener(this);
    }

    void load()
    {
        MainActivity.datas.clear();
        MainActivity.kordatas.clear();
        MainActivity.japdatas.clear();
        MainActivity.chndatas.clear();
        MainActivity.engdatas.clear();
        MainActivity.etcdatas.clear();
        try {
            ParseQuery<ParseObject> query;
            query = ParseQuery.getQuery("Korean"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                MainActivity.kordatas.add(new Item(object));
                MainActivity.datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
            }
            query = ParseQuery.getQuery("China"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                MainActivity.chndatas.add(new Item(object));
                MainActivity.datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
            }
            query = ParseQuery.getQuery("Japan"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                MainActivity.japdatas.add(new Item(object));
                MainActivity.datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
            }
            query = ParseQuery.getQuery("English"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                MainActivity.engdatas.add(new Item(object));
                MainActivity.datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
            }
            query = ParseQuery.getQuery("Etc"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                MainActivity.etcdatas.add(new Item(object));
                MainActivity.datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
            }
            datas = MainActivity.datas;
            for(int i=0;i<word.length;++i) {
                for (int j = datas.size() - 1; j >= 0; --j) {
                    Log.v("Name",datas.get(j).getName());
                    if (Search(word[i], datas.get(j)) == false) {
                        datas.remove(j);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onSortLike(ArrayList<Item> data) // 좋아요 순으로 정렬
    {
        for(int i = 0; i<data.size()-1;++i)
        {
            for(int j=i+1;j<data.size();++j)
            {
                if (data.get(i).getLike() < data.get(j).getLike())
                {
                    Item temp = data.get(i);
                    data.set(i, data.get(j));
                    data.set(j, temp);
                }
            }
        }
    }

    public void onSortDay(ArrayList<Item> data) // 최순 순으로 정렬
    {
        for(int i = 0; i<data.size()-1;++i)
        {
            for(int j=i+1;j<data.size();++j)
            {
                if (data.get(i).getCreatedAt().before(data.get(j).getCreatedAt()))
                {
                    Item temp = data.get(i);
                    data.set(i, data.get(j));
                    data.set(j, temp);
                }
            }
        }
    }

    boolean Search(String str, Item item)
    {
        JSONArray arr;
        arr = item.getMaterial();
        Log.v("Search",str);
        Log.v("Material",arr.toString());
        for(int i=0;i<arr.length();++i)
        {
            try {
                Log.v("Material",arr.getJSONArray(i).get(0).toString());
                if(arr.getJSONArray(i).get(0).toString().equals(str))
                {
                    Log.v("vvv","true");
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        arr = item.getSubMaterial();
        for(int i=0;i<arr.length();++i)
        {
            try {
                Log.v("subMaterial",arr.getJSONArray(i).get(0).toString());
                if(arr.getJSONArray(i).get(0).toString().equals(str))
                {
                    Log.v("vvv","true");
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.searchAllRec)
        {
            onSortLike(datas); // 좋아요 순으로 정렬
        }
        else if(checkedId==R.id.searchAllNew)
        {
            onSortDay(datas);
        }
        ListView list = (ListView)findViewById(R.id.searchList);
        itemAdapter adapter = new itemAdapter(this,R.layout.item,datas);
        list.setAdapter(adapter);
    } // 라디오버튼 바뀔때

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
                    }
                    else
                    {
                        Intent intent = new Intent(SearchActivity.this,BookmarkActivity.class);
                        startActivity(intent);
                    }
                    break;
                case 1:
                    Intent intent = new Intent(SearchActivity.this,MyRecipeActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    break;
            }
            dlDrawer.closeDrawer(SearchNavList);
        }
    }
}
