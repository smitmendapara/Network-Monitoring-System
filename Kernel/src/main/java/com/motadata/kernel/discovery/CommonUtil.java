package com.motadata.kernel.discovery;

import com.motadata.kernel.util.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class CommonUtil
{
    private final static LinkedBlockingQueue<String> pollingQueue = new LinkedBlockingQueue<>();

    private final static LinkedBlockingQueue<String> deviceQueue = new LinkedBlockingQueue<>();

    private static final Logger _logger = new Logger();

    public static String takePollingIp()
    {
        try
        {
            return pollingQueue.take();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return null;
    }

    public static void putPollingIp(String ipName)
    {
        try
        {
            pollingQueue.add(ipName);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static String takeDeviceType()
    {
        try
        {
            return deviceQueue.take();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return null;
    }

    public static void putDeviceType(String device)
    {
        try
        {
            deviceQueue.add(device);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
