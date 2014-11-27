package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Jesus Zarate on 11/26/14.
 */
public class myWishItemActivity extends Activity
{

   String timeToDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_wish_item);

        myWishItem wishItem = null;

        if(getIntent().hasExtra(myListFragment.ITEM_ID))
        {
            int id = getIntent().getExtras().getInt(myListFragment.ITEM_ID);
            wishItem = myWishList.getInstance().getWishItem(id);
        }

        // Set the color for the text.
        Resources res = getResources();
        int background_blue = res.getColor(R.color.background_light_blue);
        int itemColor = res.getColor(R.color.primary_light_blue);

        Button getDirectionsButton = (Button) findViewById(R.id.directionsButton);
        getDirectionsButton.setText(timeToDestination + ": Get Directions");
        getDirectionsButton.setBackgroundColor(Color.TRANSPARENT);
        getDirectionsButton.setTextColor(background_blue);

        ImageView itemImage = (ImageView) findViewById(R.id.imageView1);
        TextView name = (TextView) findViewById(R.id.itemName1);
        name.setTextColor(itemColor);
        TextView location = (TextView) findViewById(R.id.itemLocationLabel1);
        location.setTextColor(itemColor);
        TextView price = (TextView) findViewById(R.id.priceLabel1);
        price.setTextColor(itemColor);
        TextView onSale = (TextView) findViewById(R.id.onSaleLabel1);
        onSale.setTextColor(itemColor);
        SeekBar wantLevel = (SeekBar) findViewById(R.id.wantLevelBar1);
        wantLevel.setEnabled(false);

        ImageView mapImage = (ImageView) findViewById(R.id.mapImage);

        if(wishItem != null)
        {
            itemImage.setImageBitmap(wishItem.getPicture());
            name.append(": " + wishItem.getItemName());
            location.append(": " + wishItem.getLocationName());
            price.append(": " + wishItem.getPrice());

            String sale = wishItem.isOnSale() ? "YES" : "NO";
            onSale.append(": " + sale);

            wantLevel.setProgress((int) wishItem.getWantLevel() * 10);

            // Add the map image here.
        }

    }
}
