package com.app.management.companymanagement.dao;

import com.app.management.companymanagement.exceptions.DAOException;

import java.util.List;

public interface IDAO<T, ID> {
    void create(T entity) throws DAOException;
    T read(ID id) throws DAOException;
    List<T> list() throws DAOException;
    void update(T entity) throws DAOException;
    void delete(ID id) throws DAOException;
}
