package com.eventer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.eventer.Adapters.ContactListAdapter;
import com.eventer.AsyncTasks.SendEmail;
import com.eventer.AsyncTasks.getJsonFromUrl;
import com.eventer.Objects.Person;

import java.util.ArrayList;
import java.util.List;


public class AddContacts extends Activity {

    ContactListAdapter contactListAdapter;
    private List<String> memberList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        ListView personList = (ListView) findViewById(R.id.memberListView);
        contactListAdapter = new ContactListAdapter(this, memberList);
        personList.setAdapter(contactListAdapter);


        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPerson();
            }
        });

        findViewById(R.id.finishEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmails();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contacts, menu);
        return true;
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

    public void createNewPerson() {
        final EditText textbox = (EditText) findViewById(R.id.selectContact);
        memberList.add(textbox.getText().toString());
        contactListAdapter.notifyDataSetChanged();
        new getJsonFromUrl(AddContacts.this,"http://" + MainActivity.hostIp + ":8080/addPerson?eventName=" +
                CreateEvent.eventName+"&email="+textbox.getText().toString()).execute();
    }

    public void sendEmails() {
        for (String receiver : memberList) {
            new SendEmail(receiver).execute();
        }
        Intent intent = new Intent(AddContacts.this, MainActivity.class);
        startActivity(intent);
    }
}
