package com.machinemode.flickrwidget.domain;

public class Interestingness
{
    private Photos photos;
    private String stat;
    private int code;
    private String message;
    
    public Interestingness() { }
    
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
}
