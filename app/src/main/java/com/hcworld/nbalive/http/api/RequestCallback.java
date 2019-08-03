package com.hcworld.nbalive.http.api;

public interface RequestCallback<T> {

    void onSuccess(T t);

    void onFailure(String message);

}
