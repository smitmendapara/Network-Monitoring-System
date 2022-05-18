package dao;

import util.CommonConstant;
import util.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPoolIml implements ConnectionPool
{
    private static String databaseURL;

    private static String databaseUser;

    private static String databasePassword;

    private static LinkedBlockingQueue<Connection> connectionPool;

    private static int CONNECTION_POOL_SIZE = 10;

    private static final Logger _logger = new Logger();

    static
    {
        databaseURL = CommonConstant.DATABASE_URL;

        databaseUser = CommonConstant.DATABASE_USERNAME;

        databasePassword = CommonConstant.DATABASE_PASSWORD;

        connectionPool = new LinkedBlockingQueue<>(CONNECTION_POOL_SIZE);
    }

    public boolean createPool()
    {
        try
        {
            for (int index = 0; index < CONNECTION_POOL_SIZE; index++)
            {
                //TODO what if createConnection return null
                if (createConnection(databaseURL, databaseUser, databasePassword) != null)
                {
                    connectionPool.add(createConnection(databaseURL, databaseUser, databasePassword));
                }
                else
                {
                    return false;
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("connection was not created.", exception);
        }

        return true;
    }

    private static Connection createConnection(String url, String user, String password)
    {
        Connection connection = null;

        try
        {
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on create connection time.", exception);
        }

        return connection;
    }

    @Override
    public Connection getConnection()
    {
        Connection connection = null;

        try
        {
            connection = connectionPool.take();
        }

        catch (Exception exception)
        {
            _logger.error("something went wrong on get connection.", exception);
        }

        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection)
    {
        //TODO - you can put check for the connection here instead of where the method is being called
        boolean add = false;

        try
        {
            add = connectionPool.add(connection);
        }
        catch (Exception exception)
        {
            _logger.error("connection not re-added successfully.", exception);
        }

        return add;
    }
}
