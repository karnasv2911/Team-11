package com.ichangemycity.ichangemycommunity.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatter implements JsonDeserializer<Date>,
        JsonSerializer<Date> {

    private final DateFormat[] formats;
    /** */
    public static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"; //$NON-NLS-1$
    /** */
    public static String DATE_FORMAT_V2_1 = "yyyy/MM/dd HH:mm:ss Z"; //$NON-NLS-1$
    /** */
    public static String DATE_FORMAT_V2_2 = "yyyy-MM-dd'T'HH:mm:ss"; //$NON-NLS-1$

    public static String DATE_FORMAT_V2_3 = "yyyy-MM-dd"; //$NON-NLS-1$

    /**
     * Create date formatter
     */
    public DateFormatter() {
        formats = new DateFormat[4];
        formats[0] = new SimpleDateFormat(DATE_FORMAT);
        formats[1] = new SimpleDateFormat(DATE_FORMAT_V2_1);
        formats[2] = new SimpleDateFormat(DATE_FORMAT_V2_2);
        formats[3] = new SimpleDateFormat(DATE_FORMAT_V2_3);
        final TimeZone timeZone = TimeZone.getTimeZone("UTC"); //$NON-NLS-1$
        for (DateFormat format : formats)
            format.setTimeZone(timeZone);
    }

    public Date deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonParseException exception = null;
        final String value = json.getAsString();
        for (DateFormat format : formats)
            try {
                synchronized (format) {
                    return format.parse(value);
                }
            } catch (ParseException e) {
                exception = new JsonParseException(e);
            }
        throw exception;
    }

    public JsonElement serialize(Date date, Type type,
                                 JsonSerializationContext context) {
        final DateFormat primary = formats[0];
        String formatted;
        synchronized (primary) {
            formatted = primary.format(date);
        }
        return new JsonPrimitive(formatted);
    }
}
