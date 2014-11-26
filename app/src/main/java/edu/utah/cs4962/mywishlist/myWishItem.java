package edu.utah.cs4962.mywishlist;

/**
 * Created by Jesus Zarate on 11/25/14.
 */
public class myWishItem
{
    private String itemName;
    private String locationName;
    private int price;
    private boolean onSale;
    private int wantLevel;
    private Coordinates coordinates;

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

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
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

    public int getWantLevel()
    {
        return wantLevel;
    }

    public void setWantLevel(int wantLevel)
    {
        this.wantLevel = wantLevel;
    }

    class Coordinates
    {
        float latidude;
        float longitude;
    }
}
