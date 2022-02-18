package action.monitor;

import bean.MonitorBean;

import dao.UserDAO;

import action.helper.ServiceProvider;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;

import java.util.List;

public class Monitor extends ActionSupport
{
    private int id;

    private String ip;

    private String name;

    private String discoveryUsername;

    private String discoveryPassword;

    private String deviceType;

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscoveryUsername() {
        return discoveryUsername;
    }

    public void setDiscoveryUsername(String discoveryUsername) {
        this.discoveryUsername = discoveryUsername;
    }

    public String getDiscoveryPassword() {
        return discoveryPassword;
    }

    public void setDiscoveryPassword(String discoveryPassword) {
        this.discoveryPassword = discoveryPassword;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    private List<List<String>> monitorList;

    private MonitorBean bean = null;

    private List<MonitorBean> beanList = null;

    public List<MonitorBean> getBeanList()
    {
        return beanList;
    }

    public void setBeanList(List<MonitorBean> beanList)
    {
        this.beanList = beanList;
    }

    private ServiceProvider serviceProvider = new ServiceProvider();

    private static final UserDAO _dao = new UserDAO();

    private static final Logger _logger = new Logger();

    public String getMonitorData()
    {
        _dao.setNewId(id);

        try
        {
            beanList = new ArrayList<>();

            monitorList = _dao.getMonitorTable();

            if (monitorList != null)
            {
                for (List<String> subList : monitorList)
                {
                    bean = new MonitorBean();

                    bean.setId(Integer.parseInt(subList.get(0)));

                    bean.setName(subList.get(1));

                    bean.setIP(subList.get(2));

                    bean.setDevice(subList.get(5));

                    beanList.add(bean);
                }
            }
        }
        catch (Exception exception)
        {
            _logger.warn("monitor data not set into the bean!");
        }

        return "success";
    }

    public String getMonitorForm()
    {
        _dao.setNewId(id);

        try
        {
            beanList = new ArrayList<>();

            monitorList = _dao.getMonitorTB();

            if (monitorList != null)
            {
                for (List<String> subList : monitorList)
                {
                    bean = new MonitorBean();

                    bean.setId(Integer.parseInt(subList.get(0)));

                    bean.setIP(subList.get(2));

                    if (subList.get(3) == null)
                    {
                        bean.setUsername("");
                    }
                    else
                    {
                        bean.setUsername(subList.get(3));
                    }

                    bean.setDevice(subList.get(5));

                    beanList.add(bean);
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
        _dao.setNewId(id);

        beanList = new ArrayList<>();

        bean = new MonitorBean();

        if (_dao.checkIpMonitor(id))
        {
            if (_dao.enterMonitorTableData(id))
            {
                bean.setFlag(true);

                beanList.add(bean);

                return "success";
            }

            return "success";
        }
        else
        {
            bean.setFlag(false);

            beanList.add(bean);

            return "error";
        }
    }

    public String getPolling()
    {
        _dao.setNewId(id);

        _dao.setIp(ip);

        _dao.setDeviceType(deviceType);

        try
        {
            monitorList = _dao.getDashboardData();

            if (!monitorList.isEmpty())
            {
                for (List<String> subList : monitorList)
                {
                    name = subList.get(1);

                    ip = subList.get(2);

                    discoveryUsername = subList.get(3);

                    discoveryPassword = subList.get(4);

                    deviceType = subList.get(5);
                }
            }

            serviceProvider.setId(id);

            serviceProvider.setName(name);

            serviceProvider.setIp(ip);

            serviceProvider.setDiscoveryUsername(discoveryUsername);

            serviceProvider.setDiscoveryPassword(discoveryPassword);

            serviceProvider.setDeviceType(deviceType);

            if (serviceProvider.pollingDevice())
            {
                return "success";
            }
            else
            {
                return "error";
            }
        }
        catch (Exception exception)
        {
            _logger.warn("dashboard page not found!");
        }

        return null;
    }
}
