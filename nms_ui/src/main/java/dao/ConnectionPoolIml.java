package dao;

import util.CommonConstantUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPoolIml implements ConnectionPool
{
    private static String url;

    private static String user;

    private static String password;

    private static LinkedBlockingQueue<Connection> connectionPool;

    private static int CONNECTION_POOL_SIZE = 3;

    static
    {
        url = CommonConstantUI.DATABASE_URL;

        user = CommonConstantUI.DATABASE_USERNAME;

        password = CommonConstantUI.DATABASE_PASSWORD;

        connectionPool = new LinkedBlockingQueue<>(CONNECTION_POOL_SIZE);
    }

    public void createPool()
    {
        for (int index = 0; index < CONNECTION_POOL_SIZE; index++)
        {
            connectionPool.add(createConnection(url, user, password));
        }
    }

    private static Connection createConnection(String url, String user, String password)
    {
        try
        {
            return DriverManager.getConnection(url, user, password);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public Connection getConnection()
    {
        Connection connection = null;

        try
        {
            if (connectionPool.size() > 0)
            {
                connection = connectionPool.take();
            }
            else
            {
                throw new RuntimeException("No Available Connections!");
            }

        }

        catch (Exception exception)
        {

        }

        return connection;
    }

    @Override
    public void releaseConnection(Connection connection)
    {
        connectionPool.add(connection);
    }
}
