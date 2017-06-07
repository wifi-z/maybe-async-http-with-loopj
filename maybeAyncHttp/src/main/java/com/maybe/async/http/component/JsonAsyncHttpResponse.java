package com.maybe.async.http.component;

public interface JsonAsyncHttpResponse {
    void onSuccess(RequestToJson requestData);
    void onFailure(RequestToJson requestData);
    void onCompleted(RequestToJson requestData);
    void onProgress(long size, long totalSize, float percent);
}
