package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Created by Jesus Zarate on 11/24/14.
 */
public class myListFragment extends Fragment implements ListAdapter
{
    ListView wishItemList;

    // key => ImageName, value => bitmap
    HashMap<String, Bitmap> pictures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        wishItemList = new ListView(getActivity());
        wishItemList.setAdapter(this);
        wishItemList.setDividerHeight(10);

        pictures = new HashMap<String, Bitmap>();

        wishItemList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // Start the item activity.
            }
        });

        return wishItemList;
    }

    public void refreshList()
    {
        wishItemList.invalidateViews();
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
        Resources res = getResources();
        int background_blue = res.getColor(R.color.background_light_blue);
        item.setBackgroundColor(background_blue);

        String imageName = myWishList.getInstance().getWishItem(i).getImageName();
        String imagePath = getImagePath(imageName);

        //Bitmap bitmap = getImage(imagePath);

        ImageView imagePreview = new ImageView(getActivity());
        //imagePreview.setImageBitmap(bitmap);

        if(!pictures.containsKey(imageName))
        {
            Bitmap tempB = getPic(imagePath);
            pictures.put(imageName, tempB);

            imagePreview.setImageBitmap(tempB);
        }
        else
        {
            imagePreview.setImageBitmap(pictures.get(imageName));
        }

        TextView textView = new TextView(getActivity());
        textView.setText(myWishList.getInstance().getWishItem(i).getItemName());
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD_ITALIC);
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 20);
        imageParams.setMargins(20, 20, 0, 20);
        item.addView(imagePreview, imageParams);
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

    public String getImagePath(String imageName)
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/" + myListActivity.MY_WISH_LIST_DIR + "/" + imageName;
    }

    private Bitmap getPic(String ImagePath) {
        // Get the dimensions of the View
        int targetW = 100;//mImageView.getWidth();
        int targetH = 100;//mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(ImagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(ImagePath, bmOptions);

        return bitmap;
        //mImageView.setImageBitmap(bitmap);
    }

    public Bitmap getImage(String imagePath)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        return bitmap;
    }
}
