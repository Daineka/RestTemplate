package org.example.service;

import java.util.List;

public interface Service<K, T> {
    List<T> getAll();

    T save(T model);

    T get(K key);

    T update(T model);

    T delete(T model);
}