package helper;

import bean.DiscoverBean;
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
        //TODO put whole code in the try catch. What will happen if you get error in this code ?
        try
        {
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            while (true)
            {
                DiscoverBean discoverBean = CommonUtil.takeDevice();

                //TODO what if this take device retunr null ?
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
