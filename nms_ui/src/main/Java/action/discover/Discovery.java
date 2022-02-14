package action.discover;

import bean.DiscoverBean;

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

    private static final Logger _logger = new Logger();

    private final UserDAO _dao = new UserDAO();

    List<List<String>> discoverList;

    DiscoverBean bean = null;

    List<DiscoverBean> beanList = null;

    public List<DiscoverBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DiscoverBean> beanList) {
        this.beanList = beanList;
    }

    public String execute()
    {
        try
        {
            beanList = new ArrayList<DiscoverBean>();

            discoverList = UserDAO.getDiscoverTB();

            if (discoverList != null)
            {
                for (int i = 0; i < discoverList.size(); i++)
                {
                    bean = new DiscoverBean();

                    bean.setId(Integer.parseInt(discoverList.get(i).get(0)));

                    bean.setName(discoverList.get(i).get(1));

                    bean.setIP(discoverList.get(i).get(2));

                    bean.setDevice(discoverList.get(i).get(3));

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
        ServiceProvider serviceProvider = new ServiceProvider(name, ip, discoveryUsername, discoveryPassword, deviceType);

        try
        {
            if (ServiceProvider.checkDiscovery())
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
            _logger.error("discovery failed!", exception);
        }

        return null;
    }

    public String deleteData()
    {
        ServiceProvider serviceProvider = new ServiceProvider();

            serviceProvider.setIp(ip);

        if (UserDAO.deleteDiscoverTableData(id))
        {
            return "success";
        }
        else
        {
            return "error";
        }
    }

    public String reDiscovery()
    {
        ResultSet resultSet = null;

        try
        {
            discoverList = UserDAO.getReDiscoveryData(ip, deviceType);

            if (discoverList != null)
            {
                for (int i = 0; i < discoverList.size(); i++)
                {
                    deviceType = discoverList.get(i).get(5);
                }
            }

            ServiceProvider serviceProvider = new ServiceProvider(ip, deviceType);

            if (ServiceProvider.checkDiscovery())
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
