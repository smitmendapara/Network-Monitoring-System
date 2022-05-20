package helper;

import bean.DiscoverBean;
import service.DiscoveryService;
import util.Logger;

public class MultipleDeviceDiscover implements Runnable
{
    private DiscoverBean discoverBean;

    //TODO this will create new object at every 2 second

    //TODO this will create new object at every 2 second
    private static ServerWebSocket serverWebSocket = new ServerWebSocket();

    private static final Logger _logger = new Logger();

    public MultipleDeviceDiscover(DiscoverBean discoverBean)
    {
        this.discoverBean = discoverBean;
    }

    @Override
    public void run()
    {
        try
        {
            if (DiscoveryService.executeDeviceDiscovery(discoverBean.getId(), discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType()))
            {
                serverWebSocket.onMessage("IP : " + discoverBean.getIp() + " Discovered!");
            }
            else
            {
                serverWebSocket.onMessage("IP : " + discoverBean.getIp() + " not Discovered!");
            }
        }
        catch (Exception exception)
        {
            _logger.error("device discovery failed.", exception);
        }
    }
}
