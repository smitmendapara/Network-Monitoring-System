package com.motadata.kernel.discovery;

import com.motadata.kernel.dao.DataAccess;
import com.motadata.kernel.util.Logger;
import org.h2.util.json.JSONObject;

import java.sql.Connection;

import java.sql.ResultSet;

import java.sql.Statement;

import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static com.motadata.kernel.BootManager.linuxBasedPolling;

public class Scheduler extends TimerTask implements Runnable
{
    private static final Logger _logger = new Logger();

    private static final DataAccess _dao = new DataAccess();

    String query = "SELECT * FROM TB_MONITOR";

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
                context.put(resultSet.getInt(1), "-#-" + resultSet.getString(5)); //  resultSet.getString(2) + resultSet.getString(3) + resultSet.getString(4)

                String ip = resultSet.getString(1);

                String deviceType = resultSet.getString(5);

                if(deviceType.equals("ping"))
                {
//                    pingBasedPolling(ip);
                }
//                else if(deviceType.equals("linux"))
//                {
//                    String credential = resultSet.getString(4);
//
//                    JSONObject jsonObject = new JSONObject(credential);
//
//                    jsonObject["user"];
//
//                    linuxBasedPolling();
//                }
            }

            getPolling();
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
                }

                Thread.sleep(1000);
            }
        }
        catch (Exception ignored)
        {

        }
    }
}
