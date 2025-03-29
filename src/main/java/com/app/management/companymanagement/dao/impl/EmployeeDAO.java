package com.app.management.companymanagement.dao.impl;

import com.app.management.companymanagement.model.Employee;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class EmployeeDAO extends GenericDAOImpl<Employee, Long>  {

    public List<Employee> findByDepartment(Long departmentId) {
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.department.id = :departmentId", Employee.class);
        query.setParameter("departmentId", departmentId);
        return query.getResultList();
    }

    public List<Employee> findByRole(String role) {
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.role = :role", Employee.class);
        query.setParameter("role", role);
        return query.getResultList();
    }

    public List<Employee> searchByName(String name) {
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.nom LIKE :name OR e.prenom LIKE :name", Employee.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    public List<Employee> searchEmployees(String searchTerm, String departmentId, String role) {
        StringBuilder queryString = new StringBuilder("SELECT e FROM Employee e WHERE 1=1");
        Map<String, Object> parameters = new HashMap<>();

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            queryString.append(" AND (LOWER(e.nom) LIKE :searchTerm OR LOWER(e.prenom) LIKE :searchTerm)");
            parameters.put("searchTerm", "%" + searchTerm.trim().toLowerCase() + "%");
        }

        if (departmentId != null && !departmentId.trim().isEmpty()) {
            try {
                Long deptId = Long.parseLong(departmentId);
                queryString.append(" AND e.department.id = :departmentId");
                parameters.put("departmentId", deptId);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Erreur : ID de département invalide - " + departmentId);
            }
        }

        if (role != null && !role.trim().isEmpty()) {
            queryString.append(" AND LOWER(e.role) = :role");
            parameters.put("role", role.trim().toLowerCase());
        }

        TypedQuery<Employee> query = entityManager.createQuery(queryString.toString(), Employee.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }

    public Employee findByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT e FROM Employee e WHERE e.email = :email", Employee.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public long emailExists(String email) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM Employee u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count;
    }

}
