package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jesus Zarate on 12/7/14.
 */
public class SendEmailsClass
{
    private static final int REQUEST_SHARE_DATA = 18;
    Context context;

    public SendEmailsClass(Context context)
    {
        this.context = context;
    }

    public void sendEmails(HashMap<Buddy, Buddy> buddyHashMap)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "abc@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, myListActivity.EXTRA_SUBJECT);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
