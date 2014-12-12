package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainScreenActivity extends Activity
{
    public static final String MY_LIST_TYPE = "myList";
    public static final String SS_GROUP_TYPE = "ssGroup";
    public static final String BUDDY_LIST_TYPE = "buddyList";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Uri data = getIntent().getData();
        if (data != null)
        {
            getIntent().setData(null);
            try
            {
                //importData(data);
            } catch (Exception e)
            {
                // warn user about bad data here
                finish();
                return;
            }
        }
        Button wishListButton = (Button) findViewById(R.id.myWishListButton);
        wishListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openWishList(MainScreenActivity.this, MY_LIST_TYPE);
            }
        });
        Button sSantaGroupButton = (Button) findViewById(R.id.mySecretSantaGroup);
        sSantaGroupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openWishList(MainScreenActivity.this, SS_GROUP_TYPE);
            }
        });

        Button secretBuddiesButton = (Button) findViewById(R.id.mySecretBuddiesButton);
        secretBuddiesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openWishList(MainScreenActivity.this, BUDDY_LIST_TYPE);
            }
        });

    }

    private void openWishList(Context context, String TypeOfList)
    {
        Intent intent = new Intent(context, myListActivity.class);
        intent.putExtra(TypeOfList, true);
        startActivity(intent);
    }

    private void openSecretSantaGroup(Context context)
    {
        Intent intent = new Intent(context, myNewWishItemActivity.class);
        startActivity(intent);
    }

}
