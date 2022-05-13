package service;

import monitorBean.DiscoverBean;
import helper.MultipleDeviceDiscover;
import helper.QueueHandler;
import util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelProvision implements Runnable
{
    private static final Logger _logger = new Logger();

    @Override
    public void run()
    {
        try
        {
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            while (true)
            {
                DiscoverBean discoverBean = QueueHandler.takeDiscoverBean();

                executorService.execute(new MultipleDeviceDiscover(discoverBean));
            }
        }
        catch (Exception exception)
        {
            _logger.error("Executor Service not executed...", exception);
        }
    }
}
