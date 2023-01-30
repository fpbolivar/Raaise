package com.raaise.android.ApiManager;

public interface DataCallback<T> {
    void onSuccess(T t);

    void onError(ServerError serverError);
}
