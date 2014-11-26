package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;

/**
 * Created by Jesus Zarate on 11/24/14.
 */
public class myNewWishItemActivity extends Activity
{

    public static final int labelSize = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.new_wishlist_item);


        //region <Programmatically adding a layout>

//        LinearLayout rootLayout = new LinearLayout(this);
//        rootLayout.setOrientation(LinearLayout.VERTICAL);
//
//        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
//        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        textParams.setMargins(30, 0, 30, 0);
//        Space space1 = new Space(this);
//
//
//        //region <Image Layout>
//        LinearLayout imageLayout = new LinearLayout(this);
//        imageLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//
//        ImageView previewItemImage = new ImageView(this);
//        previewItemImage.setImageResource(R.drawable.ic_launcher);
//
//        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 20);
//        //imageParams.gravity = Gravity.NO_GRAVITY;
//
//        //imageParams.setMargins(100, 100 , 100 , 100);
//        //previewItemImage.setLayoutParams(imageParams);
//
//        imageLayout.addView(previewItemImage, imageParams);
//
//        //endregion <Image Layout>

//
//
//        //region Input Layout
//        LinearLayout inputLayout = new LinearLayout(this);
//        inputLayout.setOrientation(LinearLayout.VERTICAL);
//
//        TextView itemNameLabel = new TextView(this);
//        itemNameLabel.setText("Item Name");
//        itemNameLabel.setTextSize(labelSize);
//
//        EditText itemName = new EditText(this);
//
//        TextView locationLabel = new TextView(this);
//        locationLabel.setText("Name of item location");
//        locationLabel.setTextSize(labelSize);
//
//        EditText locationInput = new EditText(this);
//
//        //region <Price>
//
//        LinearLayout pricePartLayout = new LinearLayout(this);
//        pricePartLayout.setOrientation(LinearLayout.VERTICAL);
//
//        TextView priceLabel = new TextView(this);
//        priceLabel.setText("Price");
//        priceLabel.setTextSize(labelSize);
//
//        EditText priceInput = new EditText(this);
//
//        pricePartLayout.addView(priceLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
//        pricePartLayout.addView(priceInput, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
//        //endregion <Price>
//
//        //region <On Sale>
//        LinearLayout salePortionLayout = new LinearLayout(this);
//        salePortionLayout.setOrientation(LinearLayout.VERTICAL);
//
//        CheckBox saleCheckBox = new CheckBox(this);
//        saleCheckBox.setText("On Sale");
//        saleCheckBox.setTextSize(labelSize);
//
//        salePortionLayout.addView(saleCheckBox, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        //endregion <On Sale>
//
//        //region <Price Portion>
//        LinearLayout pricePortionLayout = new LinearLayout(this);
//        pricePortionLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//        pricePortionLayout.addView(pricePartLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
//        pricePortionLayout.addView(salePortionLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
//
//        //endregion <Price Portion>
//
//        //region <Item Name>
//        inputLayout.addView(space1, spaceParams);
//        inputLayout.addView(itemNameLabel, textParams);
//        inputLayout.addView(itemName, textParams);
//        //endregion <Item Name>
//
//        //region <Location Name>
//        inputLayout.addView(new Space(this), spaceParams);
//        inputLayout.addView(locationLabel, textParams);
//        inputLayout.addView(locationInput, textParams);
//        //endregion <Location Name>
//
//        //region <Want Level>
//
//        SeekBar wantLevel = new SeekBar(this);
//
//        //endregion <Want Level>
//
//        Button addItem = new Button(this);
//        addItem.setText("Add Item");
//        addItem.setTextSize(labelSize);
//        //addItem.setGravity(Gravity.BOTTOM);
//
//
//        inputLayout.addView(new Space(this), spaceParams);
//        inputLayout.addView(pricePortionLayout, textParams);
//
//        inputLayout.addView(new Space(this), spaceParams);
//        inputLayout.addView(wantLevel, textParams);
//
//        inputLayout.addView(new Space(this), spaceParams);
//        textParams.gravity = Gravity.BOTTOM;
//        inputLayout.addView(addItem, textParams);
//        //endregion Input Layout
//
//        rootLayout.addView(imageLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 25));
//        rootLayout.addView(inputLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 75));
//        setContentView(rootLayout);
        //endregion <Programmatically adding a layout>
    }
}
