package action.discover;

import bean.NMSBean;
import dao.UserDAO;

import action.helper.ServiceProvider;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Discovery extends ActionSupport
{
    private int idAttribute;

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

    public int getIdAttribute() {
        return idAttribute;
    }

    public void setIdAttribute(int idAttribute) {
        this.idAttribute = idAttribute;
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

    ResultSet resultSet = null;

    NMSBean bean = null;

    List<NMSBean> beanList = null;

    public List<NMSBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<NMSBean> beanList) {
        this.beanList = beanList;
    }

    public String execute()
    {
        try
        {
            beanList = new ArrayList<NMSBean>();

            resultSet = UserDAO.getDiscoverTB();

            if (resultSet != null)
            {
                while (resultSet.next())
                {
                    bean = new NMSBean();

                    bean.setName(resultSet.getString(2));

                    bean.setId(resultSet.getInt(1));

                    bean.setIP(resultSet.getString(3));

                    bean.setDevice(resultSet.getString(4));

                    beanList.add(bean);
                }
            }
        }
        catch (Exception exception)
        {

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

        if (UserDAO.deleteDiscoverTableData(idAttribute))
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
            resultSet = UserDAO.getReDiscoveryData(ip, deviceType);

            while (resultSet.next())
            {
                deviceType = resultSet.getString(6);
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
