package com.sap.team_f.cook;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by KyungTack on 2015-11-27.
 */
public class FoodActivity extends ActionBarActivity {

    ListView foodRecipeList;
    Item food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodactivity);

        food = MainActivity.food;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(food.getName());

        ImageView foodImg = (ImageView)findViewById(R.id.selectedFoodImg);
        ImageLoaderTask imageLoaderTask = new ImageLoaderTask(foodImg,food.getImage());
        imageLoaderTask.execute();
        TextView foodLike = (TextView)findViewById(R.id.selectedFoodLike);
        foodLike.setText(Integer.toString(food.getLike()));
        Button likeBtn = (Button)findViewById(R.id.likeBtn);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(StartActivity.currentUser==null)
                {
                    Toast.makeText(FoodActivity.this, "로그인 해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONArray arr = StartActivity.currentUser.getJSONArray("Like");
                /*if(arr==null)
                {
                    arr = new JSONArray();
                    arr.put(food.getName());
                    food.setLike(true);
                    setLike(arr);
                    return;
                }*/
                for(int i=0;i<arr.length();++i)
                {
                    try {
                        if(arr.getString(i).equals(food.getName()))
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                arr.remove(i);
                            }
                            food.setLike(false);

                            setLike(arr);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                arr.put(food.getName());
                food.setLike(true);
                setLike(arr);
                return;
            }
        });
        TextView foodType = (TextView)findViewById(R.id.selectedFoodType);
        foodType.setText(food.getType());
        TextView foodInfo = (TextView)findViewById(R.id.selectedFoodInfoText);
        foodInfo.setText(food.getInfo());
        TextView foodMaterial = (TextView)findViewById(R.id.selectedFoodMaterialText);
        JSONArray material = food.getMaterial();
        String str = "";
        try {
            str=str+material.getString(0);
            for(int i=1;i<material.length();++i)
            {
                str=str+", "+material.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        foodMaterial.setText(str);
        str="";
        TextView foodSubMaterial = (TextView)findViewById(R.id.selectedFoodSubMaterialText);
        JSONArray submaterial = food.getSubMaterial();
        try {
            str=str+submaterial.getString(0);
            for(int i=1;i<submaterial.length();++i)
            {
                str=str+", "+submaterial.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        foodSubMaterial.setText(str);
        foodRecipeList = (ListView)findViewById(R.id.selectedFoodRecipeList);
        JSONArray recipeList = food.getRecipe();
        String[] recipe = new String[recipeList.length()];
        Log.v("index", Integer.toString(recipeList.length()));
        for(int i=0;i<recipeList.length();++i)
        {
            try {
                recipe[i]=Integer.toString(i+1)+" : " +recipeList.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,recipe);
        foodRecipeList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(foodRecipeList);
        TextView foodComment = (TextView)findViewById(R.id.selectedFoodCommentText);
        foodComment.setText(food.getComment());
    }

    private void setLike(JSONArray arr)
    {
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
                    foodObj.put("Like", food.getLike());
                    foodObj.saveInBackground();
                }
            }
        });
        StartActivity.currentUser.put("Like", arr);
        StartActivity.currentUser.saveInBackground();
        TextView foodLike = (TextView)findViewById(R.id.selectedFoodLike);
        foodLike.setText(Integer.toString(food.getLike()));
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
