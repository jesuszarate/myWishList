package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Jesus Zarate on 11/26/14.
 */
public class myWishItemActivity extends Activity
{

    String timeToDestination;
    myWishItem wishItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_wish_item);

        if (getIntent().hasExtra(myListFragment.BUDDY_ID) && getIntent().hasExtra(myListFragment.ITEM_ID))
        {
            int itemId = getIntent().getExtras().getInt(myListFragment.ITEM_ID);
            int buddyId = getIntent().getExtras().getInt(myListFragment.BUDDY_ID);

            wishItem = myBuddyList.getInstance().getBuddy(buddyId).wishList.get(itemId);
        } else if (getIntent().hasExtra(myListFragment.ITEM_ID))
        {
            int id = getIntent().getExtras().getInt(myListFragment.ITEM_ID);
            wishItem = myWishList.getInstance().getWishItem(id);
        }

        // Set the color for the text.
        Resources res = getResources();
        int background_blue = res.getColor(R.color.background_light_blue);
        int itemColor = res.getColor(R.color.primary_light_blue);

        Button getDirectionsButton = (Button) findViewById(R.id.directionsButton);
        getDirectionsButton.setText(getString(R.string.GetDirections));
        getDirectionsButton.setBackgroundColor(Color.TRANSPARENT);
        getDirectionsButton.setTextColor(background_blue);

        getDirectionsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(wishItem != null)
                {
                    if(wishItem.getCoordinates() != null)
                    {
                        String location = wishItem.getLocationName();
                        double latitude = wishItem.getCoordinates().latidude;
                        double longitude = wishItem.getCoordinates().longitude;
                        getDirectionsToLocation(location, latitude, longitude);
                    }

                }
            }
        });

        ImageView mapImage = (ImageView) findViewById(R.id.mapImage);
        mapImage.setImageResource(R.drawable.google_maps);
        mapImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String location = wishItem.getLocationName();
                double latitude = wishItem.getCoordinates().latidude;
                double longitude = wishItem.getCoordinates().longitude;
                getDirectionsToLocation(location, latitude, longitude);

            }
        });

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

        if (wishItem != null)
        {
            String imageName = wishItem.getImageName();
            String imagePath = myListFragment.getImagePath(imageName);

            itemImage.setImageBitmap(myListFragment.getPic(imagePath));

            name.append(": " + wishItem.getItemName());
            location.append(": " + wishItem.getLocationName());
            price.append(": " + wishItem.getPrice());

            String sale = wishItem.isOnSale() ? "YES" : "NO";
            onSale.append(": " + sale);

            wantLevel.setProgress((int) wishItem.getWantLevel() * 10);

            // Add the map image here.
        }

    }

    private void getDirectionsToLocation(String location, double latitude, double longitude)
    {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?&daddr=%f,%f (%s)", latitude, longitude, location);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(myWishItemActivity.this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }
}
