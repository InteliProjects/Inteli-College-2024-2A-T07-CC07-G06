package com.nsync.microservice.model;

import java.util.List;

public class ResponseWrapper<T> {
    private List<T> data;

    public ResponseWrapper() {
    }

    public ResponseWrapper(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
