package com.redhat.syseng.soleng.rhpam.processmigration.service;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Credentials;

public interface CredentialsService {

    Credentials get(Long id);

    Credentials save(Credentials credentials);

    Credentials delete(Long id);

}
