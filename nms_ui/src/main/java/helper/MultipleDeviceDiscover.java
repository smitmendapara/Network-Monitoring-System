package helper;

import bean.DiscoverBean;
import service.ServerWebSocketEndPoint;
import service.CommonServiceProvider;
import util.Logger;

public class MultipleDeviceDiscover implements Runnable
{
    private DiscoverBean discoverBean;

    private CommonServiceProvider commonServiceProvider = new CommonServiceProvider();

    private ServerWebSocketEndPoint serverWebSocketEndPoint = new ServerWebSocketEndPoint();

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
            if (commonServiceProvider.executeDeviceDiscovery(discoverBean.getId(), discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType()))
            {
                serverWebSocketEndPoint.onMessage("IP : " + discoverBean.getIp() + " Discovered!");
            }
            else
            {
                serverWebSocketEndPoint.onMessage("IP : " + discoverBean.getIp() + " not Discovered!");
            }
        }
        catch (Exception exception)
        {
            _logger.error("device discovery failed.", exception);
        }
    }
}
