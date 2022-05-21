package helper;

import bean.DiscoverBean;
import util.CommonConstant;
import util.CommonUtil;
import util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PollingInitializer implements Runnable
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
                DiscoverBean discoverBean = CommonUtil.takeDevice();

                if (discoverBean != null)
                {
                    executorService.execute(new Polling(discoverBean));
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("polling executor service not successfully started.", exception);
        }
    }
}
