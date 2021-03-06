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
    private Date createdAt;
    private JSONArray recipe;
    private String type = "";
    private JSONArray subMaterial;
    private JSONArray reply;

    public Item(ParseObject obj,String type)
    {
        this.objectId = obj.getObjectId();
        this.image = obj.getParseFile("Image").getUrl();
        this.name = obj.getString("Name");
        this.like = obj.getInt("Like");
        this.info = obj.getString("Info");
        this.comment = obj.getString("Comment");
        this.createdAt = obj.getCreatedAt();
        this.material = obj.getJSONArray("Material");
        this.recipe = obj.getJSONArray("Recipe");
        this.subMaterial = obj.getJSONArray("SubMaterial");
        this.Id = obj.getString("Id");
        this.type=type;
        this.reply = obj.getJSONArray("Reply");
    }
    public String getObjectId() {return this.objectId;}
    public String getImage()    {return this.image;}
    public String getName()     {return this.name;}
    public int getLike()        {return this.like;}
    public String getInfo()     {return this.info;}
    public String getComment()  {return this.comment;}
    public Date getCreatedAt()  {return this.createdAt;}
    public JSONArray getMaterial() {return this.material;}
    public JSONArray getSubMaterial() {return this.subMaterial;}
    public JSONArray getRecipe() {return this.recipe;}
    public String getId()       {return this.Id;}
    public String getType()     {return this.type;}
    public JSONArray getReply() {return this.reply;}
    public void setLike(boolean sw){
        if(sw==true)
            ++like;
        else
            --like;
    }

    public void setReply(JSONArray reply) {this.reply=reply;}
}
