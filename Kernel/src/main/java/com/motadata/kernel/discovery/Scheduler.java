package com.motadata.kernel.discovery;

import com.motadata.kernel.dao.DataAccess;

import com.motadata.kernel.helper.ServiceProvider;

import com.motadata.kernel.util.Logger;

import java.sql.Connection;

import java.sql.ResultSet;

import java.sql.Statement;

import java.util.TimerTask;

import java.util.concurrent.ConcurrentHashMap;

public class Scheduler extends TimerTask implements Runnable
{
    private static final Logger _logger = new Logger();

    private static final DataAccess _dao = new DataAccess();

    String query = "SELECT * FROM TB_MONITOR";

    @Override
    public void run()
    {
        Statement statement = null;

        try
        {
            Connection connection = _dao.getConnection();

            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next())
            {
                int id = resultSet.getInt(1);

                String name = resultSet.getString(2);

                String ip = resultSet.getString(3);

                String discoveryUsername = resultSet.getString(4);

                String discoveryPassword = resultSet.getString(5);

                String deviceType = resultSet.getString(6);

                ServiceProvider serviceProvider = new ServiceProvider(name, ip, discoveryUsername, discoveryPassword, deviceType);

                serviceProvider.setId(id);

                ServiceProvider.checkDiscovery();

            }
        }

        catch (Exception exception)
        {
            _logger.error("something went wrong in scheduling...", exception);
        }
        finally
        {
            try
            {
                if (statement != null && !statement.isClosed())
                {
                    statement.close();
                }
            }
            catch (Exception ignored)
            {

            }
        }
    }
}
