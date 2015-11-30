package com.sap.team_f.cook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by KyungTack on 2015-11-27.
 */
public class AddRecipeActivity extends ActionBarActivity {

    protected static final int REQ_CODE_PICK_PICTURE = 0;
    protected static final int CAMERA_PIC_REQUEST = 1337;
    ArrayList<Recipe> index = new ArrayList<Recipe>();
    RecipeAdapter adapter = null;
    Bitmap orgImage;
    int num=0;
    ListView list;
    String foodCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecipeactivity);
        final ImageView foodImg = (ImageView)findViewById(R.id.foodImg);
        foodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, REQ_CODE_PICK_PICTURE);
            }
        });
        Spinner spinner =(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinadapter = ArrayAdapter.createFromResource(this,R.array.food_array,android.R.layout.simple_spinner_item);
        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodCategory=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                foodCategory = "기타";
            }
        });

        //adapter = new RecipeAdapter(AddRecipeActivity.this,R.layout.recipe,index);
        adapter = new RecipeAdapter(this,index);
        list = (ListView)findViewById(R.id.foodRecipeList);
        list.setAdapter(adapter);
        Button addBtn = (Button)findViewById(R.id.foodRecipeAddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++num;
                for (int i = 0; i < num; ++i) {
                    if (index.size() < i + 1)
                        index.add(new Recipe(i + 1));
                    Log.v("index : ", Integer.toString(index.get(i).getNum()));
                }
                Log.v("index ", Integer.toString(adapter.getCount()));
                adapter.notifyDataSetChanged();
            }
        });

        Button delBtn = (Button)findViewById(R.id.foodRecipeDeleteBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num==0)
                    return;
                --num;
                index.remove(num);
                Log.v("index ",Integer.toString(adapter.getCount()));
                adapter.notifyDataSetChanged();
            }
        });

        Button enrollBtn = (Button)findViewById(R.id.foodEnrollBtn);
        enrollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item;
                ImageView foodImg=(ImageView)findViewById(R.id.foodImg);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                orgImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ParseFile img = new ParseFile("food.png",byteArray);
                Log.v("food img :",img.toString());
                EditText foodName = (EditText)findViewById(R.id.foodNameText);
                String name = foodName.getText().toString();
                Log.v("food name :",name);
                EditText foodInfo = (EditText)findViewById(R.id.foodInfoText);
                String info = foodInfo.getText().toString();
                Log.v("food info :",info);
                EditText foodMaterial = (EditText)findViewById(R.id.foodMaterialText);
                String[] material=foodMaterial.getText().toString().split(" ");
                JSONArray materialJSON = new JSONArray(Arrays.asList(material));
                for(int i=0;i<materialJSON.length();++i)
                {
                    try {
                        Log.v("food material :",materialJSON.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                EditText foodSubMaterial = (EditText)findViewById(R.id.foodSubMaterialText);
                String[] submaterial=foodSubMaterial.getText().toString().split(" ");
                JSONArray submaterialJSON = new JSONArray(Arrays.asList(submaterial));
                for(int i=0;i<submaterialJSON.length();++i)
                {
                    try {
                        Log.v("food submaterial :",submaterialJSON.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String[] recipe = new String[adapter.getCount()];
                for(int i=0;i<adapter.getCount();++i)
                {
                    recipe[i]=adapter.getItem(i).getRecipe();
                }
                JSONArray recipeJSON = new JSONArray(Arrays.asList(recipe));
                for(int i=0;i<recipeJSON.length();++i)
                {
                    try {
                        Log.v("food recipe :",recipeJSON.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                EditText foodComment = (EditText)findViewById(R.id.foodCommentText);
                String comment = foodComment.getText().toString();
                Log.v("food comment :",comment);
                try {
                    ParseObject obj;
                    if(foodCategory=="한식")
                        obj = new ParseObject("Korean");
                    else if(foodCategory=="중식")
                        obj = new ParseObject("China");
                    else if(foodCategory=="일식")
                        obj = new ParseObject("Japan");
                    else if(foodCategory=="양식")
                        obj = new ParseObject("English");
                    else
                        obj = new ParseObject("Etc");
                    obj.put("Name",name);
                    obj.put("Image",img);
                    obj.put("Like",0);
                    obj.put("Info",info);
                    obj.put("Material",materialJSON);
                    obj.put("Comment",comment);
                    obj.put("Recipe",recipeJSON);
                    obj.put("SubMaterial",submaterialJSON);
                    obj.put("Id",StartActivity.currentUser.getUsername());
                    obj.save();
                    //finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_PICK_PICTURE || requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == ActionBarActivity.RESULT_OK) {
                ImageView foodImg = (ImageView) findViewById(R.id.foodImg);
                Uri imgUri = data.getData(); // 이미지 주소 저장
                // 이미지 절대경로 얻기
                Cursor c = getContentResolver().query(Uri.parse(imgUri.toString()), null, null, null, null);
                c.moveToNext();
                String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                orgImage = BitmapFactory.decodeFile(absolutePath, options);
                Bitmap bitmap = Bitmap.createScaledBitmap(orgImage, 300, 300, true);

                foodImg.setImageBitmap(bitmap);
            }
        }
    }
}
