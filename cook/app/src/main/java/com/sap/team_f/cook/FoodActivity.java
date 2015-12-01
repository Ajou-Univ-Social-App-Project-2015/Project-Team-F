package com.sap.team_f.cook;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by KyungTack on 2015-11-27.
 */
public class FoodActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodactivity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(MainActivity.food.getName());

        Item food = MainActivity.food;
        ImageView foodImg = (ImageView)findViewById(R.id.selectedFoodImg);
        ImageLoaderTask imageLoaderTask = new ImageLoaderTask(foodImg,food.getImage());
        imageLoaderTask.execute();
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
        ListView foodRecipeList = (ListView)findViewById(R.id.selectedFoodRecipeList);
        JSONArray recipeList = food.getRecipe();
        String[] recipe = new String[recipeList.length()];
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
        TextView foodComment = (TextView)findViewById(R.id.selectedFoodCommentText);
        foodComment.setText(food.getComment());
    }
}
