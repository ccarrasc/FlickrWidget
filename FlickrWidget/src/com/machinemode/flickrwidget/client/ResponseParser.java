package com.machinemode.flickrwidget.client;

import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.machinemode.flickrwidget.domain.Interestingness;
import com.machinemode.flickrwidget.util.BooleanAdapter;

public class ResponseParser
{
    private static final String TAG = ResponseParser.class.getSimpleName();
    
    private ResponseParser() { }
    
    public static Interestingness readJsonFeed(InputStream inputStream)
    {
        Interestingness interestingness = new Interestingness();
        InputStreamReader json = new InputStreamReader(inputStream);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(boolean.class, new BooleanAdapter());
        Gson gson = builder.create();
        
        try
        {
            interestingness = gson.fromJson(json, Interestingness.class);
        }
        catch(JsonSyntaxException e)
        {
            Log.e(TAG, "JsonSyntaxException: " + e.getMessage());
        }
        catch(JsonIOException e)
        {
            Log.e(TAG, "JsonIOException: " + e.getMessage());
        }
        
        return interestingness;
    }
}
