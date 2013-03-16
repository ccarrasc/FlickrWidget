package com.machinemode.flickrwidget.domain;


public class Request
{
    private Request() { }
    
    public static String buildRequest(String uri, String api_key, String format, RequestParams params)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(uri);
        stringBuilder.append('&');
        stringBuilder.append("api_key=");
        stringBuilder.append(api_key);
        stringBuilder.append('&');
        stringBuilder.append("format=");
        stringBuilder.append(format);
        stringBuilder.append("&nojsoncallback=1&");
        stringBuilder.append(params.toQueryString());
        
        return stringBuilder.toString();
    }
}
