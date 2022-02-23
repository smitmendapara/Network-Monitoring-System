package action.discover;

import bean.DashboardBean;
import bean.DiscoverBean;

import bean.MonitorBean;
import dao.UserDAO;

import action.helper.ServiceProvider;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.ResultSet;

import java.util.ArrayList;

import java.util.List;

public class Discovery extends ActionSupport
{
    private int id;

    private String name;

    private String ip;

    private String discoveryUsername;

    private String discoveryPassword;

    private String deviceType;

    private Object result;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    private DiscoverBean bean = null;

    private List<DiscoverBean> beanList = null;

    public List<DiscoverBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DiscoverBean> beanList) {
        this.beanList = beanList;
    }

    private ServiceProvider serviceProvider = new ServiceProvider();

    private static final UserDAO _dao = new UserDAO();

    private static final Logger _logger = new Logger();

    public String getDiscoverData()
    {
        List<List<String>> discoverList;

        try
        {
            beanList = new ArrayList<>();

            discoverList = _dao.getDiscoverData();

            if (discoverList != null)
            {
                for (List<String> subList : discoverList)
                {
                    bean = new DiscoverBean();

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
            _logger.warn("getting data error from DiscoverBean!");
        }

        return "success";
    }

    public String executeDiscovery()
    {
        serviceProvider.setName(name);

        serviceProvider.setIp(ip);

        serviceProvider.setDiscoveryUsername(discoveryUsername);

        serviceProvider.setDiscoveryPassword(discoveryPassword);

        serviceProvider.setDeviceType(deviceType);

        beanList = new ArrayList<>();

        bean = new DiscoverBean();

        try
        {
            if (serviceProvider.addDevice())
            {
                bean.setFlag(true);

                beanList.add(bean);

                return "success";
            }
            else
            {
                bean.setFlag(false);

                beanList.add(bean);

                return  "error";
            }

        }
        catch (Exception exception)
        {
            _logger.error("discovery failed!", exception);
        }

        return null;
    }

    public String deleteDiscoverData()
    {
        serviceProvider.setIp(ip);

        if (_dao.deleteDiscoverTableData(id))
        {
            return "success";
        }
        else
        {
            return "error";
        }
    }

    public String executeReDiscovery()
    {
        try
        {
            serviceProvider.setId(id);

            serviceProvider.setIp(ip);

            serviceProvider.setDeviceType(deviceType);

            if (serviceProvider.checkDiscovery())
            {
                return "success";
            }
            else
            {
                return  "error";
            }

        }
        catch (Exception exception)
        {
            _logger.error("re-discovery failed!", exception);
        }

        return null;
    }
}
