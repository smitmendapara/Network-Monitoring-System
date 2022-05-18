package helper;

import bean.DiscoverBean;
import service.DiscoveryService;
import util.Logger;

public class Polling implements Runnable
{
    private DiscoverBean discoverBean;

    private DiscoveryService discoveryService = new DiscoveryService();

    private static final Logger _logger = new Logger();

    public Polling(DiscoverBean discoverBean) {
        this.discoverBean = discoverBean;
    }

    @Override
    public void run()
    {
        //TODO where is try catch ?
        try
        {
            if (discoveryService.pollingDevice(discoverBean.getId(), discoverBean.getName(),discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType()))
            {
                _logger.info("IP : " + discoverBean.getIp() + " successfully discovered!");
            }
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on polling.", exception);
        }
    }
}
