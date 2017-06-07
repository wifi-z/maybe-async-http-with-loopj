package com.maybe.async.http.component;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ParameterData extends RequestParams {

    public void add(Map<String, Object> params) {
        try {
            if (params != null) {
                Iterator<String> it = params.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    Object value = params.get(key);
                    if (value instanceof String) {
                        put(key, value);
                    } else if (value instanceof Integer) {
                        put(key, value);
                    } else if (value instanceof StringBuilder) {
                        put(key, value);
                    } else if (value instanceof File) {
                        put(key, (File) value, null, null);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(String key, File file) {
        try {
            put(key, file, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void add(String key, InputStream stream, String name, String type) {
        if (key != null && stream != null && name != null) {
            put(key, stream, name, type);
        }
    }

    public void add(String key, ArrayList<File> files) {
        try {
            for (File file : files) {
                put(key, file, null, null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
