package edu.utah.cs4962.mywishlist;

import android.graphics.Bitmap;

/**
 * Created by Jesus Zarate on 11/25/14.
 */
public class myWishItem
{
    private String itemName;
    private String locationName;
    private double price;
    private boolean onSale;
    private double wantLevel;
    private Coordinates coordinates;
    private String imageName;
    private Bitmap picture;

    private String JSONBitmapString;

    public String getJSONBitmapString()
    {
        return JSONBitmapString;
    }

    public void setJSONBitmapString(String JSONBitmapString)
    {
        this.JSONBitmapString = JSONBitmapString;
    }

    public Bitmap getPicture()
    {
        return picture;
    }

    public void setPicture(Bitmap picture)
    {
        this.picture = picture;
    }

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    public Coordinates getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates)
    {
        this.coordinates = coordinates;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public boolean isOnSale()
    {
        return onSale;
    }

    public void setOnSale(boolean onSale)
    {
        this.onSale = onSale;
    }

    public double getWantLevel()
    {
        return wantLevel;
    }

    public void setWantLevel(double wantLevel)
    {
        this.wantLevel = wantLevel;
    }

    class Coordinates
    {
        float latidude;
        float longitude;
    }
}
