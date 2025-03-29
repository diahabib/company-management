package com.app.management.companymanagement.exceptions;

import java.sql.SQLException;

public class DAOException extends SQLException {
    public DAOException(String message) {
        super(message);
    }
}
