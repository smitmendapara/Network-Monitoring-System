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

    private static String response;

    private static String commandOutput = null;

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

                    response = string.substring(string.indexOf("-"));

                    ipStatus = checkPingIpStatus(response, ip);

                    ResultSet resultSet = UserDAO.getReDiscoveryData(ip);

                    boolean next = resultSet.next();

                    if (next)
                    {
                        setRediscoverProperties(resultSet);

                        if (UserDAO.enterReDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus))
                        {
                            return status;
                        }
                    }
                    else
                    {
                        if (UserDAO.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus))
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



                    if(sshConnectionUtil != null)
                    {
                        commandOutput = sshConnectionUtil.executeCommand("uname -a");

                        ipStatus = checkLinuxIpStatus(commandOutput);

                        _logger.info("Command output : " + commandOutput);
                    }
                    else
                    {
                        _logger.warn("ssh object is null!");

                        status = false;
                    }

                    if (commandOutput != null && next)
                    {
                        if (UserDAO.enterReDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, commandOutput, ipStatus))
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
                        if (UserDAO.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, commandOutput, ipStatus))
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

    private static String checkLinuxIpStatus(String commandOutput)
    {
        try
        {
            if (commandOutput.equals(""))
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
            _logger.warn("doesn't check linux ip status!");
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
            _logger.warn("doesn't check ping ip status!");
        }
    }

    private static String checkPingIpStatus(String subString, String ip)
    {
        String packet;

        try
        {
            packet = subString.substring(subString.indexOf("transmitted") + 13, subString.indexOf("transmitted") + 14);

            checkStatus(packet);

        }
        catch (Exception exception)
        {
            _logger.warn("does not check IP status");
        }

        return ipStatus;
    }

    public static String getRTTTime(String subString)
    {
        String rttTime = "0";

        try
        {
            int rtt = subString.indexOf("rtt");

            if (rtt != -1)
            {
                rttTime = subString.substring(subString.indexOf("rtt") + 23, subString.indexOf("rtt") + 24);
            }

        }
        catch (Exception exception)
        {
            _logger.warn("packet not receive!");
        }

        return rttTime;
    }

    public static String getPacketLoss(String subString)
    {
        String receivedPacket = null;

        String packetLoss = null;

        try
        {
            receivedPacket = subString.substring(subString.indexOf("transmitted") + 13, subString.indexOf("transmitted") + 14);

            switch (receivedPacket)
            {
                case "0" : packetLoss = "100";

                           break;

                case "1" : packetLoss = "75";

                           break;

                case "2" : packetLoss = "50";

                           break;

                case "3" : packetLoss = "25";

                           break;

                case "4" : packetLoss = "0";

                           break;
            }

//            if (receivedPacket == "0")
//            {
//                packetLoss = "100";
//            }
//            if (receivedPacket == "1")
//            {
//                packetLoss = "75";
//            }
//            if (receivedPacket == "2")
//            {
//                packetLoss = "50";
//            }
//            if (receivedPacket == "3")
//            {
//                packetLoss = "25";
//            }
//            if (receivedPacket == "4")
//            {
//                packetLoss = "0";
//            }
        }
        catch (Exception exception)
        {
            _logger.warn("packet not receive!");
        }

        return packetLoss;
    }

    public static String getSentPacket(String subString)
    {
        String sentPacket = null;

        try
        {
            sentPacket = subString.substring(subString.indexOf("statistics") + 14, subString.indexOf("statistics") + 15);
        }
        catch (Exception exception)
        {
            _logger.warn("packet not receive!");
        }

        return sentPacket;
    }

    public static String getReceivedPacket(String subString)
    {
        String packet = null;

        try
        {
            packet = subString.substring(subString.indexOf("transmitted") + 13, subString.indexOf("transmitted") + 14);
        }
        catch (Exception exception)
        {
            _logger.warn("packet not receive!");
        }

        return packet;
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
