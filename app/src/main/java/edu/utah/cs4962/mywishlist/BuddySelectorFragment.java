package edu.utah.cs4962.mywishlist;

import android.app.Fragment;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jesus Zarate on 12/10/14.
 */
public class BuddySelectorFragment extends Fragment implements ListAdapter
{
    ListView buddyList;
    ArrayList<Buddy> dataList;

    public static ArrayList<Buddy> selectedBuddies;

    public void init()
    {
        dataList = myBuddyList.getInstance().getBuddyList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        buddyList = new ListView(getActivity());
        buddyList.setAdapter(this);
        buddyList.setDividerHeight(10);
        buddyList.setBackgroundColor(getResources().getColor(R.color.green_A400));

        buddyList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                view.setBackgroundColor(getResources().getColor(R.color.accent_light_blue));
                selectedBuddies.add(dataList.get(i));
            }
        });

        selectedBuddies = new ArrayList<Buddy>();

        return buddyList;
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
        if (dataList != null)
            return dataList.size();
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
        LinearLayout item = new LinearLayout(getActivity());
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        Resources res = getResources();
        int background_blue = res.getColor(R.color.accent_green);
        //item.setBackgroundColor(background_blue);
        item.setBackgroundResource(R.drawable.list_item);

        TextView textView = new TextView(getActivity());
        textView.setText("\n" +
                dataList.get(i).MemberName +
                "\n");
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD_ITALIC);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        item.addView(textView);

        return item;
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
