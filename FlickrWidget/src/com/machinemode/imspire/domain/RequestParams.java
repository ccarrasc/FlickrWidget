package com.machinemode.imspire.domain;

import java.lang.reflect.Field;

import android.util.Log;

public class RequestParams
{
    private static final String TAG = RequestParams.class.getSimpleName();

    private String gallery_id = new String();
    private int per_page = 100;
    private int page = 1;

    /**
     * Default Constructor - Initializes to the following:<br />
     * gallery_id=""<br />
     * per_page=100<br />
     * page=1<br />
     */
    public RequestParams()
    {
    }

    /**
     * Constructor - Sets the request parameters to be used for issuing an API call
     * 
     * @param galleryId
     *            The ID of the gallery of photos to return
     * @param page
     *            The page of results to return. (default is 1)
     * @param perPage
     *            Number of photos to return per page. The maximum allowed value is 500. (default is
     *            100)
     */
    public RequestParams(String galleryId, int page, int perPage)
    {
        this.gallery_id = galleryId;
        this.page = page;
        this.per_page = perPage;
    }

    public String getGalleryId()
    {
        return gallery_id;
    }

    public void setGalleryId(String gallery_id)
    {
        this.gallery_id = gallery_id;
    }

    public int getPerPage()
    {
        return per_page;
    }

    public void setPerPage(int per_page)
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
