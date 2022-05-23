package helper;

import bean.DiscoverBean;
import service.DiscoveryService;
import util.Logger;

public class MultipleDeviceDiscover implements Runnable
{
    private DiscoverBean discoverBean;

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
                serverWebSocket.onMessage("IP : " + discoverBean.getIp() + " Discovered!", discoverBean.getSessionId());
            }
            else
            {
                serverWebSocket.onMessage("IP : " + discoverBean.getIp() + " not Discovered!", discoverBean.getSessionId());
            }
        }
        catch (Exception exception)
        {
            _logger.error("device discovery failed.", exception);
        }
    }
}
