package com.motadata.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by smit on 30/12/21.
 */
public class UserDAO
{
    String URL = "jdbc:h2:tcp://localhost/configdb;" +
                                "DATABASE_TO_UPPER=false;IGNORECASE=TRUE;" +
                                "MODE=PostgreSQL;AUTO_RECONNECT=TRUE;" +
                                "AUTO_SERVER=TRUE;MV_STORE=FALSE;MVCC=FALSE";

    String USER = "motadata";

    String PASS = "motadata";

    String query = "SELECT * FROM TB_USER WHERE USERNAME = ? AND PASSWORD = ?";

    public boolean check(String username, String password)
    {
        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, username);

            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                return true;
            }
        }
        catch (Exception exception)
        {

        }
        return false;
    }
}
