package helper;

import bean.DiscoverBean;
import service.ServiceProvider;
import util.CommonUtil;

import java.util.Date;

public class Polling implements Runnable
{
    ServiceProvider serviceProvider = new ServiceProvider();

    @Override
    public void run()
    {
        while (true)
        {
            DiscoverBean discoverBean = CommonUtil.takeDevice();

            serviceProvider.pollingDevice(discoverBean.getId(), discoverBean.getName(),discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType());

            System.out.println(new Date());
        }
    }
}
