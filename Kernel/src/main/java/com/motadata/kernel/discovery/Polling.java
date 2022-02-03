package com.motadata.kernel.discovery;

import com.motadata.kernel.dao.DataAccess;

import com.motadata.kernel.util.Logger;

import java.io.BufferedReader;

import java.io.InputStreamReader;

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

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
