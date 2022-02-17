package com.motadata.kernel.helper;

import com.motadata.kernel.dao.DataAccess;

import java.util.Arrays;

import com.motadata.kernel.util.CommonConstant;

import com.motadata.kernel.util.Logger;

import com.motadata.kernel.util.SSHConnectionUtil;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.sql.Timestamp;

import java.text.DecimalFormat;

import java.util.List;

public class ServiceProvider
{
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

    public static int getId()
    {
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

    public ServiceProvider()
    {

    }

    public ServiceProvider(String ip, String deviceType)
    {

        this.ip = ip;

        this.deviceType = deviceType;
    }

    public ServiceProvider(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        this.name = name;

        this.ip = ip;

        this.discoveryUsername = discoveryUsername;

        this.discoveryPassword = discoveryPassword;

        this.deviceType = deviceType;
    }

    private static final Logger _logger = new Logger();

    public static boolean pollingDevice()
    {
        boolean status = true;

        List<String> dataList;

        try
        {
            if (deviceType.equals(CommonConstant.STRING_ZERO) || deviceType.equals(CommonConstant.PING_DEVICE))
            {
                String pingResult = "";

                String pingCmd = CommonConstant.PING_COMMAND + ip;

                Runtime runtime = null;

                Process process = null;

                Timestamp timestamp = null;

                BufferedReader bufferedInput = null;

                try
                {
                    runtime = Runtime.getRuntime();

                    process = runtime.exec(pingCmd);

                    timestamp = new Timestamp(System.currentTimeMillis());

                    bufferedInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    String inputLine;

                    while ((inputLine = bufferedInput.readLine()) != null)
                    {
                        pingResult = pingResult.concat(inputLine);
                    }

                    bufferedInput.close();

                    response = pingResult.substring(pingResult.indexOf("-"));

                    ipStatus = checkPingIpStatus(response, ip);

                    dataList = DataAccess.getReMonitorData(ip, deviceType);

                    if (!dataList.isEmpty())
                    {
                        setRediscoverProperties(dataList);

                        if (DataAccess.enterReMonitorData(ip, deviceType, response, ipStatus, timestamp.toString()))
                        {
                            if (DataAccess.enterReResultTableData(ip, deviceType, response, ipStatus, timestamp.toString()))
                            {
                                packet = getReceivedPacket(response);

                                memory = CommonConstant.DOUBLE_ZERO;

                                if (DataAccess.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus))
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

            if (deviceType.equals(CommonConstant.STRING_ONE) || deviceType.equals(CommonConstant.LINUX_DEVICE))
            {
                Timestamp timestamp = null;

                SSHConnectionUtil sshConnectionUtil = null;

                try
                {
                    dataList = DataAccess.getReMonitorData(ip, deviceType);

                    if (!dataList.isEmpty())
                    {
                        setRediscoverProperties(dataList);
                    }

                    sshConnectionUtil = SSHConnectionUtil.getSSHObject(ip, CommonConstant.SSH_PORT, discoveryUsername, discoveryPassword, CommonConstant.SSH_TIMEOUT);

                    if(sshConnectionUtil != null)
                    {
                        uName_Output = sshConnectionUtil.executeCommand(CommonConstant.LINUX_U_NAME_COMMAND, true);

                        free_Output = sshConnectionUtil.executeCommand(CommonConstant.LINUX_FREE_COMMAND, true);

                        df_Output = sshConnectionUtil.executeCommand(CommonConstant.LINUX_DISK_COMMAND, true);

                        ioStat_Output = sshConnectionUtil.executeCommand(CommonConstant.LINUX_CPU_COMMAND, true);

                        ipStatus = CommonConstant.DEVICE_UP;

                        timestamp = new Timestamp(System.currentTimeMillis());

                        specificData = getSpecificData(uName_Output, free_Output, df_Output, ioStat_Output);

                        _logger.info("Command output : " + specificData);
                    }
                    else
                    {
                        _logger.warn("ssh object is null!");

                        ipStatus = CommonConstant.DEVICE_DOWN;

                        specificData = CommonConstant.STRING_NULL;

                        timestamp = new Timestamp(System.currentTimeMillis());

                    }

                    if (!dataList.isEmpty())
                    {
                        if (DataAccess.enterReMonitorData(ip, deviceType, specificData, ipStatus, timestamp.toString()))
                        {
                            if (DataAccess.enterReResultTableData(ip, deviceType, specificData, ipStatus, timestamp.toString()))
                            {
                                packet = CommonConstant.STRING_ZERO;

                                memory = CommonConstant.DOUBLE_ZERO;

                                if (specificData != null && !specificData.equals(CommonConstant.STRING_NULL))
                                {
                                    memory = getFreeMemoryPercent(specificData);
                                }

                                if (DataAccess.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus))
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
                            _logger.warn("still not inserted data into tb_monitor & tb_result table!");
                        }
                    }

                }
                catch (Exception exception)
                {
                    _logger.error("something went wrong on linux discovery verify side!", exception);

                    status = false;
                }
                finally
                {
                    try
                    {
                        if (sshConnectionUtil != null)
                        {
                            sshConnectionUtil.destroy();
                        }

                    }
                    catch (Exception exception)
                    {
                        _logger.warn("ssh connection is not closed!");
                    }
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

            String[] ioSplit = ioStat_Output.split("\n");

            String CPU_System = ioSplit[3].substring(23, 31).trim();

            String CPU_User = ioSplit[3].substring(0, 15).trim();

            String sharedMemory = freeSplit[1].substring(43, 55).trim();

            String cacheMemory = freeSplit[1].substring(55, 67).trim();

            String []stringArray = {linux, ubuntu, x86, totalMemory, usedMemory, freeMemory, osVersion, osName, totalSwap, usedSwap, freeSwap, disk, CPU_User, CPU_System, sharedMemory, cacheMemory};

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
            if (packet.equals(CommonConstant.STRING_ZERO))
            {
                ipStatus = CommonConstant.DEVICE_DOWN;
            }
            else
            {
                ipStatus = CommonConstant.DEVICE_UP;
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
        double free = CommonConstant.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalMemory = Double.parseDouble(responseData[3].trim());

            double usedMemory = Double.parseDouble(responseData[4].trim());

            double sharedMemory = Double.parseDouble(responseData[14].trim());

            double cacheMemory = Double.parseDouble(responseData[15].substring(0, responseData[15].length() - 1).trim());

            free = (totalMemory - usedMemory - sharedMemory - cacheMemory) / totalMemory * 100;

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
        String rttTime = CommonConstant.STRING_ZERO;

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
                case CommonConstant.STRING_ZERO  : packetLoss = CommonConstant.STRING_HUNDRED;

                                                   break;

                case CommonConstant.STRING_ONE   : packetLoss = CommonConstant.STRING_SEVENTY_FIVE;

                                                   break;

                case CommonConstant.STRING_TWO   : packetLoss = CommonConstant.STRING_FIFTY;

                                                   break;

                case CommonConstant.STRING_THREE : packetLoss = CommonConstant.STRING_TWENTY_FIVE;

                                                   break;

                case CommonConstant.STRING_FOUR  : packetLoss = CommonConstant.STRING_ZERO;

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

    private static void setRediscoverProperties(List<String> dataList)
    {
        try
        {
            name = dataList.get(0);

            discoveryUsername = dataList.get(1);

            discoveryPassword = dataList.get(2);

            deviceType = dataList.get(3);

        }
        catch (Exception exception)
        {
            _logger.warn("not set your re discover data properties!");
        }
    }
}
