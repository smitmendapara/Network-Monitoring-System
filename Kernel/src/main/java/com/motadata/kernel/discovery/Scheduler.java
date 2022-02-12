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
    private static int id;

    private static String name;

    private static String ip;

    private static String discoveryUsername;

    private static String discoveryPassword;

    private static String deviceType;

    private static String selectMonitor = "SELECT * FROM TB_MONITOR";

    private static final Logger _logger = new Logger();

    private static final DataAccess _dao = new DataAccess();

    @Override
    public void run()
    {
        ResultSet resultSet = null;

        Statement statement = null;

        Connection connection = null;

        try
        {
            connection = _dao.getConnection();

            statement = connection.createStatement();

            resultSet = statement.executeQuery(selectMonitor);

            while (resultSet.next())
            {
                id = resultSet.getInt(1);

                name = resultSet.getString(2);

                ip = resultSet.getString(3);

                discoveryUsername = resultSet.getString(4);

                discoveryPassword = resultSet.getString(5);

                deviceType = resultSet.getString(6);

                ServiceProvider serviceProvider = new ServiceProvider(name, ip, discoveryUsername, discoveryPassword, deviceType);

                serviceProvider.setId(id);

                ServiceProvider.pollingDevice();
            }
        }

        catch (Exception exception)
        {
            _logger.error("something went wrong in scheduling...", exception);
        }
    }
}
