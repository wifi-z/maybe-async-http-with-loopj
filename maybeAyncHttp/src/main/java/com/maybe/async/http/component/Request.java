package com.maybe.async.http.component;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class Request extends BaseRequest {

	private AsyncHttpResponse response = null;

	private Request(Builder builder) {
		this.protocol = builder.protocol;
		this.httpMethod = builder.httpMethod;
		this.requestParams = builder.requestParams;
		this.response = builder.response;
		this.data = builder.data;
		this.encodingType = builder.encodingType;
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        super.onSuccess(statusCode, headers, responseBody);

		try {
			if (encodingType != null) {
				content = new String(responseBody, encodingType.getType());
			} else {
				content = new String(responseBody);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		if (response != null) {
			response.onSuccess(this);
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			byte[] responseBody, Throwable error) {
        super.onFailure(statusCode, headers, responseBody, error);
        if (response != null) {
            response.onFailure(this);
        }
	}

	public static class Builder {
		private Protocol protocol = null;
		private HttpMethod httpMethod = null;
		private ParameterData requestParams = null;
		private AsyncHttpResponse response = null;
		private EncodingType encodingType = null;
		private Object data = null;

		public Builder(Protocol protocol, HttpMethod httpMethod,
				AsyncHttpResponse response) {
			this.protocol = protocol;
			this.httpMethod = httpMethod;
			this.response = response;
		}

		public Builder addParameter(Map<String, Object> params) {
			if (requestParams == null) {
				requestParams = new ParameterData();
			}

			requestParams.add(params);
			return this;
		}

		public Builder addParameter(String key, String value) {
			if (requestParams == null) {
				requestParams = new ParameterData();
			}

			requestParams.add(key, value);
			return this;
		}

		public Builder addFile(String key, File file) {
			if (requestParams == null) {
				requestParams = new ParameterData();
			}
			requestParams.add(key, file);
			return this;
		}

		public Builder addFiles(String key, ArrayList<File> files) {
			if (requestParams == null) {
				requestParams = new ParameterData();
			}
			requestParams.add(key, files);
			return this;
		}

		public Builder addInputStream(String key, InputStream stream, String name, String type) {
			if (requestParams == null) {
				requestParams = new ParameterData();
			}
			requestParams.add(key, stream, name, type);
			return this;
		}

		public Builder responseEncodingType(EncodingType encodingType) {
			this.encodingType = encodingType;
			return this;
		}

		public Builder Data(Object data) {
			this.data = data;
			return this;
		}

		public Request build() {
			return new Request(this);
		}
	}
}
