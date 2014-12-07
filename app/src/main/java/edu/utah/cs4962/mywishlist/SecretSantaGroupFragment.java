package edu.utah.cs4962.mywishlist;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Jesus Zarate on 11/27/14.
 */
public class SecretSantaGroupFragment extends Fragment implements ListAdapter
{
    ListView ssGroupListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ssGroupListView = new ListView(getActivity());
        ssGroupListView.setAdapter(this);

        return ssGroupListView;
    }

    public void refreshList()
    {
        ssGroupListView.invalidateViews();
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
       return GroupMemebers.getInstance().getGroupCount();
    }

    @Override
    public Object getItem(int i)
    {
        return GroupMemebers.getInstance().getBuddy(i);
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
        int lighterBlue = res.getColor(R.color.accent_light_blue);
        int darkerBlue = res.getColor(R.color.list_darker_blue);


        textView.setText(GroupMemebers.getInstance().getBuddy(i).MemberName);
        textView.setTextSize(30);
        textView.setHeight(50);
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
        return GroupMemebers.getInstance().getGroupCount() <= 0;
    }


}
