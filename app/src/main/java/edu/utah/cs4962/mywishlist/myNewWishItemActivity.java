package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Jesus Zarate on 11/24/14.
 */
public class myNewWishItemActivity extends Activity
{

    public String imagePath;

    myWishItem newWishItem;

    EditText itemName;
    EditText itemLocation;
    EditText itemPrice;
    CheckBox itemOnSale;
    SeekBar itemWantLevel;

    Button addNewItemButton;


    private double latitude;
    private double longitude;

    //region Listeners

    public interface OnNewItemAddedListener
    {
        public void OnNewItemAdded(myNewWishItemActivity myNewWishItemActivity);
    }

    OnNewItemAddedListener _onNewItemAddedListener = null;

    public void setOnNewItemAddedListener(OnNewItemAddedListener onNewItemAddedListener)
    {
        this._onNewItemAddedListener = onNewItemAddedListener;
    }
    //endregion Listeners


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_wishlist_item);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        // Create a new wish item
        newWishItem = new myWishItem();

        itemName = (EditText) findViewById(R.id.itemNameInputText);
        itemLocation = (EditText) findViewById(R.id.locationInputText);

        if (getIntent().hasExtra(myListActivity.IMAGE_PATH))
        {
            // Get the coordinates of the location where the image was taken
            GPSTracker gpsTracker = new GPSTracker(myNewWishItemActivity.this);

            if (gpsTracker.canGetLocation())
            {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();

                Toast.makeText(getApplicationContext(), "Latitude: " + latitude +
                        "\nLongitude: " + longitude, Toast.LENGTH_SHORT).show();
            }

            // Set the image taken to the item preview
            imagePath = getIntent().getStringExtra(myListActivity.IMAGE_PATH);

            Bitmap bitmap = getPic(imagePath);
            imageView.setImageBitmap(bitmap);
        }

        itemPrice = (EditText) findViewById(R.id.itemPriceInput);
        itemPrice.setHint("0.00");
        itemPrice.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        itemOnSale = (CheckBox) findViewById(R.id.onSaleCheckBox);
        itemWantLevel = (SeekBar) findViewById(R.id.wantLevelBar);

        //region <Add New Item Button>
        addNewItemButton = (Button) findViewById(R.id.addNewItemButton);

        // Add the new item to the user's list.
        addNewItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (checkCorrectInput())
                {
                    newWishItem.setItemName(itemName.getText().toString());
                    newWishItem.setLocationName(itemLocation.getText().toString());
                    newWishItem.setPrice(Double.parseDouble(itemPrice.getText().toString()));
                    newWishItem.setWantLevel(itemWantLevel.getProgress() / 10);
                    newWishItem.setOnSale(itemOnSale.isChecked());

                    myWishItem.Coordinates coordinates = new myWishItem.Coordinates();
                    coordinates.latidude = latitude;
                    coordinates.longitude = longitude;
                    newWishItem.setCoordinates(coordinates);

                    File dir = Environment.getExternalStorageDirectory();
                    //File dir = getFilesDir();

                    if (dir.exists())
                    {
                        String regex = getString(R.string.image_regular_expression);
                        File from = new File(dir, myListActivity.MY_WISH_LIST_DIR + "/" + myListActivity.LATEST_IMAGE);

                        File to = new File(dir,
                                myListActivity.MY_WISH_LIST_DIR + "/" + regex + itemName.getText() +
                                        "_" + itemLocation.getText() + regex + ".png");


                        if (from.exists())
                        {
                            from.renameTo(to);
                        }

                        if (to.exists())
                        {
                            newWishItem.setImageName(to.getName());
                            //addNewItemButton.setText(to.getName());
                        }
                    }

                myWishList.getInstance().addWishtItem(newWishItem);
                MainScreenActivity.saveMyWishList(getFilesDir());

                closeActivity();
            }
            }
        });


        //endregion <Add New Item Button>
    }

    public Bitmap getPic(String ImagePath)
    {
        if (ImagePath != null)
        {
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
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(ImagePath, bmOptions);

            return bitmap;
            //mImageView.setImageBitmap(bitmap);
        }
        return null;
    }

    public void closeActivity()
    {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        //finishActivity(myListActivity.NEW_ITEM_ADDED);
        finish();
    }

    public boolean checkCorrectInput()
    {
        if (itemName.getText().length() > 0 &&
                itemLocation.getText().length() > 0)
        {
            if (itemPrice.getText().length() > 0)
            {
                try
                {
                    Double.parseDouble(itemPrice.getText().toString());
                    return true;
                } catch (Exception e)
                {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        MainScreenActivity.saveMyWishList(getFilesDir());
    }
}
