package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.ContentResolver;
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
import java.io.File;
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
                String dataType = importData(data);

                if(dataType.equals(getString(R.string.WPM_DATA_TYPE)))
                    Toast.makeText(this, getString(R.string.buddyAddedMessage) ,Toast.LENGTH_SHORT).show();
                else if(dataType.equals(getString(R.string.WPM_DATA_TYPE)))
                    Toast.makeText(this, getString(R.string.imageAddedMessage) ,Toast.LENGTH_SHORT).show();

                finish();

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

    private String importData(Uri data)
    {
        final String scheme = data.getScheme();

        String path = getPath(data);

        String extension = getString(R.string.extension);

       //String p = data.getPath();

        if(data.getPath().endsWith(extension))
        {
            renameImage(path);
            return getString(R.string.PNG_DATA_TYPE);
        }
        else if(path.endsWith(extension))
        {
            renameImage(path);
            return getString(R.string.PNG_DATA_TYPE);
        }
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
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


                Type myWishListType = new TypeToken<Buddy>() {
                }.getType();
                Gson gson = new Gson();

                buddyWishList = gson.fromJson(buf.toString(), myWishListType);

                // perform your data import here…
                saveBuddy();

                return getString(R.string.WPM_DATA_TYPE);

            }catch (Exception e)
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

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }



    public void unzipFile(String filePath)
    {
        try
        {
            ZipFile zf = new ZipFile(filePath);
            Enumeration entries = zf.entries();

            BufferedReader input = new BufferedReader(new InputStreamReader(
                    System.in));

            while (entries.hasMoreElements())
            {
                ZipEntry ze = (ZipEntry) entries.nextElement();

                //System.out.println("Read " + ze.getName() + "?");

                String inputLine = input.readLine();
                if (inputLine.equalsIgnoreCase("yes"))
                {
                    long size = ze.getSize();
                    if (size > 0)
                    {
                        //System.out.println("Length is " + size);

                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(zf.getInputStream(ze)));
                        String line;
                        while ((line = br.readLine()) != null)
                        {
                            System.out.println(line);
                            // Gson should be able to read this string.
                        }
                        br.close();
                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
