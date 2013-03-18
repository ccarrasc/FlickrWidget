package com.machinemode.flickrwidget.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Photos
{
    private static final transient String TAG = Photos.class.getSimpleName();
    
    private int page;
    private int pages;
    private int perpage;
    private int total;
    private List<Photo> photo = new ArrayList<Photo>();
    
    public Photos() { }

    public int getPage()
    {
        return page;
    }

    public int getPages()
    {
        return pages;
    }

    public int getPerpage()
    {
        return perpage;
    }

    public int getTotal()
    {
        return total;
    }

    public List<Photo> getPhoto()
    {
        return photo;
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        Field fields[] = this.getClass().getDeclaredFields();
        
        for(Field field : fields)
        {
            stringBuilder.append(field.getName());
            stringBuilder.append(": ");
            try
            {
                stringBuilder.append(field.get(this));
            }
            catch(IllegalArgumentException e)
            {
                Log.e(TAG, e.getMessage());
                stringBuilder.append("IllegalArgumentException!");
            }
            catch(IllegalAccessException e)
            {
                Log.e(TAG, e.getMessage());
                stringBuilder.append("IllegalAccessException!");
            }
            stringBuilder.append("\n");
        }
        
        return stringBuilder.toString();
    }
}
