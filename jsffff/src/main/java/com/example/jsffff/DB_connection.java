package com.example.jsffff;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB_connection {

    public Connection getConnection() throws Exception{
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userform","Timeadmin","ThePassword");
        }catch (Exception e){
            System.out.println(e);
        }
        return connection;
    }

}
