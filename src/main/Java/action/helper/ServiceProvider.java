package action.helper;

import action.dao.UserDAO;
import action.util.Logger;
import action.util.SSHConnectionUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;

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

    private static String uName_Output = null;

    private static String free_Output = null;

    private static String specificData = null;

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

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

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

                        if (UserDAO.enterReDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                        {
                            if (UserDAO.enterReResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                            {
                                _logger.debug("successfully data re-inserted into tb_discovery & tb_result table!");
                            }
                            else
                            {
                                _logger.warn("still not re-inserted data into tb_result table!");
                            }
                        }
                        else
                        {
                            _logger.warn("still not re-inserted into tb_discover and tb_result table!");
                        }
                    }
                    else
                    {
                        if (UserDAO.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                        {
                            if (UserDAO.enterResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                            {
                                _logger.debug("successfully data inserted into tb_discovery & tb_result table!");
                            }
                            else
                            {
                                _logger.debug("successfully data inserted into tb_discovery table!");

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
                Timestamp timestamp = null;

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
                        uName_Output = sshConnectionUtil.executeCommand("uname -a");

                        free_Output = sshConnectionUtil.executeCommand("free");

                        timestamp = new Timestamp(System.currentTimeMillis());

                        ipStatus = checkLinuxIpStatus(uName_Output, free_Output);

                        specificData = getSpecificData(uName_Output, free_Output);

                        _logger.info("Command output : " + "\n" + uName_Output + "\n" + free_Output);
                    }
                    else
                    {
                        _logger.warn("ssh object is null!");

                        status = false;
                    }

                    if (specificData != null && next)
                    {
                        if (UserDAO.enterReDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                        {
                            if (UserDAO.enterReResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                            {
                                _logger.debug("successfully re-inserted into tb_discovery & tb_result table!");
                            }

                            else
                            {
                                _logger.debug("successfully data inserted into tb_discovery table!");

                                _logger.warn("still not inserted data into tb_result table!");
                            }
                        }
                        else
                        {
                            _logger.warn("data not inserted into tb_discover and tb_result table!");
                        }
                    }
                    else if (!next)
                    {
                        if (UserDAO.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                        {
                            if (UserDAO.enterResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
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

    private static String getSpecificData(String uName_output, String free_output)
    {
        String data = null;

        String[] splitData;

        try
        {
            String linux = uName_output.substring(uName_output.indexOf("Linux"), 5);

            String ubuntu = uName_output.substring(uName_output.indexOf("ubuntu"), 12);

            String x86 = uName_output.substring(uName_output.indexOf("x86"), 82);

            splitData = free_output.split("\n");

            String totalMemory = splitData[1].substring(4, 19).trim();

            String usedMemory = splitData[1].substring(19, 31).trim();

            String freeMemory = splitData[1].substring(31, 43).trim();

            String []stringArray = {linux, ubuntu, x86, totalMemory, usedMemory, freeMemory};

            data = Arrays.toString(stringArray);

        }
        catch (Exception exception)
        {
            _logger.warn("not get specific data from linux output!");
        }

        return data;
    }

    private static String checkLinuxIpStatus(String uName_Output, String free_Output)
    {
        try
        {
            if (uName_Output.equals("") || free_Output.equals(""))
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
