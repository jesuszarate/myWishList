package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by Jesus Zarate on 11/24/14.
 */
public class myListFragment extends Fragment implements ListAdapter
{
    ListView wishItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        wishItemList = new ListView(getActivity());
        wishItemList.setAdapter(this);

        return wishItemList;
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
        return myWishList.getInstance().getWishListCount();
    }

    @Override
    public Object getItem(int i)
    {
        return myWishList.getInstance().getWishItem(i);
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
        item.setBackgroundColor(Color.LTGRAY);

        ImageView imagePreview = new ImageView(getActivity());
        imagePreview.setImageResource(R.drawable.ic_launcher);

        TextView textView = new TextView(getActivity());
        textView.setText("android phone");
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD_ITALIC);
        textView.setGravity(Gravity.CENTER_VERTICAL);


        item.addView(imagePreview, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 20));
        item.addView(textView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 80));

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
        return myWishList.getInstance().getWishListCount() <= 0;
    }
}
