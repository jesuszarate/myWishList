package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jesus Zarate on 12/10/14.
 */
public class BuddySelectorActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);


        LinearLayout listLayout = new LinearLayout(this);
        listLayout.setOrientation(LinearLayout.VERTICAL);
        listLayout.setId(13);

        //region <Title>
        LinearLayout titleLayout = new LinearLayout(this);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);

        Resources res = getResources();
        int green = res.getColor(R.color.accent_green);
        int indigo = res.getColor(R.color.primary_light_blue);
        titleLayout.setBackgroundColor(indigo);

        TextView titleView = new TextView(this);
        titleView.setText(getString(R.string.selectABuddy));
        titleView.setTextSize(40);
        titleView.setTypeface(null, Typeface.BOLD_ITALIC);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(green);

        //titleLayout.addView(_addItemButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10));
        titleLayout.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 90));
        //titleLayout.addView(shareButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10));

        // TODO: CREATE A BUDDY PICKING FRAGMENT
        BuddySelectorFragment listFragment = new BuddySelectorFragment();
        listFragment.init();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(13, listFragment);
        transaction.commit();

        Button addButton = new Button(this);
        addButton.setText(getString(R.string.add));
        addButton.setBackgroundResource(R.drawable.button_custom);
        addButton.setTextColor(Color.WHITE);
        addButton.setPadding(5,5,5,5);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GroupMemebers.getInstance().addAllBuddies(BuddySelectorFragment.selectedBuddies);
                closeActivity();
            }
        });

        rootLayout.addView(titleLayout);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(5,5,5,5);

        rootLayout.addView(addButton, buttonParams);
        rootLayout.addView(listLayout);

        setContentView(rootLayout);
    }

    private void closeActivity()
    {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
