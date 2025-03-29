package com.app.management.companymanagement.dao.impl;

import com.app.management.companymanagement.model.Role;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

@Stateless
public class RoleDAO extends GenericDAOImpl<Role, Long> {
    public Role findByName(String name) {
        Role.RoleEnum roleEnum;

        try {
            roleEnum = Role.RoleEnum.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rôle invalide : " + name, e);
        }

        try {
            return entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", roleEnum)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
