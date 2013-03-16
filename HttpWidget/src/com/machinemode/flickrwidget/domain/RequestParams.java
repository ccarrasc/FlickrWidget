package com.machinemode.flickrwidget.domain;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class RequestParams
{
    private static final String TAG = RequestParams.class.getSimpleName();
    private String gallery_id = new String();
    private Date date = new Date();
    private int per_page;
    private int page;

    public RequestParams() { }
    
    public String getGallery_id()
    {
        return gallery_id;
    }

    public void setGallery_id(String gallery_id)
    {
        this.gallery_id = gallery_id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getPer_page()
    {
        return per_page;
    }

    public void setPer_page(int per_page)
    {
        this.per_page = per_page;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public String toQueryString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("gallery_id=");
        stringBuilder.append(gallery_id);
        stringBuilder.append('&');
        
        //stringBuilder.append("date=");
        //stringBuilder.append(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date));
        //stringBuilder.append('&');
        
        stringBuilder.append("per_page=");
        stringBuilder.append(String.valueOf(per_page));
        stringBuilder.append('&');
        
        stringBuilder.append("page=");
        stringBuilder.append(String.valueOf(page));
        
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
