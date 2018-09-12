package com.redhat.syseng.soleng.rhpam.processmigration.service.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.redhat.syseng.soleng.rhpam.processmigration.model.Plan;
import com.redhat.syseng.soleng.rhpam.processmigration.service.PlanService;

@ApplicationScoped
public class PlanServiceImpl implements PlanService {

    @PersistenceContext(unitName = "migration-unit")
    EntityManager em;

    //Used by Junit test where em can't be injected
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public Plan get(Long id) {
        Query query = em.createNamedQuery("Plan.findById", Plan.class);
        query.setParameter("id", id);
        Plan result = (Plan) query.getSingleResult();
        return result;
    }

    @Override
    public List<Plan> findAll() {
        return em.createNamedQuery("Plan.findAll", Plan.class).getResultList();
    }

    @Override
    @Transactional
    public Plan delete(Long id) {
        Plan plan = em.find(Plan.class, id);
        em.remove(plan);
        return plan;
    }

    @Override
    @Transactional
    public Plan save(Plan plan) {
        em.persist(plan);
        return plan;
    }

}
