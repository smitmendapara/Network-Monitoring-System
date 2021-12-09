package com.motadata.kernel.dao;

import com.motadata.kernel.util.CommonConstant;

import java.sql.*;
import java.util.Scanner;

public class DataAccess
{
    private static final GetQuery _query = new GetQuery();

    private static String DB_URL = CommonConstant.DATABASE_URL;

    private static String USER = "motadata";

    private static String PASS = "motadata";

    private static Connection connection = null;

    private static Statement statement = null;

    private static ResultSet resultSet = null;

    public Connection getConnection()
    {
        try
        {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return connection;
    }

    public boolean configureDB(Connection connection) throws SQLException
    {
        boolean status = true;

        try
        {
            statement = connection.createStatement();

            System.out.println("Enter select, update, and delete query number respectively between 1 to 3 : ");

            Scanner scanner = new Scanner(System.in);

            int value = scanner.nextInt();

            String sql = _query.getQuery(value);

            System.out.println();

            System.out.println("Executable Query : " + sql);

            if (!sql.equals("stop") && value == 1)
            {
                resultSet = statement.executeQuery(sql); // make dynamic query

                System.out.println();

                System.out.println("database configured with the data value!");

                System.out.println();

                while (resultSet.next())
                {
                    System.out.println(resultSet.getInt(1) + " : " + resultSet.getString(2) + " : " + resultSet.getString(3));
                }

                System.out.println();
            }

            else if (!sql.equals("stop") && value == 2)
            {
                statement.executeUpdate(sql);

                System.out.println("database updated with the new value!");

                System.out.println();
            }
            else if (!sql.equals("stop") && value == 3)
            {
                statement.executeUpdate(sql);

                System.out.println("database updated with the new value!");

                System.out.println();
            }
            else
            {
                System.out.println("sorry, query doesn't execute!");
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            status = false;
        }
        finally
        {
            statement.close();
        }

        return status;
    }
}
