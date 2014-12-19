package edu.utah.cs4962.mywishlist;

import java.util.ArrayList;

/**
 * Created by Jesus Zarate on 11/25/14.
 */
public class myWishList
{

    private ArrayList<myWishItem> wishList = new ArrayList<myWishItem>();

    private User user = new User();

    private static myWishList _instance = null;

    static myWishList getInstance()
    {
        if(_instance == null)
        {
            _instance = new myWishList();
        }
        return _instance;
    }
    private myWishList(){}

    public void initMyWishList(ArrayList<myWishItem> wishList)
    {
        this.wishList = wishList;
    }
    public ArrayList<myWishItem> getWishList()
    {
        return wishList;
    }

    public myWishItem getWishItem(int identifier)
    {
        return wishList.get(identifier);
    }

    public void addWishtItem(myWishItem wishItem)
    {
        wishList.add(wishItem);
    }

    public void removeWishItem(myWishItem wishItem)
    {
        wishList.remove(wishItem);
    }

    public int getWishListCount()
    {
        return wishList.size();
    }

    public String getEmailAddress()
    {
        return user.emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.user.emailAddress = emailAddress;
    }

    public User getUser()
    {
        return this.user;
    }

    public String getUserName()
    {

        return user.userName;
    }

    public void setUserName(String userName)
    {
        this.user.userName = userName;
    }

    public Buddy toBuddy()
    {
        Buddy buddy = new Buddy();
        buddy.MemberName = user.userName;
        buddy.EmailAddress = user.emailAddress;
        buddy.wishList = wishList;

        return buddy;
    }
}
