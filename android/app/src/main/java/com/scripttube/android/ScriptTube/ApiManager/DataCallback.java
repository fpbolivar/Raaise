package com.scripttube.android.ScriptTube.ApiManager;

public interface DataCallback<T> {
    void onSuccess(T t);
    void onError(ServerError serverError);
}
