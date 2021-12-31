package com.motadata.kernel.util;

import com.motadata.kernel.dao.DataAccess;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.sql.Connection;

import java.sql.Statement;

public class Polling implements Runnable
{
    private static final Logger _logger = new Logger();

    private static final DataAccess _dao = new DataAccess();

    @Override
    public void run()
    {
        String ip = CommonUtil.takePollingIp();

        String pingResult = "";

        String pingCmd = "ping -c 4 " + ip;

        try
        {
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(pingCmd);

            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String inputLine;

            while ((inputLine = input.readLine()) != null)
            {
                pingResult += inputLine;
            }

            input.close();

            String string = pingResult.toString();

            String subString = string.substring(string.indexOf("rtt"));

            addIpMonitor(ip, subString);

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private static void addIpMonitor(String ip, String subString)
    {
        Statement statement = null;

        Connection connection;

        try
        {
            String query = "INSERT INTO TB_MONITOR VALUES (" + "'" + ip + "'" + ", " + "'" + subString + "'" + ")";

            connection = _dao.getConnection();

            statement = connection.createStatement();

            statement.executeUpdate(query);

            statement.close();
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong in polling...", exception);
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
