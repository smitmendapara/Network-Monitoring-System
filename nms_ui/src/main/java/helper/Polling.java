package helper;

import bean.DiscoverBean;
import service.DiscoveryService;
import util.Logger;

public class Polling implements Runnable
{
    private DiscoverBean discoverBean;

    private static final Logger _logger = new Logger();

    public Polling(DiscoverBean discoverBean) {
        this.discoverBean = discoverBean;
    }

    @Override
    public void run()
    {
        try
        {
            if (DiscoveryService.pollingDevice(discoverBean.getId(), discoverBean.getName(),discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType()))
            {
                _logger.info("IP : " + discoverBean.getIp() + " successfully polled!");
            }
            else
            {
                _logger.info("IP : " + discoverBean.getIp() + " not polled!");
            }
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on polling.", exception);
        }
    }
}
