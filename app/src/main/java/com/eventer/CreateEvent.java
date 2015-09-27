package com.eventer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eventer.Adapters.ContactListAdapter;
import com.eventer.AsyncTasks.getJsonFromUrl;
import com.eventer.Objects.Person;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class CreateEvent extends Activity {
    private static final String TAG="CreateEvent";

    public static String eventName = "";

    private List<Person> memberList = new ArrayList<Person>();
    ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


/*        ListView personList = (ListView) findViewById(R.id.memberListView);
        contactListAdapter = new ContactListAdapter(this, memberList);
        personList.setAdapter(contactListAdapter);*/

        final EditText textbox = (EditText) findViewById(R.id.eventName);
        Button createEventContinue = (Button) findViewById(R.id.createEventContinue);
        createEventContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateEvent.this, DateTimeSelect.class);
                String eventName = textbox.getText().toString();
                executeUrl(eventName);
                Log.d(TAG, "http://" + MainActivity.hostIp + ":8080/addEventName?eventName=" + eventName);
                startActivity(intent);
            }
        });



    }

    public void setEventId(String id) {
        //CreateEvent.eventId = id;
        Log.d(TAG, "event ID : " + id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
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

    public void createNewPerson(String name) {
        memberList.add(new Person(name));
        contactListAdapter.notifyDataSetChanged();
    }

    public void removeMember(int position) {
        memberList.remove(position);
        contactListAdapter.notifyDataSetChanged();
    }

    public void executeUrl(String eventName) {
         new getJsonFromUrl(CreateEvent.this,"http://" + MainActivity.hostIp + ":8080/addEventName?eventName=" + eventName).execute();
        CreateEvent.eventName=eventName;
    }
}
