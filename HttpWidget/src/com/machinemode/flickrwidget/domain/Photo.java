package com.machinemode.flickrwidget.domain;

import java.lang.reflect.Field;

import android.graphics.Bitmap;
import android.util.Log;

public class Photo
{
    private static final transient String TAG = Photo.class.getSimpleName();
    
    private String id;
    private String owner;
    private String secret;
    private String server;
    private int farm;
    private String title;
    private boolean ispublic;
    private boolean isfriend;
    private boolean isfamily;
    private transient String url;
    private transient Bitmap bitmap;
    
    public Photo() { }

    public String getId()
    {
        return id;
    }

    public String getOwner()
    {
        return owner;
    }

    public String getSecret()
    {
        return secret;
    }

    public String getServer()
    {
        return server;
    }

    public int getFarm()
    {
        return farm;
    }

    public String getTitle()
    {
        return title;
    }

    public boolean isIspublic()
    {
        return ispublic;
    }

    public boolean isIsfriend()
    {
        return isfriend;
    }

    public boolean isIsfamily()
    {
        return isfamily;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }
    
    public static String buildUrl(Photo photo)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://");
        stringBuilder.append("farm").append(String.valueOf(photo.getFarm())).append(".staticflickr.com/");
        stringBuilder.append(photo.getServer()).append('/');
        stringBuilder.append(photo.id).append('_').append(photo.getSecret()).append("_c.jpg");       
        return stringBuilder.toString();
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
