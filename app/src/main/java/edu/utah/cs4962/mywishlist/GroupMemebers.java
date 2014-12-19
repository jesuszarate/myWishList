package edu.utah.cs4962.mywishlist;

import java.util.ArrayList;

/**
 * Created by Jesus Zarate on 11/27/14.
 */
public class GroupMemebers
{
    private ArrayList<Buddy> ssGroupList = new ArrayList<Buddy>();

    private static GroupMemebers instance = null;

    public static GroupMemebers getInstance()
    {
        if (instance == null)
        {
            instance = new GroupMemebers();
        }
        return instance;
    }
    private GroupMemebers(){}

    public void initGroupMembers(ArrayList<Buddy> ssGroupList)
    {
        this.ssGroupList = ssGroupList;
    }

    public ArrayList<Buddy> getSsGroupList()
    {
        return ssGroupList;
    }

    public Buddy getBuddy(int id)
    {
        return ssGroupList.get(id);
    }

    public void addBuddy(Buddy buddy)
    {
        ssGroupList.add(buddy);
    }

    public void addAllBuddies(ArrayList<Buddy> Buddies)
    {
        ssGroupList.addAll(Buddies);
    }

    public void removeBuddy(Buddy buddy)
    {
        ssGroupList.remove(buddy);
    }

    public int getGroupCount()
    {
        return ssGroupList.size();
    }

    public void clearList(){ ssGroupList.clear(); }

}
