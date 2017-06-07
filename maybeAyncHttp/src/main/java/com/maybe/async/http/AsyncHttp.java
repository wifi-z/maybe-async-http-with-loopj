package com.maybe.async.http;

import android.content.Context;
import android.util.Log;

import com.maybe.async.http.component.BaseRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.Map;

public class AsyncHttp {
    protected AsyncHttpClient client = null;
    private final int TIME_OUT = 15000;

    public AsyncHttp() {
        initHttpClient();
    }

    public void initHttpClient() {
        client = new AsyncHttpClient();
        client.setTimeout(TIME_OUT);
        client.setConnectTimeout(TIME_OUT);
    }

    public void allowSSLAllHost() {
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
    }

    public void setTimeout(int value) {
        if (client != null) {
            client.setTimeout(value);
        }
    }

    public void setConnectTimeout(int value) {
        if (client != null) {
            client.setConnectTimeout(value);
        }
    }

    public void setUserAgent(String agent) {
        if (client != null) {
            client.setUserAgent(agent);
        }
    }

    public void addHeader(Map<String, String> header) {
        if (client != null) {
            try {
                if (header != null) {
                    Iterator<String> it = header.keySet().iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        String value = header.get(key);
                        client.addHeader(key, value);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addHeader(String key, String value) {
        if (client != null) {
            client.addHeader(key, value);
        }
    }

    public void removeHeader(String key) {
        if (client != null) {
            client.removeHeader(key);
        }
    }

    public void removeAllHeader() {
        if (client != null) {
            client.removeAllHeaders();
        }
    }

    public RequestHandle sendRequest(BaseRequest request) {
        if (request == null || client == null) {
            if (request == null) {
                Log.e("AsyncHttp", "request is null");
            }

            if (client == null) {
                Log.e("AsyncHttp", "client is null");
            }
            return null;
        }

        switch (request.getHttpMethod()) {
            case POST:
                return client.post(request.getUrl(), request.getRequestParams(),
                        request);
            case PUT:
                return client.put(request.getUrl(), request.getRequestParams(),
                        request);
            case GET:
                return client.get(request.getUrl(), request.getRequestParams(),
                        request);
            case DELETE:
                return client.delete(request.getUrl(), request);
            default:
                return null;
        }
    }

    public RequestHandle sendRequest(BaseRequest request,
                                     ResponseHandlerInterface responseHandler) {
        if (request == null || client == null) {
            if (request == null) {
                Log.e("AsyncHttp", "request is null");
            }

            if (client == null) {
                Log.e("AsyncHttp", "client is null");
            }
            return null;
        }

        switch (request.getHttpMethod()) {
            case POST:
                return client.post(request.getUrl(), request.getRequestParams(),
                        responseHandler);
            case PUT:
                return client.put(request.getUrl(), request.getRequestParams(),
                        responseHandler);
            case GET:
                return client.get(request.getUrl(), request.getRequestParams(),
                        responseHandler);
            case DELETE:
                return client.delete(request.getUrl(), responseHandler);
            default:
                return null;
        }
    }

    public void cancelRequests(Context context) {
        if (client != null) {
            client.cancelRequests(context, true);
        }
    }

    public void cancelAllRequest() {
        if (client != null) {
            client.cancelAllRequests(true);
        }
    }

    public <T> void setParameter(Map<String, T> parameter, String key,
                                    T value) {
        if (parameter == null || key == null || value == null) {
            return;
        }

        if (!(value instanceof String)
                && !(value instanceof Integer)
                && !(value instanceof Long)
                && !(value instanceof Float)
                && !(value instanceof Double)
                && !(value instanceof StringBuilder)
                && !(value instanceof File)
                && !(value instanceof ByteArrayInputStream)) {
            try {
                throw new InvalidParameterException(
                        "Valid type is String or File");
            } catch (InvalidParameterException e) {
                e.printStackTrace();
            }
        } else {
            parameter.put(key, value);
        }
    }


    public <T> T deserializeResponse(String content, Class<?> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        T object = null;
        try {
            object = (T) mapper.readValue(content, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }
}
