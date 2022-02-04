package action.monitor;

import action.dao.UserDAO;

import action.helper.ServiceProvider;

import action.util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.ResultSet;

public class Monitor extends ActionSupport
{
    private int id;

    private String ip;

    private String name;

    private String discoveryUsername;

    private String discoveryPassword;

    private String deviceType;

    public static boolean flag;

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

    public String executeMonitor()
    {
        _dao.setNewId(id);

        if (true)
        {
            return "success";
        }
        else
        {
            return "error";
        }
    }

    public String provisionMonitor()
    {
        _dao.setNewId(id);

        if (UserDAO.enterMonitorTableData(id))
        {
            this.flag = true;

            return "success";
        }
        else
        {
            this.flag = false;

            return "error";
        }
    }

    public String getPolling()
    {
        UserDAO _dao = new UserDAO(id, ip);

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
