package com.app.management.companymanagement.services;

import com.app.management.companymanagement.dao.impl.TaskDAOImpl;
import com.app.management.companymanagement.exceptions.ServiceException;
import com.app.management.companymanagement.model.Employee;
import com.app.management.companymanagement.model.Task;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
@Transactional
public class TaskService {

    @EJB
    private TaskDAOImpl taskDAO;

    public void addTask(Task task) {
        try {
            if (task == null) {
                throw new ServiceException("Les informations de la tâche sont incomplètes.");
            }
            taskDAO.create(task);
        } catch (Exception e) {
            throw new ServiceException("Impossible d'ajouter la tâche.", e);
        }
    }

    public Task getTask(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("L'ID de la tâche ne peut pas être nul.");
            }
            return taskDAO.read(id);
        } catch (Exception e) {
            throw new ServiceException("Impossible de récupérer la tâche.", e);
        }
    }

    public List<Task> getAllTasks() {
        try {
            return taskDAO.list();
        } catch (Exception e) {
            throw new ServiceException("Impossible de récupérer la liste des tâches.", e);
        }
    }

    public void updateTask(Task task) {
        try {
            if (task == null) {
                throw new ServiceException("L'ID de la tache est requis pour la mise à jour.");
            }
            taskDAO.update(task);
        } catch (Exception e) {
            throw new ServiceException("Impossible de mettre à jour la tache.", e);
        }
    }

    public void deleteTask(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("L'ID de la tache ne peut pas être nul.");
            }
            taskDAO.delete(id);
        } catch (Exception e) {
            throw new ServiceException("Impossible de supprimer la tache.", e);
        }
    }

    public List<Task> getTasksByEmployee(Long employeeId) {
        try {
            return taskDAO.findTasksByEmployee(employeeId);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des tâches de l'employé.", e);
        }
    }

    public List<Task> getTasksByProject(Long projectId) {
        try {
            return taskDAO.findTasksByProject(projectId);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des tâches du projet.", e);
        }
    }

    public List<Task> filterTasks(String employeeId, String projectId, String status) {
        try {
            return taskDAO.filterTasks(employeeId, projectId, status);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la recherche de la tache.", e);
        }
    }


}
