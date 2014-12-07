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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Jesus Zarate on 11/25/14.
 */
public class myListActivity extends Activity
{
    static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 15;
    static final int NEW_ITEM_ADDED = 16;
    static final int MEMBER_ADDED = 17;


    myListFragment listFragment;
    SecretSantaGroupFragment secretSantaGroupFragment;
    private ImageButton _addItemButton;

    public static final String MY_WISH_LIST_DIR = "myWishListImages";
    public static final String SIGN_UP_RESULTS = "addnewmember";

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
        titleView.setText("     myList");
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
                if(getIntent().hasExtra(MainScreenActivity.MY_LIST))
                {
                    openCamera();
                }
                else if(getIntent().hasExtra(MainScreenActivity.SS_GROUP))
                {
                    openAddMemberActivity(myListActivity.this);
                }
            }
        });

        if(getIntent().hasExtra(MainScreenActivity.SS_GROUP))
        {
            titleView.setText("mySecret Santa Group");
        }
        titleLayout.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 90));
        titleLayout.addView(_addItemButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10));
        //endregion <Title>


        LinearLayout listLayout = new LinearLayout(this);

        if(getIntent().hasExtra(MainScreenActivity.MY_LIST))
        {
            listFragment = new myListFragment();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, listFragment);
            transaction.commit();
        }
        else if(getIntent().hasExtra(MainScreenActivity.SS_GROUP))
        {
            secretSantaGroupFragment = new SecretSantaGroupFragment();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, secretSantaGroupFragment);
            transaction.commit();
        }

        rootLayout.addView(titleLayout);
        rootLayout.addView(listLayout);


        // Button to assign secret buddies
        if(getIntent().hasExtra(MainScreenActivity.SS_GROUP))
        {
            Button assignBuddiesButton = new Button(this);
            assignBuddiesButton.setText("Assign Secret Buddies");

            assignBuddiesButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    RandomizeBuddies randomizeBuddies = new RandomizeBuddies();
                    randomizeBuddies.randomizer(GroupMemebers.getInstance().getSsGroupList());
                }
            });

            rootLayout.addView(assignBuddiesButton, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        setContentView(rootLayout);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                openNewWishItemActivity(this);
            }
            if (requestCode == NEW_ITEM_ADDED)
            {
                // Refresh the list to reflect the newly added item.
                listFragment.refreshList();
            }
            else if (requestCode == MEMBER_ADDED)
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
