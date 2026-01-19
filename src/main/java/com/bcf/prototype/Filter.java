package com.bcf.prototype;

public interface Filter {

    boolean add(String key);

    boolean hasKey(String key);

    boolean delete(String key);
}
