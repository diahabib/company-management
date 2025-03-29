package com.app.management.companymanagement.dao.impl;

import com.app.management.companymanagement.model.Project;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class ProjectDAOImpl extends GenericDAOImpl<Project, Long>{
    public List<Project> findByStatus(String status) {
        TypedQuery<Project> query = entityManager.createQuery(
                "SELECT p FROM Project p WHERE p.status = :status", Project.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<Project> findByName(String name) {
        TypedQuery<Project> query = entityManager.createQuery(
                "SELECT p FROM Project p WHERE p.nom = :name", Project.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}
