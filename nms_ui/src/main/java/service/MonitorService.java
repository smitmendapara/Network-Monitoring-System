package service;

import bean.MonitorBean;
import dao.ConnectionDAO;
import util.CommonConstant;
import util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonitorService
{
    private static final Logger _logger = new Logger();

    public static void loadMonitorDeviceData(MonitorBean monitorBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            List<List<String>> monitorList = connectionDao.getMonitorForm(monitorBean.getId());

            if (monitorList != null)
            {
                for (List<String> subList : monitorList)
                {
                    monitorBean.setId(Integer.parseInt(subList.get(0)));

                    monitorBean.setIp(subList.get(2));

                    monitorBean.setDiscoveryUsername(subList.get(3));

                    monitorBean.setDeviceType(subList.get(4));
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("monitor device not load.", exception);
        }
    }

    public static void provisionMonitorDevice(MonitorBean monitorBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            List<HashMap<String, Object>> monitorDetailsList = connectionDao.getMonitorDetails(monitorBean.getId());

            for (HashMap<String, Object> monitorDetails : monitorDetailsList)
            {
                monitorBean.setIp(monitorDetails.get("IP").toString());

                monitorBean.setDiscoveryUsername(monitorDetails.get("Username").toString());

                monitorBean.setDiscoveryPassword(monitorDetails.get("Password").toString());

                monitorBean.setDeviceType(monitorDetails.get("Device").toString());
            }

            if (connectionDao.checkIpMonitor(monitorBean.getIp(), monitorBean.getDeviceType()))
            {
                monitorBean.setFlag(provisionMonitor(monitorBean.getId(), monitorBean.getDeviceType()));
            }
            else
            {
                monitorBean.setFlag(Boolean.FALSE);
            }
        }
        catch (Exception exception)
        {
            _logger.error("provision failed!", exception);
        }
    }

    public static void getMonitorDeviceData(MonitorBean monitorBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        List<MonitorBean> beanList = new ArrayList<>();

        try
        {
            List<List<String>> monitorList = connectionDao.getMonitorTable();

            if (monitorList != null)
            {
                for (List<String> subList : monitorList)
                {
                    MonitorBean newBean = new MonitorBean();

                    newBean.setId(Integer.parseInt(subList.get(0)));

                    newBean.setName(subList.get(1));

                    newBean.setIp(subList.get(2));

                    newBean.setDeviceType(subList.get(4));

                    newBean.setStatus(subList.get(5));

                    beanList.add(newBean);
                }
            }

            monitorBean.setBeanList(beanList);
        }
        catch (Exception exception)
        {
            _logger.error("monitor device data not fetched.", exception);
        }
    }

    public static void deleteMonitorDevice(MonitorBean monitorBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            monitorBean.setFlag(connectionDao.deleteMonitorData(monitorBean.getId()));
        }
        catch (Exception exception)
        {
            _logger.error("monitor device not deleted.", exception);
        }
    }

    private static boolean provisionMonitor(int id, String deviceType)
    {
        boolean status = true;

        String response = "", ipStatus = "Unknown";

        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            if (deviceType.equals(CommonConstant.STRING_ZERO) || deviceType.equals(CommonConstant.PING_DEVICE))
            {
                try
                {
                    if (connectionDao.enterMonitorTableData(id, response, ipStatus))
                    {
                        _logger.debug("successfully data inserted into tb_monitor table!");
                    }
                    else
                    {
                        _logger.warn("still not inserted into tb_discover and tb_result table!");
                    }
                }
                catch (Exception exception)
                {
                    _logger.error("something went wrong on ping discovery verify side!", exception);

                    status = false;
                }

                return status;

            }

            if (deviceType.equals(CommonConstant.STRING_ONE) || deviceType.equals(CommonConstant.LINUX_DEVICE))
            {
                try
                {
                    if (connectionDao.enterMonitorTableData(id, response, ipStatus))
                    {
                        _logger.debug("successfully data inserted into tb_monitor table!");
                    }
                    else
                    {
                        _logger.warn("still not inserted into tb_discover and tb_result table!");
                    }
                }
                catch (Exception exception)
                {
                    _logger.error("something went wrong on linux discovery verify side!", exception);

                    status = false;
                }

                return status;
            }
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on discovery side!", exception);

            status = false;
        }

        return status;
    }
}
