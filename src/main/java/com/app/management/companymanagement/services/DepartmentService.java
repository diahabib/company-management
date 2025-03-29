package com.app.management.companymanagement.services;

import com.app.management.companymanagement.dao.impl.DepartmentDAOImpl;
import com.app.management.companymanagement.exceptions.ServiceException;
import com.app.management.companymanagement.model.Department;
import jakarta.ejb.Stateless;
import jakarta.ejb.EJB;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class DepartmentService {

    @EJB
    private DepartmentDAOImpl departmentDAO;

    public void addDepartment(Department department) {
        try {
            if (department == null || department.getNom() == null) {
                throw new ServiceException("Les informations du département sont incomplètes.");
            }
            departmentDAO.create(department);
        } catch (Exception e) {
            throw new ServiceException("Impossible d'ajouter le département.", e);
        }
    }

    public Department getDepartment(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("L'ID du département ne peut pas être nul.");
            }
            return departmentDAO.read(id);
        } catch (Exception e) {
            throw new ServiceException("Impossible de récupérer le département.", e);
        }
    }

    public List<Department> getAllDepartments() {
        try {
            return departmentDAO.list();
        } catch (Exception e) {
            throw new ServiceException("Impossible de récupérer la liste des départements.", e);
        }
    }

    public void updateDepartment(Department department) {
        try {
            if (department == null) {
                throw new ServiceException("L'ID du département est requis pour la mise à jour.");
            }
            departmentDAO.update(department);
        } catch (Exception e) {
            throw new ServiceException("Impossible de mettre à jour le département.", e);
        }
    }

    public void deleteDepartment(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("L'ID du département ne peut pas être nul.");
            }
            departmentDAO.delete(id);
        } catch (Exception e) {
            throw new ServiceException("Impossible de supprimer le département.", e);
        }
    }

    public Department findByName(String name) {
        try {
            if (name == null || name.isEmpty()) {
                throw new ServiceException("Le nom du département ne peut pas être vide.");
            }
            return departmentDAO.findByName(name);
        } catch (Exception e) {
            throw new ServiceException("Impossible de trouver le département par nom.", e);
        }
    }

    public boolean departmentExists(String nom) {
        try {
            return departmentDAO.findByName(nom) != null;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Long countDepartments() {
        try {
            return departmentDAO.count();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
