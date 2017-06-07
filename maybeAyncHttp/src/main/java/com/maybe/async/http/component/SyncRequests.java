package com.maybe.async.http.component;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.maybe.async.http.AsyncHttp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cz.msebera.android.httpclient.Header;

public class SyncRequests {
    private AsyncHttp asyncHttp = null;
    private BlockingQueue<BaseRequest> requestList = null;
    private RequestsResponse response = null;

    public SyncRequests(Builder builder) {
        asyncHttp = builder.asyncHttp;
        requestList = builder.requestList;
        response = builder.response;
    }

    public void sendRequest() {
        try {
            if (!requestList.isEmpty()) {
                final BaseRequest request = requestList.take();
                asyncHttp.sendRequest(request, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        request.onSuccess(statusCode, headers, responseBody);
                        sendRequest();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        request.onFailure(statusCode, headers,
                                responseBody, error);
                        sendRequest();
                    }
                });
            } else {
                if (response != null) {
                    response.requestFinished();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        requestList.clear();
    }

    public static class Builder {
        private AsyncHttp asyncHttp = null;
        private BlockingQueue<BaseRequest> requestList = null;
        private RequestsResponse response = null;

        public Builder(AsyncHttp asyncHttp, RequestsResponse response) {
            this.asyncHttp = asyncHttp;
            this.response = response;
        }

        public Builder addRequest(BaseRequest request) {
            if (requestList == null) {
                requestList = new LinkedBlockingQueue<BaseRequest>();
            }

            try {
                requestList.put(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this;
        }

        public SyncRequests build() {
            return new SyncRequests(this);
        }
    }
}
