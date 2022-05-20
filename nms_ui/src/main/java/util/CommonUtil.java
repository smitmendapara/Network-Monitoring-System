package util;

import bean.DiscoverBean;

import java.util.concurrent.LinkedBlockingQueue;

public class CommonUtil
{
    private static LinkedBlockingQueue<DiscoverBean> multipleDeviceDiscoverQueue = new LinkedBlockingQueue<>();

    private static LinkedBlockingQueue<DiscoverBean> pollingQueue = new LinkedBlockingQueue<>();

    private static final Logger _logger = new Logger();

    public static DiscoverBean takeDiscoverBean()
    {
        try
        {
            return multipleDeviceDiscoverQueue.take();
        }
        catch (Exception exception)
        {
            _logger.error("id not taken from blocking queue...", exception);
        }

        return null;
    }

    //TODO where is this method being used ?
    public static boolean putDiscoverBean(DiscoverBean discoverBean)
    {
        boolean add = CommonConstant.FALSE;

        try
        {
            add = multipleDeviceDiscoverQueue.add(discoverBean);
        }
        catch (Exception exception)
        {
            _logger.error("id not put into the blocking queue...", exception);
        }

        return add;
    }

    public static DiscoverBean takeDevice()
    {
        try
        {
            return pollingQueue.take();
        }
        catch (Exception exception)
        {
            _logger.error("id not taken from blocking queue...", exception);
        }

        return null;
    }

    public static boolean putDevice(DiscoverBean discoverBean)
    {
        boolean add = CommonConstant.FALSE;

        try
        {
            add = pollingQueue.add(discoverBean);
        }
        catch (Exception exception)
        {
            _logger.error("id not put into the blocking queue...", exception);
        }

        return add;
    }
}