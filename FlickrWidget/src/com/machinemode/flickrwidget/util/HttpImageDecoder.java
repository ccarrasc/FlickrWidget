package com.machinemode.flickrwidget.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public final class HttpImageDecoder
{
    private static final String TAG = HttpImageDecoder.class.getSimpleName();
    private static final BitmapFactory.Options OPTIONS = new BitmapFactory.Options();
    private static final int MAX_WIDTH = 2048;
    private static final int MAX_HEIGHT = 2048;

    /**
     * No instantiate constructor
     */
    private HttpImageDecoder()
    {
    }

    /**
     * 
     * @param urlString
     * @param width
     *            Desired image width in pixels
     * @param height
     *            Desired image height in pixels
     * @return
     */
    public static Bitmap decodeUrl(String urlString, int width, int height)
    {
        Bitmap bitmap = null;

        try
        {
            URL url = new URL(urlString);
            InputStream inputStream = (InputStream)url.getContent();

            OPTIONS.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, OPTIONS);

            if(OPTIONS.outWidth > 0 && OPTIONS.outHeight > 0 && OPTIONS.outWidth < MAX_WIDTH
                    && OPTIONS.outHeight < MAX_HEIGHT)
            {
                OPTIONS.inSampleSize = calculateInSampleSize(OPTIONS, width, height);
                OPTIONS.inJustDecodeBounds = false;
                inputStream = (InputStream)url.getContent();
                Bitmap sampledBitmap = BitmapFactory.decodeStream(inputStream, null, OPTIONS);

                if(sampledBitmap != null)
                {
                    bitmap = Bitmap.createScaledBitmap(sampledBitmap, width, height, false);
                }
                else
                {
                    Log.w(TAG, "sampledBitmap is null!?!?!");
                }
            }
            else
            {
                Log.i(TAG, "Disgarding " + urlString + " since it's too big!");
            }
        }
        catch(MalformedURLException e)
        {
            Log.e(TAG + ":MalformedURLException", e.getMessage() + ":" + urlString);
        }
        catch(IOException e)
        {
            Log.e(TAG + ":IOException", e.getMessage() + ":" + urlString);
        }

        return bitmap;
    }

    /**
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return The calculated sample size
     * @see <a
     *      href="http://developer.android.com/training/displaying-bitmaps/load-bitmap.html#load-bitmap">Load
     *      a Scaled Down Version into Memory</a>
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
            int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(reqWidth <= 0)
        {
            reqWidth = 1;
        }

        if(reqHeight <= 0)
        {
            reqHeight = 1;
        }

        if(height > reqHeight || width > reqWidth)
        {
            if(width > height)
            {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            }
            else
            {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }
}
