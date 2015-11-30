package com.sap.team_f.cook;

/**
 * Created by KyungTack on 2015-11-30.
 */
public class Recipe {
    private int num = 0;
    private String recipe = "";
    public Recipe(int num)
    {
        this.num = num;
        this.recipe = "";
    }

    public int getNum(){return this.num;}
    public String getRecipe(){return this.recipe;}
    public void setNum(int num){this.num=num;}
    public void setRecipe(String recipe){this.recipe = recipe;}
}
