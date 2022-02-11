package action.monitor;

import bean.MonitorBean;

import dao.UserDAO;

import action.helper.ServiceProvider;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.ResultSet;

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

    private boolean flag = true;

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

    private static final UserDAO _dao = new UserDAO();

    private static final Logger _logger = new Logger();

    ResultSet resultSet = null;

    MonitorBean bean = null;

    List<MonitorBean> beanList = null;

    public List<MonitorBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<MonitorBean> beanList) {
        this.beanList = beanList;
    }

    public String executeMonitor()
    {
        _dao.setNewId(id);

        try
        {
            beanList = new ArrayList<MonitorBean>();

            resultSet = UserDAO.getMonitorTable();

            if (resultSet != null)
            {
                while (resultSet.next())
                {
                    bean = new MonitorBean();

                    bean.setId(resultSet.getInt(1));

                    bean.setName(resultSet.getString(2));

                    bean.setIP(resultSet.getString(3));

                    bean.setDevice(resultSet.getString(6));

                    beanList.add(bean);
                }
            }
        }
        catch (Exception exception)
        {

        }

        return "success";
    }

    public String execute()
    {
        _dao.setNewId(id);

        try
        {
            beanList = new ArrayList<MonitorBean>();

            resultSet = UserDAO.getMonitorTB();

            if (resultSet != null)
            {
                while (resultSet.next())
                {
                    bean = new MonitorBean();

                    bean.setId(resultSet.getInt(1));

                    bean.setIP(resultSet.getString(3));

                    if (resultSet.getString(4) == null)
                    {
                        bean.setUsername("");
                    }
                    else
                    {
                        bean.setUsername(resultSet.getString(4));
                    }


                    bean.setDevice(resultSet.getString(6));

                    beanList.add(bean);
                }
            }
        }
        catch (Exception exception)
        {

        }

        return "success";
    }

    public String provisionMonitor()
    {
        _dao.setNewId(id);

        if (UserDAO.enterMonitorTableData(id))
        {
            return "success";
        }
        else
        {
            return "error";
        }
    }

    public String getPolling()
    {
        UserDAO _dao = new UserDAO(id, ip, deviceType);

        ResultSet resultSet = null;

        try
        {
            resultSet = UserDAO.getDashboardData();

            while (resultSet.next())
            {
                name = resultSet.getString(2);

                ip = resultSet.getString(3);

                discoveryUsername = resultSet.getString(4);

                discoveryPassword = resultSet.getString(5);

                deviceType = resultSet.getString(6);
            }

            ServiceProvider serviceProvider = new ServiceProvider(name, ip, discoveryUsername, discoveryPassword, deviceType);

            serviceProvider.setId(id);

            if (ServiceProvider.pollingDevice())
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
