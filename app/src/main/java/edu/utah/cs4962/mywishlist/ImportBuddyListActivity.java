package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Jesus Zarate on 12/7/14.
 */
public class ImportBuddyListActivity extends Activity
{
    public static final String SIGN_UP_TYPE = "signUpType";
    public static final String USER_FILE = "signUpType";
    Buddy buddyWishList;
    Uri data;

    Gson _gson = new Gson();

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

                //openMyBuddyList();
                openMainScreenActivity(ImportBuddyListActivity.this);
            }
        });

        if (data != null)
        {
            getIntent().setData(null);
            try
            {
                String dataType = importData(data);

                if (dataType.equals(getString(R.string.WPM_DATA_TYPE)))
                {
                    MainScreenActivity.saveBuddyList(getFilesDir());
                    Toast.makeText(this, getString(R.string.buddyAddedMessage), Toast.LENGTH_SHORT).show();
                } else if (dataType.equals(getString(R.string.WPM_DATA_TYPE)))
                    Toast.makeText(this, getString(R.string.imageAddedMessage), Toast.LENGTH_SHORT).show();

                finish();

            } catch (Exception e)
            {
                Toast.makeText(this, getString(R.string.wishListCouldNotBeAdded), Toast.LENGTH_SHORT).show();
                // warn user about bad data here
                finish();
                return;
            }
        } else
        {

            // If there's a user then load its information.
            loadUser(getFilesDir());

            if (myWishList.getInstance().getUserName() == null)
            {
                openSignUpActivity();
            } else
            {
                // Start Main Screen Activity
                openMainScreenActivity(this);
            }
        }
    }

    private void openSignUpActivity()
    {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra(SIGN_UP_TYPE, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public static void openMainScreenActivity(Context context)
    {
        Intent intent = new Intent(context, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void saveBuddy()
    {
        myBuddyList.getInstance().setBuddy(buddyWishList);
    }

    private void openMyBuddyList()
    {

        Intent intent = new Intent(this, myListActivity.class);
        intent.putExtra(MainScreenActivity.BUDDY_LIST_TYPE, true);
        startActivity(intent);
    }

    private String importData(Uri data)
    {
        final String scheme = data.getScheme();

        String path = getPath(data);

        String extension = getString(R.string.extension);


        if (data.getPath().endsWith(extension))
        {
            renameImage(path);
            return getString(R.string.PNG_DATA_TYPE);
        } else if (path.endsWith(extension))
        {
            renameImage(path);
            return getString(R.string.PNG_DATA_TYPE);
        } else //if (ContentResolver.SCHEME_CONTENT.equals(scheme))
        {
            try
            {
                ContentResolver cr = this.getContentResolver();
                InputStream is = cr.openInputStream(data);
                if (is == null)
                    return null;

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


                Type myWishListType = new TypeToken<Buddy>()
                {
                }.getType();
                Gson gson = new Gson();

                buddyWishList = gson.fromJson(buf.toString(), myWishListType);

                // perform your data import here…
                saveBuddy();

                return getString(R.string.WPM_DATA_TYPE);

            } catch (Exception e)
            {
                Log.e("Loading .mwl file", e.toString());
            }
        }
        return null;
    }

    private String renameImage(String path)
    {
        String regex = getString(R.string.image_regular_expression);
        String[] regexArray = path.split(regex);

        File dir = Environment.getExternalStorageDirectory();
        if (dir.exists())
        {
            File from = new File(path);

            File to = new File(dir,
                    myListActivity.MY_WISH_LIST_DIR + "/" + regex + regexArray[1] + regex + ".png");

            if (from.exists())
            {
                from.renameTo(to);
            }
        }
        return null;
    }

    public String getPath(Uri uri)
    {
        // just some safety built in
        if (uri == null)
        {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null)
        {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //loadUser(getFilesDir());
    }

    private void loadUser(File filesDir)
    {
        try
        {
            File file = new File(filesDir, USER_FILE);
            FileReader textReader = new FileReader(file);
            BufferedReader bufferedTextReader = new BufferedReader(textReader);

            String jsonBuddyList = bufferedTextReader.readLine();

            Type lineListType = new TypeToken<User>()
            {
            }.getType();

            User user = _gson.fromJson(jsonBuddyList, lineListType);

            bufferedTextReader.close();

            myWishList.getInstance().setUserName(user.userName);
            myWishList.getInstance().setEmailAddress(user.emailAddress);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveUser(File filesDir)
    {
        Gson _gson = new Gson();
        String jsonUser = _gson.toJson(myWishList.getInstance().getUser());

        try
        {
            File file = new File(filesDir, USER_FILE);
            FileWriter textWriter;
            textWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(textWriter);

            // Write the paint points in json format.
            bufferedWriter.write(jsonUser);
            bufferedWriter.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
