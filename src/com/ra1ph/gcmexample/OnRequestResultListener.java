package com.ra1ph.gcmexample;

public interface OnRequestResultListener {
    public void onSuccess(RequestResult result);
    public void onFail(RequestResult result);
    public void onProgressUpdate(Integer progress);
}
