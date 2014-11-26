package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jesus Zarate on 11/25/14.
 */
public class myListActivity extends Activity
{
    myListFragment listFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setId(11);

        //region <Title>
        LinearLayout titleLayout = new LinearLayout(this);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);

        Resources res = getResources();
        int pink = res.getColor(R.color.pink);
        int indigo = res.getColor(R.color.indigo);
        titleLayout.setBackgroundColor(indigo);

        TextView titleView = new TextView(this);
        titleView.setText("     myList");
        titleView.setTextSize(40);
        titleView.setTypeface(null, Typeface.BOLD_ITALIC);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(pink);

        ImageButton addItemButton = new ImageButton(this);
        addItemButton.setImageResource(R.drawable.plus_sign);
        addItemButton.setBackgroundColor(Color.TRANSPARENT);
        addItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openCamera();
            }
        });

        titleLayout.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 90));
        titleLayout.addView(addItemButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10));
        //region <Title>

        listFragment = new myListFragment();

        LinearLayout listLayout = new LinearLayout(this);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(11, listFragment);
        transaction.commit();


        rootLayout.addView(titleLayout);
        rootLayout.addView(listLayout);
        setContentView(rootLayout);
    }

    private void openCamera()
    {

    }
}
