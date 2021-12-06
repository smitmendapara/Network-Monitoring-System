package com.motadata.kernel;

import com.motadata.kernel.util.Logger;
import org.h2.tools.Server;

import java.awt.image.Kernel;
import java.sql.*;

/**
 * Created by smit on 6/12/21.
 */
public class BootManager
{
    private static final Logger _logger = new Logger();

    public static void main(String[] args)
    {
        try
        {
            startKernel();
        }
        catch (Exception exception)
        {
            _logger.error("",exception);
        }

    }


    private static void startKernel()
    {
        try
        {
            // first step start h2 database server
            starth2Database();

            // verify h2 database connection
//            verifyh2Database(server);

            // start jetty server

            // load h2 database configuration

            // start nsq

            // init nsq consumer

            // start scheduler

            // implement discovery (ping,linux ssh)

            // initilzie collector (ping,linux ssh)


        }
        catch (Exception exception)
        {
            _logger.error("",exception);
        }
    }

    public static void starth2Database()
    {
//        String path = "/home/smit/Downloads/Database/h2/";
        try
        {
            Server server = Server.createTcpServer("-webAllowOthers -tcpAllowOthers -baseDir /home/smit/Downloads/Database/h2/").start();

            System.out.println(server);

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static void verifyh2Database()
    {
        final String DB_URL = "";

        final String USER = "root";

        final String PASS = "root";

        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);

            Statement statement = connection.createStatement();

//            String sql = "CREATE TABLE REGISTRATION " +
//                    "(id INTEGER not NULL, " +
//                    " user VARCHAR(255), " +
//                    " password VARCHAR(255), " +
//                    " PRIMARY KEY ( id ))";

            String sql = "SELECT * FROM REGISTRATION";

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next())
            {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
            }

//            System.out.println("Created table in given database...");

            System.out.println("selected table in given database...");

            statement.close();

            connection.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
