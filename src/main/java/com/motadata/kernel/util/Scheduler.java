package com.motadata.kernel.util;

import com.motadata.kernel.dao.DataAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by smit on 29/12/21.
 */
public class Scheduler implements Runnable
{
    private static final Logger _logger = new Logger();

    private static final DataAccess _dao = new DataAccess();

    String query = "select * from TB_DISCOVER";

    ConcurrentHashMap<Integer, String> context = new ConcurrentHashMap<>();

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
//                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2));

                context.put(resultSet.getInt(1), resultSet.getString(2));

                getPolling();
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

    private void getPolling()
    {
        try
        {
            while (true)
            {
                for (Integer ipAddress : context.keySet())
                {
                    String ip = context.get(ipAddress);

                    CommonUtil.putPollingIp(ip);

//                    partialContext.put(ipAddress,context.get(ipAddress));
                }

                Thread.sleep(10);
            }
        }
        catch (Exception ignored)
        {

        }
    }
}
