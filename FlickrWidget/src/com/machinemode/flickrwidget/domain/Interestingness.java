package com.machinemode.flickrwidget.domain;

import java.lang.reflect.Field;

import android.util.Log;

public class Interestingness
{
    private static final transient String TAG = Interestingness.class.getSimpleName();

    private Photos photos = new Photos();
    private String stat = new String();
    private String message = new String();
    private int code;

    public Interestingness()
    {
    }

    public Photos getPhotos()
    {
        return photos;
    }

    public String getStat()
    {
        return stat;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
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
