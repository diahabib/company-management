package com.app.management.companymanagement.services;

import com.app.management.companymanagement.dao.impl.EmployeeDAO;
import com.app.management.companymanagement.exceptions.ServiceException;
import com.app.management.companymanagement.model.Department;
import com.app.management.companymanagement.model.Employee;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
@Transactional
public class EmployeeService {
    @EJB
    private EmployeeDAO employeeDAO;

    public void addEmployee(Employee employee) {
        try {
            if (employee == null) {
                throw new ServiceException("Les informations de l'employé sont incomplètes.");
            }
            employeeDAO.create(employee);
        } catch (Exception e) {
            throw new ServiceException("L'ajout de l'employé a échoué.");
        }
    }

    public Employee getEmployee(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("L'ID de l'employé ne peut pas être nul.");
            }
            return employeeDAO.read(id);
        } catch (Exception e) {
            throw new ServiceException("Impossible de récupérer l'employé.", e);
        }
    }

    public List<Employee> getAllEmployees() {
        try {
            return employeeDAO.list();
        } catch (Exception e) {
            throw new ServiceException("Impossible de récupérer la liste des employés.", e);
        }
    }

    public void updateEmployee(Employee employee) {
        try {
            if (employee == null) {
                throw new ServiceException("L'ID de l'employé est requis pour la mise à jour.");
            }
            employeeDAO.update(employee);
        } catch (Exception e) {
            throw new ServiceException("Impossible de mettre à jour l'employé.", e);
        }
    }

    public void deleteEmployee(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("L'ID de l'employé ne peut pas être nul.");
            }
            employeeDAO.delete(id);
        } catch (Exception e) {
            throw new ServiceException("Impossible de supprimer l'employé.", e);
        }
    }

    public List<Employee> searchEmployees(String searchTerm, String departmentId, String role) {
        try {
            return employeeDAO.searchEmployees(searchTerm, departmentId, role);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la recherche d'employés.", e);
        }
    }

    public void assignEmployeeToDepartment(Long employeeId, Department department) {
        try {
            Employee employee = getEmployee(employeeId);
            if (employee == null) {
                throw new ServiceException("L'employé n'existe pas.");
            }
            employee.setDepartment(department);
            updateEmployee(employee);
        } catch (Exception e) {
            throw new ServiceException("Impossible d'assigner l'employé au département.", e);
        }
    }

    public boolean emailExists(String email) {
        try {
            return employeeDAO.emailExists(email) > 0;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Long countEmployees() {
        try {
            return employeeDAO.count();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}

