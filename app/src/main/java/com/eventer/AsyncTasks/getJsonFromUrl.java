package com.eventer.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.eventer.CreateEvent;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;

public class getJsonFromUrl extends AsyncTask<Void, Void, String> {

    private Activity activity;
    private String postUrlString;

    private HttpURLConnection httpURLConnection;

    //TODO : Use reflection to instantiate particular class of response object instead of passing object only here

    public getJsonFromUrl(Activity activity) {
        this.activity = activity;
    }

    public getJsonFromUrl(Activity activity, String postUrlString) {
        this.activity = activity;
        this.postUrlString = postUrlString;
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        /*
        The order is so !!! very important. If FormHttpMessageConverter is added after Jackson2Http, Bad Error Request Exception is thrown.
        Because this jackson will not consistently convert the Map value to http post request header parameters.

        But Jackson2Http converter is required for conversion of Response Json Object to POJO User.
         */
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
//            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Log.d("postUrlString", postUrlString);
            String response = restTemplate.getForObject(postUrlString, String.class);

            // ResponseEntity<String> responseEntity = restTemplate.getForEntity(postUrlString, null, String.class);
            return response;
        }
        catch (ResourceAccessException re) {
            re.printStackTrace();
        }
        catch (HttpServerErrorException se) {
            se.printStackTrace();
        }
        catch (HttpClientErrorException ce) {
            ce.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();

        }

        return null;
    }

    @Override
    protected void onPostExecute(String output) {
        if (activity instanceof CreateEvent) {
//            ((CreateEvent) activity).setEventName(output);
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getPostUrlString() {
        return postUrlString;
    }

    public void setPostUrlString(String postUrlString) {
        this.postUrlString = postUrlString;
    }

}