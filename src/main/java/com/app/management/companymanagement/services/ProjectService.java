package com.app.management.companymanagement.services;

import com.app.management.companymanagement.dao.impl.ProjectDAOImpl;
import jakarta.ejb.EJB;

import com.app.management.companymanagement.exceptions.ServiceException;
import com.app.management.companymanagement.model.Project;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
@Transactional
public class ProjectService {

    @EJB
    private ProjectDAOImpl projectDAO;

    public void addProject(Project project) {
        try {
            if (project == null || project.getNom() == null) {
                throw new ServiceException("Les informations du projet sont incomplètes.");
            }
            projectDAO.create(project);
        } catch (Exception e) {
            throw new ServiceException("Impossible d'ajouter le projet.", e);
        }
    }

    public Project getProject(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("L'ID du projet ne peut pas être nul.");
            }
            return projectDAO.read(id);
        } catch (Exception e) {
            throw new ServiceException("Impossible de récupérer le projet.", e);
        }
    }

    public List<Project> getAllProjects() {
        try {
            return projectDAO.list();
        } catch (Exception e) {
            throw new ServiceException("Impossible de récupérer la liste des projets.", e);
        }
    }

    public void updateProject(Project project) {
        try {
            if (project == null) {
                throw new ServiceException("L'ID du projet est requis pour la mise à jour.");
            }
            projectDAO.update(project);
        } catch (Exception e) {
            throw new ServiceException("Impossible de mettre à jour le projet.", e);
        }
    }

    public void deleteProject(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("L'ID du projet ne peut pas être nul.");
            }
            projectDAO.delete(id);
        } catch (Exception e) {
            throw new ServiceException("Impossible de supprimer le projet.", e);
        }
    }



    // 📈 Mettre à jour l’avancement du projet
    public void updateProjectProgress(Long projectId, String status) {
        try {
            if (projectId == null || status == null || status.isEmpty()) {
                throw new ServiceException("L'ID du projet et son avancement sont obligatoires.");
            }

            Project project = getProject(projectId);
            project.setStatus(status);
            updateProject(project);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la mise à jour de l'avancement du projet.", e);
        }
    }

    // Lister les projets en cours ou terminés
    public List<Project> getProjectsByStatus(String status) {
        try {
            if (status == null || status.isEmpty()) {
                throw new ServiceException("Le statut du projet est requis.");
            }
            return projectDAO.findByStatus(status);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors du filtrage des projets.", e);
        }
    }

    public boolean projectExists(String nom) {
        try {
            return projectDAO.findByName(nom) != null;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Long countProjects() {
        try {
            return projectDAO.count();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

}
