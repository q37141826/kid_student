package com.fxtx.framework.http.request;

import android.util.Log;
import android.util.Pair;

import com.fxtx.framework.http.OkHttpClientManager;
import com.fxtx.framework.http.callback.ResultCallback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by zhy on 15/11/6.
 */
public abstract class OkHttpRequest {
    protected OkHttpClientManager mOkHttpClientManager = OkHttpClientManager.getInstance();
    protected OkHttpClient mOkHttpClient;

    protected RequestBody requestBody;
    protected Request request;

    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;

    protected OkHttpRequest(String url, Object tag,
                            Map<String, String> params, Map<String, String> headers) {
        mOkHttpClient = mOkHttpClientManager.getOkHttpClient();
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
    }

    protected abstract Request buildRequest();

    protected abstract RequestBody buildRequestBody();

    protected void prepareInvoked(ResultCallback callback) {
        requestBody = buildRequestBody();
        requestBody = wrapRequestBody(requestBody, callback);
        request = buildRequest();
    }


    public void invokeAsyn(ResultCallback callback) {
        prepareInvoked(callback);
        mOkHttpClientManager.execute(request, callback);
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallback callback) {
        return requestBody;
    }


    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        if (builder == null) {
            throw new IllegalArgumentException("builder can not be empty!");
        }

        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public void cancel() {
        if (tag != null)
            mOkHttpClientManager.cancelTag(tag);
    }


    public static class Builder {
        private String url;
        private Object tag;
        private Map<String, String> headers;
        private Map<String, String> params;
        private Pair<String, File>[] files;
        private MediaType mediaType;
        private String json;

        private String destFileDir;
        private String destFileName;

        private int errorResId = -1;

        //for post
        private String content;
        private byte[] bytes;
        private File file;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder addParams(String key, String val) {
            if (this.params == null) {
                params = new IdentityHashMap<>();
            }
            params.put(key, val);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String val) {
            if (this.headers == null) {
                headers = new IdentityHashMap<>();
            }
            headers.put(key, val);
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public Builder files(Pair<String, File>... files) {
            this.files = files;
            return this;
        }

        public Builder destFileName(String destFileName) {
            this.destFileName = destFileName;
            return this;
        }

        public Builder destFileDir(String destFileDir) {
            this.destFileDir = destFileDir;
            return this;
        }

        public Builder errResId(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public OkHttpRequest get(ResultCallback callback) {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            request.invokeAsyn(callback);
            return request;
        }


        public OkHttpRequest post(ResultCallback callback) {
            Log.d("majin", "url" + url + "  params:" + params);
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, mediaType, content, bytes, file, json);
            request.invokeAsyn(callback);
            return request;
        }

        public OkHttpRequest upload(ResultCallback callback) {
            Log.d("majin", "url" + url + "  params:" + params);
            OkHttpRequest request = new OkHttpUploadRequest(url, tag, params, headers, files);
            request.invokeAsyn(callback);
            return request;
        }

        public OkHttpRequest download(ResultCallback callback) {
            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
            request.invokeAsyn(callback);
            return request;
        }
    }

}
