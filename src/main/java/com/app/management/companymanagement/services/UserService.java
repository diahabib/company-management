package com.app.management.companymanagement.services;

import com.app.management.companymanagement.dao.impl.RoleDAO;
import com.app.management.companymanagement.dao.impl.UserDAO;
import com.app.management.companymanagement.exceptions.ServiceException;
import com.app.management.companymanagement.model.Employee;
import com.app.management.companymanagement.model.Role;
import com.app.management.companymanagement.model.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class UserService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private RoleDAO roleDAO;



    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    public User authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);

        if (user == null) {
            return null;
        }

        String hashedPassword = hashPassword(password);


        //String hashedInputPassword = hashPassword(password);
        //if (user.getPassword().equals(hashedInputPassword)) {
          //  return user; // Authentication successful
        //}

        if (user.getPassword().equals(hashedPassword) || user.getPassword().equals(password)) {
          return user;
        }

        return null;
    }

    public void registerUser(String username, String password, Role.RoleEnum... roles) {
        if (userDAO.findByUsername(username) != null) {
            throw new RuntimeException("Ce nom d'utilisateur existe déjà.");
        }

        String hashedPassword = hashPassword(password);
        Set<Role> userRoles = new HashSet<>();

        for (Role.RoleEnum roleEnum : roles) {
            Role role = roleDAO.findByName(roleEnum.name());
            if (role == null) {
                throw new RuntimeException("Le rôle " + roleEnum.name() + " n'existe pas en base.");
            }
            userRoles.add(role);
        }

        User user = new User(username, hashedPassword);
        user.setRoles(userRoles);
        userDAO.create(user);
    }

    public void deleteUser(Long id) {
        try {
            if (id == null) {
                throw new ServiceException("L'ID de l'utilisateur ne peut pas être nul.");
            }
            userDAO.delete(id);
        } catch (Exception e) {
            throw new ServiceException("Impossible de supprimer l'utilisateur.", e);
        }
    }


    public boolean hasRole(User user, Role.RoleEnum roleToCheck) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName() == roleToCheck);
    }

    public User createUserForEmployee(Employee employee, String defaultPassword, Role.RoleEnum roleEnum) {

        String username = generateUsername(employee) ;

        if (userDAO.findByUsername(username) != null) {
            throw new RuntimeException("Un utilisateur avec ce nom existe déjà");
        }

        Role role = roleDAO.findByName(roleEnum.name());
        if (role == null) {
            throw new RuntimeException("Le rôle " + roleEnum.name() + " n'existe pas");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashPassword(defaultPassword));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userDAO.create(user);
        return user;
    }

    private String generateUsername(Employee employee) {
        if (employee.getPrenom() == null || employee.getNom() == null) {
            throw new RuntimeException("Le prénom et le nom ne peuvent pas être null.");
        }

        String base = employee.getPrenom().charAt(0) +
                employee.getNom().replaceAll("\\s+", "").toLowerCase();
        String username = base;
        int counter = 1;

        while (userDAO.findByUsername(username) != null) {
            username = base + counter;
            counter++;
        }

        return username;
    }



}
