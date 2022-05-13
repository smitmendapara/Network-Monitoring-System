package action.monitor;

import monitorBean.DiscoverBean;
import monitorBean.MonitorBean;

import com.opensymphony.xwork2.ModelDriven;
import dao.DAO;

import service.ServiceProvider;

import util.Logger;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

public class Monitor implements ModelDriven<MonitorBean>
{
    private MonitorBean monitorBean = new MonitorBean();

    private static final Logger _logger = new Logger();

    public String getMonitorForm()
    {
        DAO dao = new DAO();

        try
        {
            List<List<String>> monitorList = dao.getMonitorForm(monitorBean.getId());

            if (monitorList != null)
            {
                for (List<String> subList : monitorList)
                {
                    monitorBean = new MonitorBean();

                    monitorBean.setId(Integer.parseInt(subList.get(0)));

                    monitorBean.setIp(subList.get(2));

                    monitorBean.setDiscoveryUsername(subList.get(3));

                    monitorBean.setDeviceType(subList.get(4));
                }
            }
        }
        catch (Exception exception)
        {
            _logger.warn("monitor data not set into the bean!");
        }

        return "success";
    }

    public String provisionMonitor()
    {
        DAO dao = new DAO();

        ServiceProvider serviceProvider = new ServiceProvider();

        try
        {
            List<HashMap<String, Object>> monitorDetailsList = dao.getMonitorDetails(monitorBean.getId());

            for (HashMap<String, Object> monitorDetails : monitorDetailsList)
            {
                monitorBean.setIp(monitorDetails.get("IP").toString());

                monitorBean.setDiscoveryUsername(monitorDetails.get("Username").toString());

                monitorBean.setDiscoveryUsername(monitorDetails.get("Password").toString());

                monitorBean.setDiscoveryUsername(monitorDetails.get("Device").toString());
            }

            if (dao.checkIpMonitor(monitorBean.getIp(), monitorBean.getDeviceType()))
            {
                monitorBean.setFlag(serviceProvider.provisionMonitor(monitorBean.getId(), monitorBean.getDeviceType()));
            }
            else
            {
                monitorBean.setFlag(Boolean.FALSE);
            }
        }
        catch (Exception exception)
        {
            _logger.error("device not monitored...", exception);
        }

        return "success";
    }

    public String getMonitorData()
    {
        DAO dao = new DAO();

        List<MonitorBean> beanList = new ArrayList<>();

        try
        {
            List<List<String>> monitorList = dao.getMonitorTable();

            if (monitorList != null)
            {
                for (List<String> subList : monitorList)
                {
                    MonitorBean monitorBean = new MonitorBean();

                    monitorBean.setId(Integer.parseInt(subList.get(0)));

                    monitorBean.setName(subList.get(1));

                    monitorBean.setIp(subList.get(2));

                    monitorBean.setDeviceType(subList.get(4));

                    monitorBean.setStatus(subList.get(5));

                    beanList.add(monitorBean);
                }
            }

            monitorBean.setBeanList(beanList);
        }
        catch (Exception exception)
        {
            _logger.warn("monitor data not set into the bean!");
        }

        return "success";
    }

    public String deleteMonitorData()
    {
        DAO dao = new DAO();

        try
        {
            monitorBean = new MonitorBean();

            monitorBean.setFlag(dao.deleteMonitorData(monitorBean.getId()));
        }
        catch (Exception exception)
        {
            _logger.error("monitor row not deleted...", exception);
        }

        return "success";
    }

    @Override
    public MonitorBean getModel() {
        return monitorBean;
    }
}
