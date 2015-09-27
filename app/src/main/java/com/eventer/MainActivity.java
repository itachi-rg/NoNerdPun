package com.eventer;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eventer.AsyncTasks.GetTokenTask;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class MainActivity extends Activity {

    static final String TAG = "MainActivity";
    public static String token;
    public static String hostIp = "10.20.241.38";
    public static String mEmail; // Received from newChooseAccountIntent(); passed to getToken()

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;

    private final static String Gmail_SCOPES = "https://www.googleapis.com/auth/gmail.readonly" + " " +
            "https://www.googleapis.com/auth/gmail.compose" + " " +
            "https://www.googleapis.com/auth/gmail.send" + " " +
            "https://www.googleapis.com/auth/gmail.insert" + " " +
            "https://www.googleapis.com/auth/gmail.labels" + " " +
            "https://mail.google.com/";

    private final static String GooglePlus_SCOPE = "https://www.googleapis.com/auth/plus.login";
    private final static String SCOPE = "oauth2:"+GooglePlus_SCOPE+" "+Gmail_SCOPES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isDeviceOnline()) {
            getUsername();
        } else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }

        Button createEventButton = (Button) findViewById(R.id.createEvent);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateEvent.class);
//                setServerIp();
                startActivity(intent);
            }
        });

        Button mapsButton = (Button) findViewById(R.id.mapsLauncher);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void getUsername() {
        if (MainActivity.mEmail == null) {
            pickUserAccount();
        } else {
            Log.d(TAG, "MainActivity.mEmail : "+MainActivity.mEmail);
            new GetTokenTask(MainActivity.this, MainActivity.mEmail, SCOPE).execute();
        }
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                MainActivity.mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                // With the account name acquired, go get the auth token
                getUsername();
            } else if (resultCode == RESULT_CANCELED) {
                // The account picker dialog closed without selecting an account.
                // Notify users that they must pick an account to proceed.
                Toast.makeText(this, "Cannot proceed without an account", Toast.LENGTH_SHORT).show();
                getUsername();
            }
        } else if ((requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == RESULT_OK) {
            // Receiving a result that follows a GoogleAuthException, try auth again
            getUsername();
        }
    }

    public void handleException(final Exception e) {
        // Because this call comes from the AsyncTask, we must ensure that the following
        // code instead executes on the UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException) e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            MainActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException) e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

    public  void setToken(String tokenValue) {
        MainActivity.token = tokenValue;
        Log.d("MA::setToken", "Token value for google plus apis : " + token);
//        Toast.makeText(this, "Token obtained" + token, Toast.LENGTH_LONG).show();
        /*
        new SendEmail().execute();
        Toast.makeText(this, "Message sent ? " + token, Toast.LENGTH_LONG).show();
        */
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getToken() {
        return token;
    }

    public void setServerIp() {
        final EditText textbox = (EditText) findViewById(R.id.serverIp);
        if (textbox.getText() != null && textbox.getText().toString() != "" ) {
            Log.d(TAG,textbox.getText().toString());
            MainActivity.hostIp=textbox.getText().toString();
        }
    }
}
