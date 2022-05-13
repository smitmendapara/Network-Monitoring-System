package service;

import dao.ConnectionPoolIml;
import dao.DAO;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TimerTask;

public class Scheduler extends TimerTask implements Runnable
{
    private static final Logger _logger = new Logger();

    @Override
    public void run()
    {
        int id;

        Connection connection = null;

        String name, ip, discoveryUsername, discoveryPassword, deviceType;

        ConnectionPoolIml connectionPoolIml = new ConnectionPoolIml();

        ServiceProvider serviceProvider = new ServiceProvider();

        try
        {
            connection = connectionPoolIml.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TB_MONITOR");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                id = resultSet.getInt(1);

                name = resultSet.getString(2);

                ip = resultSet.getString(3);

                discoveryUsername = resultSet.getString(4);

                discoveryPassword = resultSet.getString(5);

                deviceType = resultSet.getString(6);

                serviceProvider.pollingDevice(id, name, ip, discoveryUsername, discoveryPassword, deviceType);
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
                if (connection != null && !connection.isClosed())
                {
                    connectionPoolIml.releaseConnection(connection);
                }
            }
            catch (Exception exception)
            {
                _logger.warn("connection is not closed!");
            }
        }
    }
}
