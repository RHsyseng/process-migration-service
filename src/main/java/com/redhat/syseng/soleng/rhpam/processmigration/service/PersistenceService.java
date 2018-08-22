package com.redhat.syseng.soleng.rhpam.processmigration.service;

import java.util.List;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Identifiable;

public interface PersistenceService<T extends Identifiable> {

    T save(T object);

    T delete(Long id);

    T get(Long id);

    List<T> findAll();

    List<T> findByQuery(String query);

}
