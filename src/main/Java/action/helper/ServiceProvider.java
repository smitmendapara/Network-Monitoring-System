package action.helper;

import action.dao.UserDAO;
import action.util.Logger;
import action.util.SSHConnectionUtil;

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

    private static String discoveryUsername;

    private static String discoveryPassword;

    private static String deviceType;

    private static String ipStatus;

    public static String getIp()
    {
        return ip;
    }

    public static void setIp(String ip)
    {
        ServiceProvider.ip = ip;
    }

    public ServiceProvider(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        this.name = name;

        this.ip = ip;

        this.discoveryUsername = discoveryUsername;

        this.discoveryPassword = discoveryPassword;

        this.deviceType = deviceType;
    }

    public ServiceProvider(String ip, String deviceType) {

        this.ip = ip;

        this.deviceType = deviceType;
    }

    public ServiceProvider() {

    }

    public static boolean checkDiscovery()
    {
        boolean status = true;

        try
        {
            if (deviceType.equals("0") || deviceType.equals("Ping"))
            {
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

                    ipStatus = checkIpStatus(subString, ip);

                    ResultSet resultSet = UserDAO.getReDiscoveryData(ip);

                    boolean next = resultSet.next();

                    if (next)
                    {
                        setRediscoverProperties(resultSet);

                        if (UserDAO.enterReDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, subString))
                        {
                            return status;
                        }
                    }
                    else
                    {
                        if (UserDAO.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, subString))
                        {
                            if (UserDAO.enterResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType))
                            {
                                _logger.debug("successfully data inserted into tb_discovery & tb_result table!");
                            }
                            else
                            {
                                _logger.warn("still not inserted data into tb_result table!");
                            }
                        }
                        else
                        {
                            _logger.warn("still not inserted into tb_discover and tb_result table!");
                        }
                    }
                }
                catch (Exception exception)
                {
                    _logger.error("something went wrong on ping discovery verify side!", exception);

                    status = false;
                }

                return status;

            }

            if (deviceType.equals("1") || deviceType.equals("Linux"))
            {
                try
                {
                    ResultSet resultSet = UserDAO.getReDiscoveryData(ip);

                    boolean next = resultSet.next();

                    if (next)
                    {
                        setRediscoverProperties(resultSet);
                    }

                    SSHConnectionUtil sshConnectionUtil = SSHConnectionUtil.getNewSSHObject(ip, 22, discoveryUsername, discoveryPassword, 30);

                    String commandOutput = null;

                    if(sshConnectionUtil != null)
                    {
                        commandOutput = sshConnectionUtil.executeCommand("uname -a");

                        _logger.info("Command output : " + commandOutput);
                    }
                    else
                    {
                        _logger.warn("ssh object is null!");

                        status = false;
                    }

                    if (commandOutput != null && next)
                    {
                        if (UserDAO.enterReDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, commandOutput))
                        {
                            _logger.debug("successfully data inserted into tb_discovery table!");
                        }
                        else
                        {
                            status = false;
                        }
                    }
                    else if (!next)
                    {
                        if (UserDAO.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, commandOutput))
                        {
                            if (UserDAO.enterResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType))
                            {
                                _logger.debug("successfully data inserted into tb_discovery & tb_result table!");
                            }
                            else
                            {
                                _logger.warn("still not inserted data into tb_result table!");
                            }
                        }
                        else
                        {
                            _logger.warn("data not inserted into tb_discover and tb_result table!");
                        }
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

    private static String checkIpStatus(String subString, String ip)
    {
        String packet;

        try
        {
            if (ip.length() == 9)
            {
                packet = subString.substring(56, 57);

                checkStatus(packet);

            }
            if (ip.length() == 10)
            {
                packet = subString.substring(57, 58);

                checkStatus(packet);

            }
            if (ip.length() == 11)
            {
                packet = subString.substring(58, 59);

                checkStatus(packet);

            }
            if (ip.length() == 12)
            {
                packet = subString.substring(59, 60);

                checkStatus(packet);

            }
            if (ip.length() == 13)
            {
                packet = subString.substring(60, 61);

                checkStatus(packet);

            }
            if (ip.length() == 14)
            {
                packet = subString.substring(61, 62);

                checkStatus(packet);

            }
            if (ip.length() == 15)
            {
                packet = subString.substring(62, 63);

                checkStatus(packet);

            }
        }
        catch (Exception exception)
        {
            _logger.warn("does not check IP status");
        }

        return ipStatus;
    }

    private static void checkStatus(String packet)
    {
        try
        {
            if (packet.equals("0"))
            {
                ipStatus = "Down";
            }
            else
            {
                ipStatus = "Up";
            }
        }
        catch (Exception exception)
        {
            _logger.warn("doesn't check ip status!");
        }
    }

    private static void setRediscoverProperties(ResultSet resultSet)
    {
        try
        {
            name = resultSet.getString(2);

            discoveryUsername = resultSet.getString(4);

            discoveryPassword = resultSet.getString(5);

            deviceType = resultSet.getString(6);

        }
        catch (Exception exception)
        {
            _logger.warn("not set your re discover data properties!");
        }
    }
}
