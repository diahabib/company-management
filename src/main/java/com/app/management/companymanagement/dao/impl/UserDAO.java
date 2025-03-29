package com.app.management.companymanagement.dao.impl;

import com.app.management.companymanagement.model.Employee;
import com.app.management.companymanagement.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

@Stateless
public class UserDAO extends GenericDAOImpl<User, Long> {

    public User findByUsername(String username) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public long usernameExists(String username) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count;
    }
}
