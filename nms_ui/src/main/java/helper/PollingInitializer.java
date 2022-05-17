package helper;

import bean.DiscoverBean;
import util.CommonUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PollingInitializer implements Runnable
{
    @Override
    public void run()
    {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        while (true)
        {
            DiscoverBean discoverBean = CommonUtil.takeDevice();

            executorService.execute(new Polling(discoverBean));
        }
    }
}
