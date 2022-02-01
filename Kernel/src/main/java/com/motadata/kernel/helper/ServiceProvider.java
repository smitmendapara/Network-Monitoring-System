package com.motadata.kernel.helper;

import com.motadata.kernel.dao.DataAccess;

import com.motadata.kernel.util.Logger;

import com.motadata.kernel.util.SSHConnectionUtil;

import javax.xml.crypto.Data;
import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.sql.ResultSet;

import java.sql.Timestamp;

import java.text.DecimalFormat;
import java.util.Arrays;

public class ServiceProvider
{
    private static final Logger _logger = new Logger();

    private static int id;

    private static String name;

    private static String ip;

    private static String discoveryUsername;

    private static String discoveryPassword;

    private static String deviceType;

    private static String ipStatus;

    private static String response;

    private static String uName_Output = null;

    private static String free_Output = null;

    private static String df_Output = null;

    private static String ioStat_Output = null;

    private static String specificData = null;

    private static String packet = null;

    private static Double memory;

    public static int getId() {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public static String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
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

                    ResultSet resultSet = DataAccess.getReDiscoveryData(ip);

                    boolean next = resultSet.next();

                    if (next)
                    {
                        setRediscoverProperties(resultSet);

                        if (DataAccess.enterReMonitorData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                        {
                            if (DataAccess.enterReDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                            {
                                if (DataAccess.enterReResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                                {
                                    packet = getReceivedPacket(response);

                                    memory = 0.0;

                                    if (DataAccess.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString()))
                                    {
                                        _logger.debug("successfully data re-inserted into tb_monitor, tb_discovery, tb_result & tb_dataDump table!");
                                    }
                                    else
                                    {
                                        _logger.debug("successfully data re-inserted into tb_discovery & tb_result table!");
                                    }
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
                            _logger.warn("still not re-inserted into tb_monitor, tb_discover and tb_result table!");
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
                    ResultSet resultSet = DataAccess.getReDiscoveryData(ip);

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

                        df_Output = sshConnectionUtil.executeCommand("df -h");

                        ioStat_Output = sshConnectionUtil.executeCommand("iostat");

                        timestamp = new Timestamp(System.currentTimeMillis());

                        ipStatus = "Up";

                        specificData = getSpecificData(uName_Output, free_Output, df_Output, ioStat_Output);

                        _logger.info("Command output : " + "\n" + uName_Output + "\n" + free_Output + "\n" + specificData);
                    }
                    else
                    {
                        _logger.warn("ssh object is null!");

                        ipStatus = "Down";

                        status = false;
                    }

                    if (specificData != null && next)
                    {
                        if (DataAccess.enterReMonitorData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                        {
                            if (DataAccess.enterReDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                            {
                                if (DataAccess.enterReResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                                {
                                    packet = "0";

                                    memory = getFreeMemoryPercent(specificData);

                                    if (DataAccess.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString()))
                                    {
                                        _logger.debug("successfully data re-inserted into tb_monitor, tb_discovery, tb_result & tb_dataDump table!");
                                    }
                                    else
                                    {
                                        _logger.debug("successfully data re-inserted into tb_discovery & tb_result table!");
                                    }
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
                        else
                        {

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

    private static String getSpecificData(String uName_output, String free_output, String df_Output, String ioStat_Output)
    {
        String data = null;

        try
        {
            String linux = uName_output.substring(uName_output.indexOf("Linux"), 5);

            String ubuntu = uName_output.substring(uName_output.indexOf("ubuntu"), 12);

            String x86 = uName_output.substring(uName_output.indexOf("x86"), 82);

            String[] freeSplit = free_output.split("\n");

            String totalMemory = freeSplit[1].substring(4, 19).trim();

            String usedMemory = freeSplit[1].substring(19, 31).trim();

            String freeMemory = freeSplit[1].substring(31, 43).trim();

            String osVersion = uName_output.substring(uName_output.indexOf("ubuntu") + 7, 30);

            String osName = uName_output.substring(uName_output.indexOf("GNU"), 106);

            String totalSwap = freeSplit[2].substring(5, 19).trim();

            String usedSwap = freeSplit[2].substring(19, 31).trim();

            String freeSwap = freeSplit[2].substring(31, 43).trim();

            String[] dfSplit = df_Output.split("\n");

            String disk = dfSplit[8].substring(42, 46).trim();

//            String[] topSplit = top_Output.split("\n");
//
//            String threads = topSplit[2].substring(8, 14).trim();

            String[] ioSplit = ioStat_Output.split("\n");

            String CPU_System = ioSplit[3].substring(23, 31).trim();

            String CPU_User = ioSplit[3].substring(0, 15).trim();

            String []stringArray = {linux, ubuntu, x86, totalMemory, usedMemory, freeMemory, osVersion, osName, totalSwap, usedSwap, freeSwap, disk, CPU_User, CPU_System};

            data = Arrays.toString(stringArray);

        }
        catch (Exception exception)
        {
            _logger.warn("not get specific data from linux output!");
        }

        return data;
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

    public static Double getFreeMemoryPercent(String linuxResponse)
    {
        double free = 0;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalMemory = Double.parseDouble(responseData[3].trim());

            double freeMemory = Double.parseDouble(responseData[5].trim());

            free = (freeMemory / totalMemory) * 100;

            free = Double.parseDouble(new DecimalFormat("##.##").format(free));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting free memory percent!");
        }

        return free;
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
