package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
//        if (id == R.id.action_share)
//        {
//
//
//            //MediaStore.Images.Media.getBitmap(getContentResolver());//getContentResolver(), yourBitmap, yourTitle , yourDescription);
//
//            //shareMyWishList(myWishList.getInstance().getWishList());
//            shareMyWishList(myWishList.getInstance().toBuddy());
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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

        ImageView shareButton = new ImageView(this);
        shareButton.setImageResource(R.drawable.share);
        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                shareMyWishList("", myWishList.getInstance().toBuddy());
            }
        });

        setHeaderTitle(titleView);

        titleLayout.addView(_addItemButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10));
        titleLayout.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 90));
        titleLayout.addView(shareButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10));
        //endregion <Title>


        LinearLayout listLayout = new LinearLayout(this);

        if (getIntent().hasExtra(MainScreenActivity.MY_LIST_TYPE))
        {
            listFragment = new myListFragment();

            boolean myList = getIntent().getBooleanExtra(MainScreenActivity.MY_LIST_TYPE, true);
            if (myList)
            {
                listFragment.init(myListFragment.MY_LIST, 0);
            } else
            {
                int selectedItem = getIntent().getIntExtra(SecretSantaGroupFragment.SELECTED_BUDDY, 0);
                listFragment.init(myListFragment.BUDDY_LIST, selectedItem);
            }
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, listFragment);
            transaction.commit();
        } else if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE))
        {
            secretSantaGroupFragment = new SecretSantaGroupFragment();
            secretSantaGroupFragment.init(SecretSantaGroupFragment.SECRET_SANTA_LIST);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, secretSantaGroupFragment);
            transaction.commit();
        } else if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
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

                        sendEmails(randomGeneratedBuddies);
                        //emailWithMultipleAttachments();
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
        } else if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
        {
            // Get the wish list extra
            titleView.setText("mySecret Buddy List");
        }
    }

    /**
     * Given the email address whom the user wants to share the
     * wish list with. It will email the list to that email address.
     * @param emailAddress
     * @param buddy
     */
    public void shareMyWishList(String emailAddress, Buddy buddy)//ArrayList<myWishItem> wishList)
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

        String[] noRecipients = {emailAddress};
        //sendEmail(noRecipients, file);

        ArrayList<String> filePaths = new ArrayList<String>();

        for(myWishItem wishItem : buddy.wishList)
        {
            filePaths.add(getImageFullPath(wishItem.getImageName()).getAbsolutePath());
        }

        filePaths.add(file.getAbsolutePath());

        emailWithMultipleAttachments(noRecipients, filePaths);
    }

    public void emailWithMultipleAttachments(String[] emailTo, ArrayList<String> filePaths)
    {
        //need to "send multiple" to get more than one attachment
        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                emailTo);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, myListActivity.EXTRA_SUBJECT);
        emailIntent.putExtra(Intent.EXTRA_TEXT, createMessageBody());
        //has to be an ArrayList
        ArrayList<Uri> uris = new ArrayList<Uri>();
        //convert from paths to Android friendly Parcelable Uri's
        for (String file : filePaths)
        {
            File fileIn = new File(file);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
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

    public void sendEmails(HashMap<Buddy, Buddy> buddyHashMap)
    {
        Iterator iterator = buddyHashMap.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<Buddy, Buddy> pair = (Map.Entry)iterator.next();
            Buddy santa = pair.getKey();
            Buddy buddy = pair.getValue();

            shareMyWishList(santa.EmailAddress, buddy);
            //emailWithMultipleAttachments(new String[]{santa.EmailAddress}, gatherFiles(buddy));
        }
    }

    /**
     * Given a buddy, will return a list of paths of every image
     * in the buddy's wish list.
     * @param buddy
     * @return
     */
    private ArrayList<String> gatherFiles(Buddy buddy)
    {
        ArrayList<String> filePaths = new ArrayList<String>();

        for(myWishItem wishItem : buddy.wishList)
        {
            filePaths.add(getImageFullPath(wishItem.getImageName()).getAbsolutePath());
        }
        return filePaths;
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
            tempItem.setItemName(item.getItemName());

            //tempItem.setJSONBitmapString(getStringFromBitmap(item.getPicture()));

            list.add(tempItem);
        }
        return list;
    }

    public File getImageFullPath(String imageName)
    {
        File dir = Environment.getExternalStorageDirectory();

        File imageFile = new File(dir, myListActivity.MY_WISH_LIST_DIR + "/" + imageName);

        return imageFile;
    }
}
