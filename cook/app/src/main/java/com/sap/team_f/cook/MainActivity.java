package com.sap.team_f.cook;

import android.app.Activity;
import android.app.ActivityManager;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private ListView mainNavList;
    private String[] navItems = {"나의 찜","나의 레시피","방명록"};
    private RadioGroup[] radio = new RadioGroup[6];
    private boolean isMember;

    public static ArrayList<Item> datas = new ArrayList<Item>(); // parse.com에서 읽어온 object들을 저장할 List
    public static ArrayList<Item> kordatas = new ArrayList<Item>();
    public static ArrayList<Item> chndatas = new ArrayList<Item>();
    public static ArrayList<Item> japdatas = new ArrayList<Item>();
    public static ArrayList<Item> engdatas = new ArrayList<Item>();
    public static ArrayList<Item> etcdatas = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        isMember = getIntent().getBooleanExtra("isMember",false);

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

        Button searchBtn = (Button)findViewById(R.id.mainSearchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText) findViewById(R.id.mainSearchText);
                String search = searchText.getText().toString();
                if (search == null) {
                    Toast.makeText(MainActivity.this, "검색 단어를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("Search", search);
                    intent.putExtra("isMember", isMember);
                    startActivity(intent);
                }
            }
        });

        Button refreshBtn = (Button)findViewById(R.id.mainRefreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
                sortList();
                ListView list; // TabHost에 List마다 추천수 높은 3개 넣어줌
                list = (ListView)findViewById(R.id.list_all);
                setList(list, datas);
                list = (ListView)findViewById(R.id.list_korean);
                setList(list,kordatas);
                list = (ListView)findViewById(R.id.list_chinese);
                setList(list,chndatas);
                list = (ListView)findViewById(R.id.list_japanese);
                setList(list,japdatas);
                list = (ListView)findViewById(R.id.list_american);
                setList(list,engdatas);
                list = (ListView)findViewById(R.id.list_etc);
                setList(list,etcdatas);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("오늘은 내가 요리사");

        TabHost tabHost = (TabHost) findViewById(R.id.mainTabhost); // TabHost 설정
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

        tabHost.setCurrentTab(0); // 기본 TabHost 설정

        mainNavList = (ListView)findViewById(R.id.mainNavList); // Navigation Drawer 설정
        mainNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        mainNavList.setOnItemClickListener(new DrawerItemClickListener());
       /* mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);*/

        dlDrawer = (DrawerLayout)findViewById(R.id.main_drawer_layout); // Drawer 버튼
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

        radio[0]=(RadioGroup)findViewById(R.id.mainAllRadio);
        radio[1]=(RadioGroup)findViewById(R.id.mainKorRadio);
        radio[2]=(RadioGroup)findViewById(R.id.mainJapRadio);
        radio[3]=(RadioGroup)findViewById(R.id.mainChnRadio);
        radio[4]=(RadioGroup)findViewById(R.id.mainEngRadio);
        radio[5]=(RadioGroup)findViewById(R.id.mainEtcRadio);

        for(int i=0;i<6;++i)
        {
            radio[i].setOnCheckedChangeListener(this);
        }

        load(); // parse데이터 로드
        sortList();
        //Log.v("vvv", datas.get(0).get("Material").toString());
        ListView list; // TabHost에 List마다 추천수 높은 3개 넣어줌
        list = (ListView)findViewById(R.id.list_all);
        setList(list, datas);
        list = (ListView)findViewById(R.id.list_korean);
        setList(list,kordatas);
        list = (ListView)findViewById(R.id.list_chinese);
        setList(list,chndatas);
        list = (ListView)findViewById(R.id.list_japanese);
        setList(list,japdatas);
        list = (ListView)findViewById(R.id.list_american);
        setList(list,engdatas);
        list = (ListView)findViewById(R.id.list_etc);
        setList(list,etcdatas);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sortList();
        ListView list; // TabHost에 List마다 추천수 높은 3개 넣어줌
        list = (ListView)findViewById(R.id.list_all);
        setList(list, datas);
        list = (ListView)findViewById(R.id.list_korean);
        setList(list,kordatas);
        list = (ListView)findViewById(R.id.list_chinese);
        setList(list,chndatas);
        list = (ListView)findViewById(R.id.list_japanese);
        setList(list,japdatas);
        list = (ListView)findViewById(R.id.list_american);
        setList(list,engdatas);
        list = (ListView)findViewById(R.id.list_etc);
        setList(list,etcdatas);
    }

    /*@Override
        protected void onResume() {
            super.onResume();
            load();
        }*/
    private void sortList()
    {
        if(radio[0].getCheckedRadioButtonId()==R.id.mainAllRec)
            onSortLike(datas);
        else
            onSortDay(datas);
        if(radio[1].getCheckedRadioButtonId()==R.id.mainKorRec)
            onSortLike(kordatas);
        else
            onSortDay(kordatas);
        if(radio[2].getCheckedRadioButtonId()==R.id.mainJapRec)
            onSortLike(japdatas);
        else
            onSortDay(japdatas);
        if(radio[3].getCheckedRadioButtonId()==R.id.mainChnRec)
            onSortLike(chndatas);
        else
            onSortDay(chndatas);
        if(radio[4].getCheckedRadioButtonId()==R.id.mainEngRec)
            onSortLike(engdatas);
        else
            onSortDay(engdatas);
        if(radio[5].getCheckedRadioButtonId()==R.id.mainEtcRec)
            onSortLike(etcdatas);
        else
            onSortDay(etcdatas);
    }

    private void setList(ListView list, ArrayList<Item> data) // 추천순으로 3개 넣어주는 method
    {
        ArrayList<Item> arItem = new ArrayList<Item>();
        for(int i=0;i<3;++i)
            arItem.add(data.get(i));
        itemAdapter adapter = new itemAdapter(this,R.layout.item,arItem);
        list.setAdapter(adapter);
    }
    private void load(){
        datas.clear();
        kordatas.clear();
        chndatas.clear();
        japdatas.clear();
        engdatas.clear();
        etcdatas.clear();
        try {
            ParseQuery<ParseObject> query;

            query = ParseQuery.getQuery("Korean"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
                kordatas.add(new Item(object));
            }
            query = ParseQuery.getQuery("China"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
                chndatas.add(new Item(object));
            }
            query = ParseQuery.getQuery("Japan"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
                japdatas.add(new Item(object));
            }
            query = ParseQuery.getQuery("English"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
                engdatas.add(new Item(object));
            }
            query = ParseQuery.getQuery("Etc"); // 서버에 mydatas class 데이터 요청
            for(ParseObject object : query.find())
            {
                datas.add(new Item(object)); // 읽어온 데이터를 List에 저장
                etcdatas.add(new Item(object));
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        ListView list;
        if(group==radio[0])
        {
            if(checkedId==R.id.mainAllRec)
            {
                onSortLike(datas); // 좋아요 순으로 정렬
            }
            else if(checkedId==R.id.mainAllNew)
            {
                onSortDay(datas);
            }
            list = (ListView)findViewById(R.id.list_all);
            setList(list, datas);
        }
        else if(group==radio[1])
        {
            if(checkedId==R.id.mainKorRec)
            {
                onSortLike(kordatas); // 좋아요 순으로 정렬
            }
            else if(checkedId==R.id.mainKorNew)
            {
                onSortDay(kordatas);
            }
            list = (ListView)findViewById(R.id.list_korean);
            setList(list, kordatas);
        }
        else if(group==radio[2])
        {
            if(checkedId==R.id.mainJapRec)
            {
                onSortLike(japdatas); // 좋아요 순으로 정렬
            }
            else if(checkedId==R.id.mainJapNew)
            {
                onSortDay(japdatas);
            }
            list = (ListView)findViewById(R.id.list_japanese);
            setList(list, japdatas);
        }
        else if(group==radio[2])
        {
            if(checkedId==R.id.mainJapRec)
            {
                onSortLike(japdatas); // 좋아요 순으로 정렬
            }
            else if(checkedId==R.id.mainJapNew)
            {
                onSortDay(japdatas);
            }
            list = (ListView)findViewById(R.id.list_japanese);
            setList(list, japdatas);
        }
        else if(group==radio[3])
        {
            if(checkedId==R.id.mainChnRec)
            {
                onSortLike(chndatas); // 좋아요 순으로 정렬
            }
            else if(checkedId==R.id.mainChnNew)
            {
                onSortDay(chndatas);
            }
            list = (ListView)findViewById(R.id.list_chinese);
            setList(list, chndatas);
        }
        else if(group==radio[4])
        {
            if(checkedId==R.id.mainEngRec)
            {
                onSortLike(engdatas); // 좋아요 순으로 정렬
            }
            else if(checkedId==R.id.mainEngNew)
            {
                onSortDay(engdatas);
            }
            list = (ListView)findViewById(R.id.list_american);
            setList(list, engdatas);
        }
        else if(group==radio[5])
        {
            if(checkedId==R.id.mainEtcRec)
            {
                onSortLike(etcdatas); // 좋아요 순으로 정렬
            }
            else if(checkedId==R.id.mainEtcNew)
            {
                onSortDay(etcdatas);
            }
            list = (ListView)findViewById(R.id.list_etc);
            setList(list, etcdatas);
        }
    } // 라디오버튼 바뀔때

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            switch (position) {
                case 0:
                    if(!isMember)
                    {
                        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                        startActivity(intent);
                    } else
                    {
                        Intent intent = new Intent(MainActivity.this,BookmarkActivity.class);
                        startActivity(intent);
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            dlDrawer.closeDrawer(mainNavList);
        }
    } // 드로우 리스너 리스트뷰 클릭시
    public void onBackPressed() {
        finish();
    }
}
