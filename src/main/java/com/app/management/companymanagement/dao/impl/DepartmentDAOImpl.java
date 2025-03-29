package com.app.management.companymanagement.dao.impl;

import com.app.management.companymanagement.model.Department;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;


@Stateless
public class DepartmentDAOImpl extends GenericDAOImpl<Department, Long> {
    public Department findByName(String name) {
        TypedQuery<Department> query = entityManager.createQuery(
                "SELECT d FROM Department d WHERE d.nom = :name", Department.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
