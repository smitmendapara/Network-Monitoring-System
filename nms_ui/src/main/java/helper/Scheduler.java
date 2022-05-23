package helper;

import bean.DiscoverBean;
import dao.ConnectionDAO;
import util.CommonUtil;
import util.Logger;

import java.util.List;
import java.util.TimerTask;

public class Scheduler extends TimerTask implements Runnable
{
    private DiscoverBean discoverBean;

    private static ConnectionDAO connectionDao = new ConnectionDAO();

    private static final Logger _logger = new Logger();

    @Override
    public void run()
    {
        try
        {
            List<List<String>> monitorList = connectionDao.getMonitorTable();

            if (monitorList != null)
            {
                for (List<String> subList : monitorList)
                {
                    discoverBean = new DiscoverBean();

                    discoverBean.setId(Integer.parseInt(subList.get(0)));

                    discoverBean.setName(subList.get(1));

                    discoverBean.setIp(subList.get(2));

                    discoverBean.setDiscoveryUsername(subList.get(3));

                    discoverBean.setDiscoveryPassword(subList.get(6));

                    discoverBean.setDeviceType(subList.get(4));

                    CommonUtil.putDevice(discoverBean);
                }
            }
        }

        catch (Exception exception)
        {
            _logger.error("something went wrong in scheduling...", exception);
        }
    }
}
