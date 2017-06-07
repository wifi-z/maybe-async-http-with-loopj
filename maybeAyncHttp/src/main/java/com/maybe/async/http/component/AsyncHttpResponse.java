package com.maybe.async.http.component;

public interface AsyncHttpResponse {
    void onSuccess(BaseRequest requestData);

    void onFailure(BaseRequest requestData);

    void onCompleted(BaseRequest requestData);
}
