package com.redhat.syseng.soleng.rhpam.processmigration.service.impl.jsondb;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Identifiable;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PersistenceService;
import io.jsondb.JsonDBTemplate;
import org.jboss.logging.Logger;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

public abstract class PersistenceServiceImpl<T extends Identifiable> implements PersistenceService<T> {

    private static final Logger logger = Logger.getLogger(PersistenceServiceImpl.class);

    // Actual location on disk for database files, process should have read-write
    // permissions to this folder
    @Inject
    @ConfigurationValue("db.path")
    private String dbFilesLocation;

    // Java package name where POJO's are present
    private String baseScanPackage = "com.redhat.syseng.soleng.rhpam.processmigration.model";

    private JsonDBTemplate jsonDBTemplate;

    private final Class<T> clazz;

    private Long id = 1L;

    public PersistenceServiceImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @PostConstruct
    public void initialize() {
        if (dbFilesLocation == null || dbFilesLocation.trim().isEmpty()) {
            dbFilesLocation = System.getProperty("java.io.tmpdir") + File.separator + "migration-db";
        }
        jsonDBTemplate = new JsonDBTemplate(dbFilesLocation, baseScanPackage);
        if (!jsonDBTemplate.collectionExists(clazz)) {
            jsonDBTemplate.createCollection(clazz);
            logger.debugv("Created collection of {0}", clazz);
        } else {
            Optional<T> max = jsonDBTemplate.findAll(clazz).stream().max((x, y) -> x.getId().compareTo(y.getId()));
            if (max.isPresent()) {
                id = max.get().getId() + 1;
            }
            logger.debugv("Recovered existing collection of {0} and id {1}", clazz, id);
        }
    }

    public T save(T object) {
        if (object.getId() == null) {
            object.setId(id++);
            jsonDBTemplate.insert(object);
            logger.debugv("Inserted object with id {0} - [{1}]", object.getId(), object);
        } else {
            jsonDBTemplate.save(object, clazz);
            logger.debugv("Saved object with id {0} - [{1}]", object.getId(), object);
        }
        return object;
    }

    public T delete(Long id) {
        T object = get(id);
        if (object != null) {
            jsonDBTemplate.remove(object, clazz);
            return object;
        }
        return null;
    }

    public T get(Long id) {
        return jsonDBTemplate.findById(id, clazz);
    }

    public List<T> findAll() {
        return jsonDBTemplate.findAll(clazz);
    }

    public List<T> findByQuery(String query) {
        return jsonDBTemplate.find(query, clazz);
    }

    public void setDbFilesLocation(String dbFilesLocation) {
        this.dbFilesLocation = dbFilesLocation;
    }

}
