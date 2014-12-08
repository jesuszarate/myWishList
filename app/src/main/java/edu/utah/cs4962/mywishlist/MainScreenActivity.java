package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class MainScreenActivity extends Activity
{

    public static final String MY_LIST = "myList";
    public static final String SS_GROUP = "ssGroup";

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
                importData(data);
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
                openWishList(MainScreenActivity.this, MY_LIST);
            }
        });
        Button sSantaGroupButton = (Button) findViewById(R.id.mySecretSantaGroup);
        sSantaGroupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openWishList(MainScreenActivity.this, SS_GROUP);
            }
        });
        Button secretBuddiesButton = (Button) findViewById(R.id.mySecretBuddiesButton);
        secretBuddiesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

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

                //JSONObject json = new JSONObject(buf.toString());

                Type myWishListType = new TypeToken<ArrayList<myWishItem>>() {
                }.getType();
                Gson gson = new Gson();

                ArrayList<myWishItem> linePoints = gson.fromJson(buf.toString(), myWishListType);

                // perform your data import here…

            }catch (Exception e){}
        }
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

//    //region Menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings)
////        {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }
    //endregion Menu
}
