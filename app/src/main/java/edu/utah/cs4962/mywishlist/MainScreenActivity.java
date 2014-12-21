package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class MainScreenActivity extends Activity
{
    public static final String MY_LIST_TYPE = "myList";
    public static final String SS_GROUP_TYPE = "ssGroup";
    public static final String BUDDY_LIST_TYPE = "buddyList";
    public static final String ADD_NEW_ITEM = "buddyList";

    public static final String BUDDY_LIST_FILE = "myBuddyList.txt";
    public static final String GROUP_MEMBERS_FILE = "groupMembers.txt";
    public static final String MY_WISH_LIST_FILE = "myWishList.txt";

    public Gson _gson = new Gson();

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

        Button addNewItem = (Button) findViewById(R.id.AddNewItem);
        addNewItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainScreenActivity.this, myListActivity.class);
                intent.putExtra(MY_LIST_TYPE, true);
                intent.putExtra(ADD_NEW_ITEM, true);
                startActivity(intent);
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

    @Override
    protected void onResume()
    {
        super.onResume();

        File fileDir = getFilesDir();

        loadBuddyList(fileDir);
        loadGroupMembers(fileDir);
        loadMyWishList(fileDir);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        File fileDir = getFilesDir();

        saveBuddyList(fileDir);
        saveGroupMembers(fileDir);
        saveMyWishList(fileDir);
    }

    //region <Preserve Buddy List>
    public static void saveBuddyList(File filesDir)
    {
        Gson _gson = new Gson();
        String jsonBuddyList = _gson.toJson(myBuddyList.getInstance().getBuddyList());

        try
        {
            File file = new File(filesDir, BUDDY_LIST_FILE);
            FileWriter textWriter;
            textWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(textWriter);

            // Write the paint points in json format.
            bufferedWriter.write(jsonBuddyList);
            bufferedWriter.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void loadBuddyList(File filesDir)
    {
        Gson _gson = new Gson();
        try {
            File file = new File(filesDir, BUDDY_LIST_FILE);
            FileReader textReader = new FileReader(file);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);

            String jsonBuddyList = bufferedTextReader.readLine();

            Type lineListType = new TypeToken<ArrayList<Buddy>>() {
            }.getType();

            ArrayList<Buddy> buddyList = _gson.fromJson(jsonBuddyList, lineListType);

            bufferedTextReader.close();

            myBuddyList.getInstance().initBuddyList(buddyList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion <Preserve Buddy List>

    //region <Preserve GroupMembers List>
    public static void saveGroupMembers(File filesDir)
    {
        Gson _gson = new Gson();
        String jsonGroupMembers = _gson.toJson(GroupMemebers.getInstance().getSsGroupList());

        try
        {
            File file = new File(filesDir, GROUP_MEMBERS_FILE);
            FileWriter textWriter;
            textWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(textWriter);

            // Write the paint points in json format.
            bufferedWriter.write(jsonGroupMembers);
            bufferedWriter.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void loadGroupMembers(File filesDir)
    {
        Gson _gson = new Gson();
        try {
            File file = new File(filesDir, GROUP_MEMBERS_FILE);
            FileReader textReader = new FileReader(file);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);

            String jsonBuddyList = bufferedTextReader.readLine();

            Type lineListType = new TypeToken<ArrayList<Buddy>>() {
            }.getType();

            ArrayList<Buddy> groupMembersList = _gson.fromJson(jsonBuddyList, lineListType);

            bufferedTextReader.close();

            GroupMemebers.getInstance().initGroupMembers(groupMembersList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion <Preserve GroupMembers List>

    //region <Preserve myWishList List>
    public static void saveMyWishList(File filesDir)
    {
        Gson _gson = new Gson();
        String jsonWishList = _gson.toJson(myWishList.getInstance().getWishList());

        try
        {
            File file = new File(filesDir, MY_WISH_LIST_FILE);
            FileWriter textWriter;
            textWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(textWriter);

            // Write the paint points in json format.
            bufferedWriter.write(jsonWishList);
            bufferedWriter.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void loadMyWishList(File filesDir)
    {
        Gson _gson = new Gson();
        try {
            File file = new File(filesDir, MY_WISH_LIST_FILE);
            FileReader textReader = new FileReader(file);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);

            String jsonBuddyList = bufferedTextReader.readLine();

            Type lineListType = new TypeToken<ArrayList<myWishItem>>() {
            }.getType();

            ArrayList<myWishItem> wishList = _gson.fromJson(jsonBuddyList, lineListType);

            bufferedTextReader.close();

            myWishList.getInstance().initMyWishList(wishList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion <Preserve GroupMembers List>

}
