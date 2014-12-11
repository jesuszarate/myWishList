package edu.utah.cs4962.mywishlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        TextView passwordLabel = (TextView) findViewById(R.id.passwordLabel);
        EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        Button   addBuddyButton = (Button) findViewById(R.id.addBuddyButton);
        Button   addBuddyFromListButton = (Button) findViewById(R.id.addBuddyFromListButton);

        if (getIntent().hasExtra(myListActivity.SIGN_UP_RESULTS))
        {
            passwordLabel.setVisibility(View.INVISIBLE);
            passwordInput.setVisibility(View.INVISIBLE);
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

                    // Add item to the GroupMemebers list
                    GroupMemebers.getInstance().addBuddy(newBuddy);

                    closeActivity();
                }
            }
        });

        addBuddyFromListButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

    }

    private void closeActivity()
    {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
