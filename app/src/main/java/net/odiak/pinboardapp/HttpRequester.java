package net.odiak.pinboardapp;

import java.util.HashMap;
import java.util.Map;

public class HttpRequester {
    public interface SuccessCallback {
        void onRequestSuccess();
    }

    public interface ErrorCallback {
        void onRequestError();
    }

    private String mPath;
    private String mBaseUrl;
    private Map<String, String> mParams;

    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;

    public HttpRequester () {
        mParams = new HashMap();
    }

    public String getPath() { return mPath; }
    public String getBaseUrl() { return mBaseUrl; }
    public Map getParams() { return mParams; }

    public HttpRequester setPath (String path) {
        mPath = path;
        return this;
    }

    public HttpRequester setBaseUrl (String baseUrl) {
        mBaseUrl = baseUrl;
        return this;
    }

    public HttpRequester setSuccessCallback (SuccessCallback callback) {
        mSuccessCallback = callback;
        return this;
    }

    public HttpRequester setErrorCallback (ErrorCallback callback) {
        mErrorCallback = callback;
        return this;
    }

    public <T> HttpRequester setParam (String name, T value) {
        mParams.put(name, String.valueOf(value));
        return this;
    }

    public void end() {
    }

    public static class Result {
    }
}
