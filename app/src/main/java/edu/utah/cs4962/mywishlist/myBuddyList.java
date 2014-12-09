package edu.utah.cs4962.mywishlist;

import java.util.ArrayList;

/**
 * Created by Jesus Zarate on 12/8/14.
 */
public class myBuddyList
{
    private ArrayList<Buddy> buddyList = new ArrayList<Buddy>();

    private static myBuddyList instance = null;

    public static myBuddyList getInstance()
    {
        if(instance == null)
        {
            instance = new myBuddyList();
        }
        return instance;
    }

    private myBuddyList(){}

    public ArrayList<Buddy> getBuddyList()
    {
        return buddyList;
    }

    public Buddy getBuddy(int id)
    {
        return buddyList.get(id);
    }

    public void setBuddy(Buddy buddy)
    {
        buddyList.add(buddy);
    }

    public void removeBuddy(Buddy buddy)
    {
        buddyList.remove(buddy);
    }

    public int getBuddyListCount()
    {
        return buddyList.size();
    }
}
