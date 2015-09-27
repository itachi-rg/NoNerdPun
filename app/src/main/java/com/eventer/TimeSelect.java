package com.eventer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.eventer.AsyncTasks.getJsonFromUrl;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class TimeSelect extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_select);

        Button timeNext = (Button) findViewById(R.id.date_time_set);

        timeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimeSelect.this, MapsActivity.class);
                setTime();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_select, menu);
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

    public void setTime() {

        try {
            int dayofmonth = getIntent().getIntExtra("date_day", 1);
            int month = getIntent().getIntExtra("date_month", 1);
            int year = getIntent().getIntExtra("date_year", 2015);

            TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
            Calendar calendar = new GregorianCalendar(year, month, dayofmonth,
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute());
            Log.d("time in milliseconds : ", calendar.toString());
            Log.d("some", "some");
            new getJsonFromUrl(TimeSelect.this,"http://" + MainActivity.hostIp + ":8080/addEventTime?eventName=" +
                    CreateEvent.eventName+"&eventTime="+calendar.getTimeInMillis()).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
