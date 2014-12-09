package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jesus Zarate on 11/25/14.
 */
public class myListActivity extends Activity
{
    static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 15;
    static final int NEW_ITEM_ADDED = 16;
    static final int MEMBER_ADDED = 17;
    private static final int REQUEST_SHARE_DATA = 18;

    myListFragment listFragment;
    SecretSantaGroupFragment secretSantaGroupFragment;
    private ImageButton _addItemButton;

    public static final String MY_WISH_LIST_DIR = "myWishListImages";
    public static final String SIGN_UP_RESULTS = "addnewmember";
    public static final String EXTRA_SUBJECT = "MY WISHLIST - Assigned Buddy";

    public HashMap<Buddy, Buddy> randomGeneratedBuddies;

    public SendEmailsClass sendEmailsClass;

    Gson gson = new Gson();
    private ShareActionProvider mShareActionProvider;


    //region Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share)
        {
            //shareMyWishList(myWishList.getInstance().getWishList());
            shareMyWishList(myWishList.getInstance().toBuddy());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
        int pink = res.getColor(R.color.accent_green);
        int indigo = res.getColor(R.color.primary_light_blue);
        titleLayout.setBackgroundColor(indigo);

        TextView titleView = new TextView(this);
        titleView.setText("myList");
        titleView.setTextSize(40);
        titleView.setTypeface(null, Typeface.BOLD_ITALIC);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(pink);

        _addItemButton = new ImageButton(this);
        _addItemButton.setImageResource(R.drawable.plus_sign);
        _addItemButton.setBackgroundColor(Color.TRANSPARENT);
        _addItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // MY LIST VIEW
                if (getIntent().hasExtra(MainScreenActivity.MY_LIST_TYPE) &&
                        getIntent().getBooleanExtra(MainScreenActivity.MY_LIST_TYPE, true))
                {
                    openCamera();
                }

                // SECRET SANTA VIEW
                else if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE)
                        || getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
                {
                    openAddMemberActivity(myListActivity.this);
                }

            }
        });

        setHeaderTitle(titleView);

        titleLayout.addView(_addItemButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10));
        titleLayout.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 90));
        //endregion <Title>


        LinearLayout listLayout = new LinearLayout(this);

        if (getIntent().hasExtra(MainScreenActivity.MY_LIST_TYPE))
        {
            listFragment = new myListFragment();

            boolean myList = getIntent().getBooleanExtra(MainScreenActivity.MY_LIST_TYPE, true);
            if(myList)
            {
                listFragment.init(myListFragment.MY_LIST, 0);
            }
            else {
                int selectedItem = getIntent().getIntExtra(SecretSantaGroupFragment.SELECTED_BUDDY, 0);
                listFragment.init(myListFragment.BUDDY_LIST, selectedItem);
            }
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, listFragment);
            transaction.commit();
        }
        else if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE))
        {
            secretSantaGroupFragment = new SecretSantaGroupFragment();
            secretSantaGroupFragment.init(SecretSantaGroupFragment.SECRET_SANTA_LIST);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, secretSantaGroupFragment);
            transaction.commit();
        }
        else if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
        {
            secretSantaGroupFragment = new SecretSantaGroupFragment();
            secretSantaGroupFragment.init(SecretSantaGroupFragment.BUDDY_LIST);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, secretSantaGroupFragment);
            transaction.commit();
        }

        rootLayout.addView(titleLayout);
        rootLayout.addView(listLayout);

        sendEmailsClass = new SendEmailsClass(this);
        // Button to assign secret buddies
        if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE))
        {
            Button assignBuddiesButton = new Button(this);
            assignBuddiesButton.setText("Assign Secret Buddies");

            assignBuddiesButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (GroupMemebers.getInstance().getGroupCount() > 2)
                    {
                        RandomizeBuddies randomizeBuddies = new RandomizeBuddies();
                        randomGeneratedBuddies = randomizeBuddies.randomizer(GroupMemebers.getInstance().getSsGroupList());

                        // Send the emails at this point to the group with their buddy.(Also clear the list)
                        //sendEmailsClass.sendEmails(randomGeneratedBuddies);

                        sendEmail(randomGeneratedBuddies);
                    }
                }
            });

            rootLayout.addView(assignBuddiesButton, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        setContentView(rootLayout);
    }


    private void setHeaderTitle(TextView titleView)
    {
        if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE))
        {
            titleView.setText("mySecret Santa Group");
        }
        else if(getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
        {
            // Get the wish list extra
            titleView.setText("mySecret Buddy List");
        }
    }

    public void shareMyWishList(Buddy buddy)//ArrayList<myWishItem> wishList)
    {
        buddy.wishList = compressWishList(buddy.wishList);

        String b = gson.toJson(buddy);

        // create attachment
        String filename = "MyWishList.mwl";

        // Create the correct file name.
        File file = new File(getExternalCacheDir(), filename);

        try
        {
            FileWriter textWriter;

            textWriter = new FileWriter(file);

            BufferedWriter bufferedWriter = new BufferedWriter(textWriter);

            bufferedWriter.write(b);
            bufferedWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if (!file.exists() || !file.canRead())
        {
            Toast.makeText(this, "Problem creating attachment",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String noRecipients = "";
        sendEmail(noRecipients, file);
    }

    private ArrayList<myWishItem> compressWishList(ArrayList<myWishItem> wishList)
    {
        ArrayList<myWishItem> list = new ArrayList<myWishItem>();
        for (myWishItem item : wishList)
        {
            myWishItem tempItem = new myWishItem();
            tempItem.setPicture(null);
            tempItem.setWantLevel(item.getWantLevel());
            tempItem.setImageName(item.getImageName());
            tempItem.setPrice(item.getPrice());
            tempItem.setLocationName(item.getLocationName());
            tempItem.setOnSale(item.isOnSale());

            list.add(tempItem);
        }
        return list;
    }

    private void sendEmail(String recipients, File file)
    {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("application/mwl");

        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, myListActivity.EXTRA_SUBJECT);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, createMessageBody());

        Uri uri = Uri.parse("file://" + file.getAbsolutePath());
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

        //startActivity(emailIntent);
        startActivityForResult(Intent.createChooser(emailIntent,
                        "Email custom data using..."),
                REQUEST_SHARE_DATA);
    }

    private String createMessageBody()
    {
        String message = "";
        for (myWishItem item : myWishList.getInstance().getWishList())
        {
            message += item.getItemName() + "\n";
            message += "- Location: " + item.getLocationName() + "\n";
            message += "- Price: " + item.getPrice() + "\n";
            message += "- On Sale: " + (item.isOnSale() ? "Yes" : "No") + "\n";
            message += "- Wanted Level: " + item.getWantLevel() + "\n\n\n";

        }
        return message;
    }

    public void sendEmail(HashMap<Buddy, Buddy> buddyHashMap)
    {

        String recipient = "",
                subject = "Sharing example",
                message = "";

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");

        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{recipient});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

        // create attachment
        String filename = "example.mytype";

        // Create the correct file name.
        File file = new File(getFilesDir(), filename);
        FileWriter textWriter;

        try
        {
            textWriter = new FileWriter(file);

            BufferedWriter bufferedWriter = new BufferedWriter(textWriter);

            //TODO: Write the list right here.
            String myWishList = gson.toJson("my_wish_list_goes_here");

            bufferedWriter.write(myWishList);
            bufferedWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if (!file.exists() || !file.canRead())
        {
            Toast.makeText(this, "Problem creating attachment",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse("file://" + file.getAbsolutePath());
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivityForResult(Intent.createChooser(emailIntent,
                        "Email custom data using..."),
                REQUEST_SHARE_DATA);

//
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                "mailto", "abc@gmail.com", null));
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, EXTRA_SUBJECT);
//
//        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void openAddMemberActivity(Context context)
    {
        Intent addMemberIntent = new Intent(context, SignupActivity.class);
        addMemberIntent.putExtra(SIGN_UP_RESULTS, true);
        startActivityForResult(addMemberIntent, MEMBER_ADDED);
    }

    public static final String LATEST_IMAGE = "l0a1t2e3s4t5.png";
    public static final String IMAGE_PATH = "imagePath";
    public String imagePath;

    private void openCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), MY_WISH_LIST_DIR);
            imagesFolder.mkdirs();

            File image = new File(imagesFolder, LATEST_IMAGE);
            Uri uriSavedImage = Uri.fromFile(image);

            imagePath = image.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                openNewWishItemActivity(this);
            }
            if (requestCode == NEW_ITEM_ADDED)
            {
                // Refresh the list to reflect the newly added item.
                listFragment.refreshList();
            } else if (requestCode == MEMBER_ADDED)
            {
                secretSantaGroupFragment.refreshList();
            }
        }
    }

    public static final String BITMAP = "bitmap";

    public void openNewWishItemActivity(Context context)
    {
        Intent openNewWishItemIntent = new Intent(context, myNewWishItemActivity.class);

        openNewWishItemIntent.putExtra(IMAGE_PATH, imagePath);
        startActivityForResult(openNewWishItemIntent, NEW_ITEM_ADDED);
    }
}
