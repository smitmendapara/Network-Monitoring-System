package helper;

import bean.DiscoverBean;
import service.CommonServiceProvider;
import util.Logger;

public class Polling implements Runnable
{
    private final DiscoverBean discoverBean;

    private CommonServiceProvider commonServiceProvider = new CommonServiceProvider();

    private static final Logger _logger = new Logger();

    public Polling(DiscoverBean discoverBean) {
        this.discoverBean = discoverBean;
    }

    @Override
    public void run()
    {
        if (commonServiceProvider.pollingDevice(discoverBean.getId(), discoverBean.getName(),discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType()))
        {
            _logger.info("IP : " + discoverBean.getIp() + " successfully discovered!");
        }
    }
}
