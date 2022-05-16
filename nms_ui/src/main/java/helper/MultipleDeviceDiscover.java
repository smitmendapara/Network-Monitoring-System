package helper;

import bean.DiscoverBean;
import service.ServerWebSocketEndPoint;
import service.ServiceProvider;

public class MultipleDeviceDiscover implements Runnable
{
    DiscoverBean discoverBean;

    ServiceProvider serviceProvider = new ServiceProvider();

    ServerWebSocketEndPoint serverWebSocketEndPoint = new ServerWebSocketEndPoint();

    public MultipleDeviceDiscover(DiscoverBean discoverBean)
    {
        this.discoverBean = discoverBean;
    }

    @Override
    public void run()
    {
        try
        {
            if (serviceProvider.executeDeviceDiscovery(discoverBean.getId(), discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType()))
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

        }
    }
}
