package helper;

import monitorBean.DiscoverBean;
import util.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class QueueHandler
{
    static LinkedBlockingQueue<DiscoverBean> linkedBlockingQueue = new LinkedBlockingQueue<>();

    private static final Logger _logger = new Logger();

    public static DiscoverBean takeDiscoverBean()
    {
        try
        {
            return linkedBlockingQueue.take();
        }
        catch (Exception exception)
        {
            _logger.error("id not taken from blocking queue...", exception);
        }

        return null;
    }

    public static boolean putDiscoverBean(DiscoverBean discoverBean)
    {
        boolean add = false;

        try
        {
           add = linkedBlockingQueue.add(discoverBean);
        }
        catch (Exception exception)
        {
            _logger.error("id not put into the blocking queue...", exception);
        }

        return add;
    }
}
