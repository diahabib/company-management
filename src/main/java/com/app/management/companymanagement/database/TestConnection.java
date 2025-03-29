package com.app.management.companymanagement.database;

import javax.swing.*;
import java.sql.Connection;

public class TestConnection {

    public  static  void main (String [] args){
        try (Connection connection = DBManager.getConnection()){
            JOptionPane.showMessageDialog(null,"Connection à la base OK.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
