package com.example.android.bakingappbr.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipesItem implements Parcelable{
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ingredients")
    @Expose
    private List<IngredientItem> ingredients = null;

    @SerializedName("steps")
    @Expose
    private ArrayList<RecipeStep> steps = null;

    @SerializedName("servings")
    @Expose
    private Integer servings;

    @SerializedName("image")
    @Expose
    private String image;



    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<IngredientItem> getIngredients() {
        return ingredients;
    }

    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }

    public String getImage() {
        return image;
    }


    private RecipesItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
        ingredients = new ArrayList<>();
        in.readTypedList(ingredients, IngredientItem.CREATOR);
        steps = new ArrayList<>();
        in.readTypedList(steps, RecipeStep.CREATOR);





    }
    public static final Creator<RecipesItem> CREATOR = new Creator<RecipesItem>() {
        @Override
        public RecipesItem createFromParcel(Parcel in) {
            return new RecipesItem(in);
        }

        @Override
        public RecipesItem[] newArray(int size) {
            return new RecipesItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(image);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);






    }
}
