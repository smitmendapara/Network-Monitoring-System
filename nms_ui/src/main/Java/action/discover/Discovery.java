package action.discover;

import action.dao.UserDAO;
import action.util.Logger;

/**
 * Created by smit on 3/1/22.
 */
public class Discovery
{
    private String name;

    private String ip;

    private String discoveryUsername;

    private String discoveryPassword;

    private String deviceType;

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

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String executeDiscovery()
    {
        try
        {
            if (UserDAO.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType))
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
            _logger.error("discovery failed!", exception);
        }

        return null;
    }
}
