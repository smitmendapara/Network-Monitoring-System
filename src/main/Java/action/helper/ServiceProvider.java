package action.helper;

import action.dao.UserDAO;
import action.util.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;

/**
 * Created by smit on 17/1/22.
 */
public class ServiceProvider
{
    private static final Logger _logger = new Logger();

    private static String name;

    private static String ip;

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        ServiceProvider.ip = ip;
    }

    private static String discoveryUsername;

    private static String discoveryPassword;

    private static String deviceType;

    public ServiceProvider(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        this.name = name;

        this.ip = ip;

        this.discoveryUsername = discoveryUsername;

        this.discoveryPassword = discoveryPassword;

        this.deviceType = deviceType;
    }

    public ServiceProvider(String ip) {

        this.ip = ip;
    }

    public ServiceProvider() {

    }

    public static boolean checkDiscovery()
    {
        boolean status = true;

        String pingResult = "";

        String pingCmd = "ping -c 4 " + ip;

        try
        {
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(pingCmd);

            BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String inputLine;

            while ((inputLine = bufferedInput.readLine()) != null)
            {
                pingResult += inputLine;
            }

            bufferedInput.close();

            String string = pingResult.toString();

            String subString = string.substring(string.indexOf("-"));

//            if (!subString.equals(""))
//            {
//                return true;
//            }

            ResultSet resultSet = UserDAO.getReDiscoveryData(ip);

            if (resultSet != null)
            {
                while (resultSet.next())
                {
                    name = resultSet.getString(2);

                    discoveryUsername = resultSet.getString(4);

                    discoveryPassword = resultSet.getString(5);

                    deviceType = resultSet.getString(6);
                }
            }

            if (UserDAO.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, subString))
            {
                if (UserDAO.enterResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType))
                {
                    return status;
                }
            }

        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on discovery verifyside!", exception);

            status = false;
        }

        return status;
    }
}
