package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

/**
 * Created by Jesus Zarate on 11/24/14.
 */
public class myNewWishItem extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(30, 0, 30, 0);
        Space space1 = new Space(this);


        TextView itemNameLabel = new TextView(this);
        itemNameLabel.setText("Item Name");
        itemNameLabel.setTextSize(15);

        EditText itemName = new EditText(this);

        TextView locationLabel = new TextView(this);
        locationLabel.setText("Name of item location ");
        locationLabel.setTextSize(15);

        EditText locationInput = new EditText(this);

        //region <Price>
        LinearLayout pricePartLayout = new LinearLayout(this);
        pricePartLayout.setOrientation(LinearLayout.VERTICAL);

        TextView priceLabel = new TextView(this);
        priceLabel.setText("Price");
        priceLabel.setTextSize(15);

        EditText priceInput = new EditText(this);

        pricePartLayout.addView(priceLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        pricePartLayout.addView(priceInput, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        //endregion <Price>

        //region <On Sale>
        LinearLayout salePortionLayout = new LinearLayout(this);
        salePortionLayout.setOrientation(LinearLayout.VERTICAL);

        CheckBox saleCheckBox = new CheckBox(this);
        saleCheckBox.setText("On Sale");

        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        checkBoxParams.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;

        salePortionLayout.addView(saleCheckBox, checkBoxParams);
        //endregion <On Sale>

        LinearLayout pricePortionLayout = new LinearLayout(this);
        pricePortionLayout.setOrientation(LinearLayout.HORIZONTAL);

        pricePortionLayout.addView(pricePartLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        pricePortionLayout.addView(salePortionLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        //region <Item Name>
        rootLayout.addView(space1, spaceParams);
        rootLayout.addView(itemNameLabel, textParams);
        rootLayout.addView(itemName, textParams);
        //endregion <Item Name>

        //region <Location Name>
        rootLayout.addView(new Space(this), spaceParams);
        rootLayout.addView(locationLabel, textParams);
        rootLayout.addView(locationInput, textParams);
        //endregion <Location Name>

        rootLayout.addView(new Space(this), spaceParams);
        rootLayout.addView(pricePortionLayout, textParams);


        setContentView(rootLayout);
    }
}
