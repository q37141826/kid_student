package com.fxtx.framework.http.callback;

import com.squareup.okhttp.Request;

public abstract class ResultCallback {

    public void onBefore(Request request) {
    }

    public void onAfter() {
    }

    public void inProgress(float progress) {

    }

    public abstract void onError(Request request, Exception e);

    public abstract void onResponse(String response);


}