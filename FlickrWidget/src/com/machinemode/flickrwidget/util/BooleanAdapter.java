package com.machinemode.flickrwidget.util;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class BooleanAdapter extends TypeAdapter<Boolean>
{

    @Override
    public Boolean read(JsonReader reader) throws IOException
    {
        switch(reader.peek())
        {
            case NULL:
                return false;
            case BOOLEAN:
                return reader.nextBoolean();
            case NUMBER:
                return reader.nextInt() != 0;
            case STRING:
                return Boolean.parseBoolean(reader.nextString());
            default:
                throw new IllegalStateException("Expected a BOOLEAN or NUMBER but was " + reader);
        }
    }

    @Override
    public void write(JsonWriter writer, Boolean value) throws IOException
    {
        if(value == null)
        {
            writer.nullValue();
        }
        else
        {
            writer.value(value);
        }
    }

}
