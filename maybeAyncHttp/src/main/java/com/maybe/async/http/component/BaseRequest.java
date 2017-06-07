package com.maybe.async.http.component;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public abstract class BaseRequest extends AsyncHttpResponseHandler {
	protected Protocol protocol = null;
	protected HttpMethod httpMethod = null;
	protected ParameterData requestParams = null;
	protected Object data = null;
	protected String content = null;
    protected int statusCode;
    private Throwable error = null;
    protected boolean isFinished;
	protected EncodingType encodingType = null;

	public String getUrl() {
		return protocol.getUrl();
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public ParameterData getRequestParams() {
		return requestParams;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public Object getData() {
		return data;
	}

    public int getStatusCode() {
        return statusCode;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
        builder.append("request status code : " + statusCode);
        if (statusCode != 200 && error != null) {
            builder.append("\nrequest error : " + error.getMessage());
        }

		builder.append("\nurl : " + protocol.getUrl());
		builder.append("\nHttpMethod : " + httpMethod.name());
		if (requestParams != null && !requestParams.toString().isEmpty()) {
			builder.append("\nrequest : " + protocol.getUrl() + "?" + requestParams.toString());
		}

		if (content != null) {
			builder.append("\ncontent : " + content);
		}

		return builder.toString();
	}

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        this.statusCode = statusCode;
        isFinished = true;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        this.statusCode = statusCode;
        this.error = error;
        isFinished = true;
    }

}
