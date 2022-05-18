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
        //TODO put whole code in the try catch. What will happen if you get error in this code ?
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        while (true)
        {
            //TODO what if this take device retunr null ?
            DiscoverBean discoverBean = CommonUtil.takeDevice();

            executorService.execute(new Polling(discoverBean));
        }
    }
}
