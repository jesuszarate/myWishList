package edu.utah.cs4962.mywishlist;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jesus Zarate on 11/27/14.
 */
public class SecretSantaGroupFragment extends Fragment implements ListAdapter
{
    ListView ssGroupListView;
    ArrayList<Buddy> dataList;

    private int LIST_ID;

    public static final int BUDDY_LIST = 101;
    public static final int SECRET_SANTA_LIST = 102;

    public static final String SELECTED_BUDDY = "selectedBuddy";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ssGroupListView = new ListView(getActivity());
        ssGroupListView.setAdapter(this);
        ssGroupListView.setDividerHeight(10);
        ssGroupListView.setBackgroundColor(getResources().getColor(R.color.green_A400));


        ssGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(LIST_ID == BUDDY_LIST)
                {
                    // Open myListActivity with the wish list.
                    openWishList(MainScreenActivity.MY_LIST_TYPE, i);
                }
            }
        });

        return ssGroupListView;
    }

    public void init(int listID)
    {
        LIST_ID = listID;
        if(listID == BUDDY_LIST)
        {
            dataList = myBuddyList.getInstance().getBuddyList();
        }
        else if(listID == SECRET_SANTA_LIST)
        {
            dataList = GroupMemebers.getInstance().getSsGroupList();
        }
    }

    public void refreshList()
    {
        ssGroupListView.invalidateViews();
    }

    private void openWishList(String TypeOfList, int selectedItem)
    {
        Intent intent = new Intent(getActivity(), myListActivity.class);
        intent.putExtra(TypeOfList, false);
        intent.putExtra(SELECTED_BUDDY, selectedItem);
        startActivity(intent);
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int i)
    {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver)
    {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver)
    {

    }

    @Override
    public int getCount()
    {
        if(dataList != null)
        {
            return dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i)
    {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

        Resources res = getResources();
        int lighterBlue = res.getColor(R.color.accent_green);
        int darkerBlue = res.getColor(R.color.accent_green);


        textView.setText("\n"+
                dataList.get(i).MemberName +
                        "\n");
        textView.setTypeface(null, Typeface.BOLD_ITALIC);
        textView.setTextSize(20);
        //textView.setHeight(50);
        textView.setBackgroundColor((i % 2) == 0 ? lighterBlue : darkerBlue);
        return textView;
    }

    @Override
    public int getItemViewType(int i)
    {
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
     return dataList.size() <= 0;
    }


}
