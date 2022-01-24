package action.discover;

import action.dao.UserDAO;
import action.helper.ServiceProvider;
import action.util.Logger;
import com.opensymphony.xwork2.ActionSupport;

import java.sql.ResultSet;

/**ex
 * Created by smit on 3/1/22.
 */
public class Discovery extends ActionSupport
{
    private int idAttribute;

    private String name;

    private String ip;

    private String discoveryUsername;

    private String discoveryPassword;

    private String deviceType;

    private Object result;

    private static final Logger _logger = new Logger();

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
        ServiceProvider.setIp(ip);

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
        try
        {
            ResultSet resultSet = UserDAO.getReDiscoveryData(ip);

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
