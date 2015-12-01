package com.sap.team_f.cook;

import android.util.Log;

import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Date;

/**
 * Created by KyungTack on 2015-11-19.
 */
public class Item implements Serializable{
    private String objectId = "";
    private String name = "";
    private String image = "";
    private int like = 0;
    private String info = "";
    private String Id = "";
    private JSONArray material;
    private String comment = "";
    private Date updatedAt;
    private Date createdAt;
    private JSONArray recipe;
    private String type = "";
    private JSONArray subMaterial;

    public Item(ParseObject obj,String type)
    {
        this.image = obj.getParseFile("Image").getUrl();
        this.name = obj.getString("Name");
        this.like = obj.getInt("Like");
        this.info = obj.getString("Info");
        this.comment = obj.getString("Comment");
        this.updatedAt = obj.getUpdatedAt();
        this.createdAt = obj.getCreatedAt();
        this.material = obj.getJSONArray("Material");
        this.recipe = obj.getJSONArray("Recipe");
        this.subMaterial = obj.getJSONArray("SubMaterial");
        this.Id = obj.getString("Id");
        this.type=type;
    }
    public String getImage()    {return this.image;}
    public String getName()     {return this.name;}
    public int getLike()        {return this.like;}
    public String getInfo()     {return this.info;}
    public String getComment()  {return this.comment;}
    public Date getUpdatedAt()  {return this.updatedAt;}
    public Date getCreatedAt()  {return this.createdAt;}
    public JSONArray getMaterial() {return this.material;}
    public JSONArray getSubMaterial() {return this.subMaterial;}
    public JSONArray getRecipe() {return this.recipe;}
    public String getId()       {return this.Id;}
    public String getType()     {return this.type;}
}
