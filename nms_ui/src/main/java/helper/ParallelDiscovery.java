package helper;

import bean.DiscoverBean;
import util.CommonConstant;
import util.CommonUtil;
import util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelDiscovery implements Runnable
{
    private static final Logger _logger = new Logger();

    @Override
    public void run()
    {
        try
        {
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            while (CommonConstant.TRUE)
            {
                DiscoverBean discoverBean = CommonUtil.takeDiscoverBean();

                if (discoverBean != null)
                {
                    executorService.execute(new MultipleDeviceDiscover(discoverBean));
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("Executor Service not executed...", exception);
        }
    }
}
