package com.app.management.companymanagement.dao.impl;

import com.app.management.companymanagement.exceptions.DAOException;
import com.app.management.companymanagement.model.Task;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Stateless
public class TaskDAOImpl extends GenericDAOImpl<Task, Long>{

    public List<Task> findTasksByEmployee(Long employeeId) {

        TypedQuery<Task> query = entityManager.createQuery(
                "SELECT t FROM Task t WHERE t.employee.id = :employeeId", Task.class);
        query.setParameter("employeeId", employeeId);
        return query.getResultList();

    }

    public List<Task> findTasksByProject(Long projectId) {

        TypedQuery<Task> query = entityManager.createQuery(
                "SELECT t FROM Task t WHERE t.project.id = :projectId", Task.class);
        query.setParameter("projectId", projectId);
        return query.getResultList();

    }


    public List<Task> filterTasks(String employeeId, String projectId, String status) {
        StringBuilder queryString = new StringBuilder("SELECT t FROM Task t WHERE 1=1");
        Map<String, Object> parameters = new HashMap<>();

        if (employeeId != null && !employeeId.trim().isEmpty()) {
            try {
                Long empId = Long.parseLong(employeeId);
                queryString.append(" AND t.employee.id = :employeeId");
                parameters.put("employeeId", empId);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : ID d'employé invalide - " + employeeId);
            }
        }

        if (projectId != null && !projectId.trim().isEmpty()) {
            try {
                Long projId = Long.parseLong(projectId);
                queryString.append(" AND t.project.id = :projectId");
                parameters.put("projectId", projId);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : ID de projet invalide - " + projectId);
            }
        }

        if (status != null && !status.trim().isEmpty()) {
            queryString.append(" AND t.statut = :status");
            parameters.put("status", status);
        }

        TypedQuery<Task> query = entityManager.createQuery(queryString.toString(), Task.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }

}
