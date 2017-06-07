package com.maybe.async.http.component;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.maybe.async.http.AsyncHttp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import cz.msebera.android.httpclient.Header;

public class AsyncRequests {
    private AsyncHttp asyncHttp = null;
    private BlockingQueue<BaseRequest> requestList = null;
    private RequestsResponse response = null;
    private CountDownLatch countDownLatch = null;

    public AsyncRequests(Builder builder) {
        asyncHttp = builder.asyncHttp;
        requestList = builder.requestList;
        response = builder.response;
        countDownLatch = new CountDownLatch(requestList.size());
    }

    public void sendRequest() {
        while (!requestList.isEmpty()) {
            try {
                final BaseRequest request = requestList.take();

                asyncHttp.sendRequest(request,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers,
                                                  byte[] responseBody) {

                                request.onSuccess(statusCode, headers, responseBody);
                                countDownLatch.countDown();

                                if (AsyncRequests.this.isFinished()) {
                                    response.requestFinished();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers,
                                                  byte[] responseBody, Throwable error) {

                                request.onFailure(statusCode, headers,
                                        responseBody, error);
                                countDownLatch.countDown();

                                if (AsyncRequests.this.isFinished()) {
                                    response.requestFinished();
                                }

                            }
                        });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel() {
        requestList.clear();
    }

    private boolean isFinish() {
        boolean isFinished = true;

        if (countDownLatch.getCount() != 0) {
            isFinished = false;
        }

        return isFinished;
    }

    private boolean isFinished() {
        return isFinish();
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

        public AsyncRequests build() {
            return new AsyncRequests(this);
        }
    }
}
