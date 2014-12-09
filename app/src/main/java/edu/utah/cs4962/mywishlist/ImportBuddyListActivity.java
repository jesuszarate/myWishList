package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;

/**
 * Created by Jesus Zarate on 12/7/14.
 */
public class ImportBuddyListActivity extends Activity
{
    Buddy buddyWishList;
    Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.save_buddy_activity);

        data = getIntent().getData();

        Button addBuddyButton = (Button) findViewById(R.id.addBuddyButton);
        addBuddyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Open the buddy list activity
                saveBuddy();

                openMyBuddyList();
            }
        });

        if (data != null)
        {
            getIntent().setData(null);
            try
            {
                importData(data);
            } catch (Exception e)
            {
                // warn user about bad data here
                finish();
                return;
            }
        }
        else {
            // Start Main Screen Activity
            openMainScreenActivity();
        }
    }
    private void openMainScreenActivity()
    {
        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void saveBuddy( )
    {
        myBuddyList.getInstance().setBuddy(buddyWishList);
    }

    private void openMyBuddyList()
    {

        Intent intent = new Intent(this, myListActivity.class);
        intent.putExtra(MainScreenActivity.BUDDY_LIST_TYPE, true);
        startActivity(intent);
    }

    private void importData(Uri data)
    {
        final String scheme = data.getScheme();

        if (ContentResolver.SCHEME_CONTENT.equals(scheme))
        {
            try
            {
                ContentResolver cr = this.getContentResolver();
                InputStream is = cr.openInputStream(data);
                if (is == null) return;

                StringBuffer buf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String str;
                if (is != null)
                {
                    while ((str = reader.readLine()) != null)
                    {
                        buf.append(str + "\n");
                    }
                }
                is.close();

                Type myWishListType = new TypeToken<Buddy>() {
                }.getType();
                Gson gson = new Gson();

                buddyWishList = gson.fromJson(buf.toString(), myWishListType);

                // perform your data import here…

            }catch (Exception e)
            {
                Log.e("Loading .mwl file", e.toString());
            }
        }
    }


}
