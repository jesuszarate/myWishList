package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Jesus Zarate on 11/27/14.
 */
public class SignupActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_activity);

        final EditText userName = (EditText) findViewById(R.id.userNameInputText);
        final EditText emailAddress = (EditText) findViewById(R.id.emailAddress);

	    // Make it so the keyboard treats it like an email
	    emailAddress.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        Button addBuddyButton = (Button) findViewById(R.id.addBuddyButton);
        Button addBuddyFromListButton = (Button) findViewById(R.id.addBuddyFromListButton);

        Button signUpButton = (Button) findViewById(R.id.signUp);

        if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
        {
            addBuddyFromListButton.setVisibility(View.GONE);
            signUpButton.setVisibility(View.GONE);

        }
        else if (getIntent().hasExtra(ImportBuddyListActivity.SIGN_UP_TYPE))
        {
            addBuddyButton.setVisibility(View.GONE);
            addBuddyFromListButton.setVisibility(View.GONE);
        }

        addBuddyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (userName.getText().length() > 0 && emailAddress.getText().length() > 0)
                {
                    Buddy newBuddy = new Buddy();
                    newBuddy.EmailAddress = emailAddress.getText().toString();
                    newBuddy.MemberName = userName.getText().toString();

                    //If this activity was opened from the secret santa activity
                    if (getIntent().hasExtra(MainScreenActivity.SS_GROUP_TYPE))
                    {
                        // Add item to the GroupMembers list
                        GroupMemebers.getInstance().addBuddy(newBuddy);
                    }

                    // else if the activity was opened from the buddy activity
                    else if (getIntent().hasExtra(MainScreenActivity.BUDDY_LIST_TYPE))
                    {
                        myBuddyList.getInstance().setBuddy(newBuddy);
                    }

                    closeActivity();
                }
            }
        });

        addBuddyFromListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openBuddySelectorActivity();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                myWishList.getInstance().setUserName(userName.getText().toString());
                myWishList.getInstance().setEmailAddress(emailAddress.getText().toString());

                ImportBuddyListActivity.saveUser(getFilesDir());

                ImportBuddyListActivity.openMainScreenActivity(SignupActivity.this);
            }
        });
    }

    public static final int BUDDY_SELECTOR = 18;
    private void openBuddySelectorActivity()
    {
        Intent buddySelector = new Intent(this, BuddySelectorActivity.class);

        startActivityForResult(buddySelector, BUDDY_SELECTOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == BUDDY_SELECTOR && resultCode == RESULT_OK )
        {
            closeActivity();
        }
    }

    private void closeActivity()
    {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
