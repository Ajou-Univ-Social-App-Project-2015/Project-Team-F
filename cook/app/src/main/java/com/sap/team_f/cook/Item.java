package com.sap.team_f.cook;

import com.parse.ParseFile;

/**
 * Created by KyungTack on 2015-11-19.
 */
public class Item {
    private String image = "";
    private String name = "";
    private int like = 0;
    public Item(ParseFile image, String name, int like)
    {
        this.image = image.getUrl();
        this.name = name;
        this.like = like;
    }
    public String getImage()
    {
        return this.image;
    }
    public String getName()
    {
        return this.name;
    }
    public int getLike()
    {
        return this.like;
    }
}
