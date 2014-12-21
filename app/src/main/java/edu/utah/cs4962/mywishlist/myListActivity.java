package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
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
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Jesus Zarate on 11/25/14.
 */
public class myListActivity extends Activity
{
    private ProgressDialog progressBar;

    static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 15;
    static final int NEW_ITEM_ADDED = 16;
    static final int MEMBER_ADDED = 17;
    static final int BUDDY_SELECTED = 19;
    private static final int REQUEST_SHARE_DATA = 18;
    public static final int EMAIL_SENT = 107;

    myListFragment listFragment;
    SecretSantaGroupFragment secretSantaGroupFragment;
    private ImageButton _addItemButton;

    public static final String MY_WISH_LIST_DIR = "myWishListImages";
    public static final String SIGN_UP_RESULTS = "addnewmember";
    public static final String EXTRA_SUBJECT = "MY WISH LIST - Assigned Buddy";

    public HashMap<Buddy, Buddy> randomGeneratedBuddies;

    public SendEmailsClass sendEmailsClass;
    ArrayList<String> filesToGarbageCollect = new ArrayList<String>();

    Gson gson = new Gson();

    private static final int MENU_ITEM_ITEM1 = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE)
                || getIntent().hasExtra(MainScreenActivity.MY_LIST_TYPE))
        {
            menu.add(Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, "Refresh");
            return true;
        } else return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case MENU_ITEM_ITEM1:

                if (getIntent().hasExtra(MainScreenActivity.MY_LIST_TYPE))
                {
                    listFragment.refreshList();
                } else if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
                {
                    secretSantaGroupFragment.refreshList();
                }

                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        //region <Title>
        LinearLayout titleLayout = new LinearLayout(this);
        titleLayout.setOrientation(LinearLayout.HORIZONTAL);

        Resources res = getResources();
        int pink = res.getColor(R.color.accent_green);
        int indigo = res.getColor(R.color.primary_light_blue);
        titleLayout.setBackgroundColor(indigo);

        TextView titleView = new TextView(this);
        titleView.setText(getString(R.string.myList));
        titleView.setTextSize(40);
        titleView.setTypeface(null, Typeface.BOLD_ITALIC);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(pink);

        _addItemButton = new ImageButton(this);
        //_addItemButton.setImageResource(R.drawable.plus_sign);
        //_addItemButton.setBackground(getResources().getDrawable(R.drawable.add_button));
        _addItemButton.setImageResource(R.drawable.add_button);
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
                else if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE))

                {
                    openAddMemberActivity(myListActivity.this, MainScreenActivity.SS_GROUP_TYPE);
                }

                // BUDDY LIST
                else if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
                {
                    openAddMemberActivity(myListActivity.this, MainScreenActivity.BUDDY_LIST_TYPE);
                }

            }
        });

        setHeaderTitle(titleView);

        titleLayout.addView(_addItemButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10));
        titleLayout.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 90));
        //endregion <Title>

        LinearLayout listLayout = new LinearLayout(this);
        listLayout.setOrientation(LinearLayout.VERTICAL);
        listLayout.setId(11);

        //region WishList
        if (getIntent().hasExtra(MainScreenActivity.MY_LIST_TYPE))
        {
            // If the user clicks on the add new item open the camera immediately
            // as if the user had pressed the add new button in this list page.
            if (getIntent().hasExtra(MainScreenActivity.ADD_NEW_ITEM))
            {
                openCamera();
            }

            ImageView shareButton = new ImageView(this);
            shareButton.setImageResource(R.drawable.share_button);
            shareButton.setBackgroundColor(Color.TRANSPARENT);
            shareButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    shareMyWishList("", myWishList.getInstance().toBuddy());
                }
            });
            titleLayout.addView(shareButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10));

            listFragment = new myListFragment();

            boolean myList = getIntent().getBooleanExtra(MainScreenActivity.MY_LIST_TYPE, true);
            if (myList)
            {
                listFragment.init(myListFragment.MY_LIST, 0);
            }
            // If the list is coming from a selected buddy
            else
            {
                _addItemButton.setVisibility(View.INVISIBLE);
                shareButton.setVisibility(View.INVISIBLE);

                int selectedItem = getIntent().getIntExtra(SecretSantaGroupFragment.SELECTED_BUDDY, 0);

                titleView.setText(myBuddyList.getInstance().getBuddy(selectedItem).MemberName);
                listFragment.init(myListFragment.BUDDY_LIST, selectedItem);
            }
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, listFragment);
            transaction.commit();
        }
        //endregion WishList

        //region Secret Santa
        else if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE))
        {
            secretSantaGroupFragment = new SecretSantaGroupFragment();
            secretSantaGroupFragment.init(SecretSantaGroupFragment.SECRET_SANTA_LIST);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, secretSantaGroupFragment);
            transaction.commit();
        }
        //endregion Secret Santa

        //region mySecret Buddies
        else if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
        {
            secretSantaGroupFragment = new SecretSantaGroupFragment();
            secretSantaGroupFragment.init(SecretSantaGroupFragment.BUDDY_LIST);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(11, secretSantaGroupFragment);
            transaction.commit();
        }
        //endregion mySecret Buddies

        rootLayout.addView(titleLayout);

        sendEmailsClass = new SendEmailsClass(this);

        //region Button to assign secret buddies
        if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE))
        {
            Button assignBuddiesButton = new Button(this);
            assignBuddiesButton.setText(getString(R.string.assignSecretBuddies));
            //assignBuddiesButton.setBackgroundColor(getResources().getColor(R.color.background_light_blue));
            assignBuddiesButton.setBackgroundResource(R.drawable.button_custom);
            assignBuddiesButton.setTextColor(Color.WHITE);
            assignBuddiesButton.setPadding(5, 5, 5, 5);

            assignBuddiesButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (GroupMemebers.getInstance().getGroupCount() > 2)
                    {
                        // Warn the user about actually sending their secret buddies
                        messageBox(myListActivity.this, "Send Lists?", getString(R.string.sendingListsWarning));

//                        // Randomize and send the lists
//                        RandomizeBuddies randomizeBuddies = new RandomizeBuddies();
//                        randomGeneratedBuddies = randomizeBuddies.randomizer(GroupMemebers.getInstance().getSsGroupList());
//
//                        // Send the emails at this point to the group with their buddy.(Also clear the list)
//                        //sendEmailsClass.sendEmails(randomGeneratedBuddies);
//
//                        sendEmails(randomGeneratedBuddies);
//
//                        GroupMemebers.getInstance().clearList();
                        //emailWithMultipleAttachments();
                    } else
                    {
                        Toast.makeText(myListActivity.this, getString(R.string.moreThanTwoBuddiesWarning),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            LinearLayout.LayoutParams bparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bparams.setMargins(10, 10, 10, 10);
            rootLayout.addView(assignBuddiesButton, bparams);
        }
        //endregion Button to assign secret buddies

        rootLayout.addView(listLayout);
        setContentView(rootLayout);
    }


    public void finalizeSecretBuddies()
    {
        // Randomize and send the lists
        RandomizeBuddies randomizeBuddies = new RandomizeBuddies();
        randomGeneratedBuddies = randomizeBuddies.randomizer(GroupMemebers.getInstance().getSsGroupList());

        // Send the emails at this point to the group with their buddy.(Also clear the list)
        //sendEmailsClass.sendEmails(randomGeneratedBuddies);

        sendEmails(randomGeneratedBuddies);

        GroupMemebers.getInstance().clearList();

        // Stop progress dialog
        progressBar.dismiss();

        //emailWithMultipleAttachments();
    }

    public void messageBox(final Context context, String title, String message)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);

        dlgAlert.setCancelable(true);

        dlgAlert.setNegativeButton("Ok",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        progressBar = new ProgressDialog(myListActivity.this);
                        progressBar.setCancelable(true);
                        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressBar.show();
                        finalizeSecretBuddies();
                    }
                });
        dlgAlert.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                return;
            }
        });
        dlgAlert.create().show();
    }

    private void setHeaderTitle(TextView titleView)
    {
        if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE))
        {
            titleView.setText("mySecret Santa Group");
        } else if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE)
                && !getIntent().hasExtra(MainScreenActivity.MY_LIST_TYPE))
        {
            // Get the wish list extra
            titleView.setText("mySecret Buddy List");
        }
    }

    /**
     * Given the email address whom the user wants to share the
     * wish list with. It will email the list to that email address.
     *
     * @param emailAddress
     * @param buddy
     */
    public void shareMyWishList(String emailAddress, Buddy buddy)//ArrayList<myWishItem> wishList)
    {
        buddy.wishList = compressWishList(buddy.wishList);

        String b = gson.toJson(buddy);

        // create attachment
        //String filename = "MyWishList.mwl";
        String mwl = getString(R.string.MWLExtension);
        String filename = buddy.MemberName + mwl;
        filesToGarbageCollect.add(filename);

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

        if (buddy.wishList != null)
        {
            for (myWishItem wishItem : buddy.wishList)
            {
                filePaths.add(getImageFullPath(wishItem.getImageName()).getAbsolutePath());
            }
        }
        filePaths.add(file.getAbsolutePath());

        emailWithMultipleAttachments(buddy, noRecipients, filePaths);
    }

    public void emailWithMultipleAttachments(Buddy buddy, String[] emailTo, ArrayList<String> filePaths)
    {
        //need to "send multiple" to get more than one attachment
        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                emailTo);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, myListActivity.EXTRA_SUBJECT);
        emailIntent.putExtra(Intent.EXTRA_TEXT, createMessageBody(buddy));
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
        //startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), EMAIL_SENT);
    }

    private String createMessageBody(Buddy buddy)
    {
        String message = "";
        if (buddy.wishList != null)
        {
            //for (myWishItem item : myWishList.getInstance().getWishList())
            for (myWishItem item : buddy.wishList)
            {
                message += item.getItemName() + "\n";
                message += "- Location: " + item.getLocationName() + "\n";
                message += "- Price: " + item.getPrice() + "\n";
                message += "- On Sale: " + (item.isOnSale() ? "Yes" : "No") + "\n";
                message += "- Wanted Level: " + item.getWantLevel() + "\n\n\n";

            }
            return message;
        } else return getString(R.string.NoLIstAvailable);
    }

    public void sendEmails(HashMap<Buddy, Buddy> buddyHashMap)
    {
        if (buddyHashMap != null)
        {
            Iterator iterator = buddyHashMap.entrySet().iterator();

            while (iterator.hasNext())
            {
                Map.Entry<Buddy, Buddy> pair = (Map.Entry) iterator.next();
                Buddy santa = pair.getKey();
                Buddy buddy = pair.getValue();

                shareMyWishList(santa.EmailAddress, buddy);
                //emailWithMultipleAttachments(new String[]{santa.EmailAddress}, gatherFiles(buddy));
            }
        }
    }

    /**
     * Given a buddy, will return a list of paths of every image
     * in the buddy's wish list.
     *
     * @param buddy
     * @return
     */
    private ArrayList<String> gatherFiles(Buddy buddy)
    {
        ArrayList<String> filePaths = new ArrayList<String>();

        for (myWishItem wishItem : buddy.wishList)
        {
            filePaths.add(getImageFullPath(wishItem.getImageName()).getAbsolutePath());
        }
        return filePaths;
    }

    private void openAddMemberActivity(Context context, String ListType)
    {
        Intent addMemberIntent = new Intent(context, SignupActivity.class);
        addMemberIntent.putExtra(SIGN_UP_RESULTS, true);

        if (ListType.equals(MainScreenActivity.SS_GROUP_TYPE))
        {
            addMemberIntent.putExtra(MainScreenActivity.SS_GROUP_TYPE, true);
        } else if (ListType.equals(MainScreenActivity.BUDDY_LIST_TYPE))
        {
            addMemberIntent.putExtra(MainScreenActivity.BUDDY_LIST_TYPE, true);
        }

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
            // Change this external directory with
            // String p = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath();

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
            // My wish list
            if (requestCode == NEW_ITEM_ADDED || requestCode == myListFragment.OPENED_ITEM)
            {
                // Refresh the list to reflect the newly added item.
                listFragment.refreshList();
            }
            // Secret Santa List
            else if (requestCode == MEMBER_ADDED)
            {
                MainScreenActivity.saveGroupMembers(getFilesDir());
                secretSantaGroupFragment.refreshList();
            }
            // Delete the .mwl files that were created to send to the users.
            else if (requestCode == EMAIL_SENT)
            {
                garbageCollect();
            }
        }
        // Delete the .mwl files that were created to send to the users.
        if (requestCode == EMAIL_SENT)
        {
            garbageCollect();
        }

    }

    private void garbageCollect()
    {
//        for(String filename : filesToGarbageCollect)
//        {
//            File file = new File(getExternalCacheDir(), filename);
//            file.delete();
//        }
//        filesToGarbageCollect.clear();
    }

    public void openNewWishItemActivity(Context context)
    {
        Intent openNewWishItemIntent = new Intent(context, myNewWishItemActivity.class);

        openNewWishItemIntent.putExtra(IMAGE_PATH, imagePath);
        startActivityForResult(openNewWishItemIntent, NEW_ITEM_ADDED);
    }

    private ArrayList<myWishItem> compressWishList(ArrayList<myWishItem> wishList)
    {
        if (wishList != null)
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
                tempItem.setCoordinates(item.getCoordinates());

                //tempItem.setJSONBitmapString(getStringFromBitmap(item.getPicture()));

                list.add(tempItem);
            }
            return list;
        }
        return null;
    }

    public File getImageFullPath(String imageName)
    {
        File dir = Environment.getExternalStorageDirectory();

        File imageFile = new File(dir, myListActivity.MY_WISH_LIST_DIR + "/" + imageName);

        return imageFile;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //MainScreenActivity.loadMyWishList(getFilesDir());
        //MainScreenActivity.loadGroupMembers(getFilesDir());
        //MainScreenActivity.loadBuddyList(getFilesDir());
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        MainScreenActivity.saveGroupMembers(getFilesDir());
        MainScreenActivity.saveBuddyList(getFilesDir());
        MainScreenActivity.saveMyWishList(getFilesDir());
    }
}
