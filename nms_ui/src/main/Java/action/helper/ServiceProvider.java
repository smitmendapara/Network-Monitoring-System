package action.helper;

import dao.UserDAO;

import util.CommonConstantUI;

import util.Logger;

import util.SSHConnectionUtil;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.sql.ResultSet;

import java.sql.Timestamp;

import java.text.DecimalFormat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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

    private static String packet = null;

    private static Double memory;

    private static String uName_Output = null;

    private static String free_Output = null;

    private static String df_Output = null;

    private static String ioStat_Output = null;

    private static String specificData = null;

    public void setId(int id)
    {
        this.id = id;
    }

    public static int getId()
    {
        return id;
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

    public ServiceProvider(String ip, String deviceType)
    {
        this.ip = ip;

        this.deviceType = deviceType;
    }

    public ServiceProvider()
    {

    }

    private static final Logger _logger = new Logger();

    public static boolean checkDiscovery()
    {
        boolean status = true;

        try
        {
            if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
            {
                String pingResult = "";

                String pingCmd = CommonConstantUI.PING_COMMAND + ip;

                Runtime runtime = null;

                Process process = null;

                Timestamp timestamp = null;

                BufferedReader bufferedInput = null;

                ResultSet resultSet = null;

                try
                {
                    runtime = Runtime.getRuntime();

                    process = runtime.exec(pingCmd);

                    timestamp = new Timestamp(System.currentTimeMillis());

                    bufferedInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    String inputLine;

                    while ((inputLine = bufferedInput.readLine()) != null)
                    {
                        pingResult += inputLine;
                    }

                    bufferedInput.close();

                    String string = pingResult.toString();

                    response = string.substring(string.indexOf("-"));

                    ipStatus = checkPingIpStatus(response, ip);

                    resultSet = UserDAO.getReDiscoveryData(ip, deviceType);

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

            if (deviceType.equals(CommonConstantUI.STRING_ONE) || deviceType.equals(CommonConstantUI.LINUX_DEVICE))
            {
                ResultSet resultSet = null;

                Timestamp timestamp = null;

                SSHConnectionUtil sshConnectionUtil = null;

                try
                {
                    resultSet = UserDAO.getReDiscoveryData(ip, deviceType);

                    boolean next = resultSet.next();

                    if (next)
                    {
                        setRediscoverProperties(resultSet);
                    }

                    sshConnectionUtil = SSHConnectionUtil.getSSHObject(ip, CommonConstantUI.SSH_PORT, discoveryUsername, discoveryPassword, CommonConstantUI.SSH_TIMEOUT);

                    if(sshConnectionUtil != null)
                    {
                        uName_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_U_NAME_COMMAND, true);

                        free_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_FREE_COMMAND, true);

                        df_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_DISK_COMMAND, true);

                        ioStat_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_CPU_COMMAND, true);

                        timestamp = new Timestamp(System.currentTimeMillis());

                        ipStatus = CommonConstantUI.DEVICE_UP;

                        specificData = getSpecificData(uName_Output, free_Output, df_Output, ioStat_Output);

                        _logger.info("Command output : " + specificData);
                    }
                    else
                    {
                        _logger.warn("ssh object is null!");

                        ipStatus = CommonConstantUI.DEVICE_DOWN;

                        specificData = CommonConstantUI.STRING_NULL;

                        timestamp = new Timestamp(System.currentTimeMillis());
                    }

                    if (next)
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

    public static boolean pollingDevice()
    {
        boolean status = true;

        try
        {
            if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
            {
                String pingResult = "";

                String pingCmd = CommonConstantUI.PING_COMMAND + ip;

                Runtime runtime = null;

                Process process = null;

                Timestamp timestamp = null;

                BufferedReader bufferedInput = null;

                ResultSet resultSet = null;

                try
                {
                    runtime = Runtime.getRuntime();

                    process = runtime.exec(pingCmd);

                    timestamp = new Timestamp(System.currentTimeMillis());

                    bufferedInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    String inputLine;

                    while ((inputLine = bufferedInput.readLine()) != null)
                    {
                        pingResult += inputLine;
                    }

                    bufferedInput.close();

                    String string = pingResult.toString();

                    response = string.substring(string.indexOf("-"));

                    ipStatus = checkPingIpStatus(response, ip);

                    resultSet = UserDAO.getReMonitorData(ip, deviceType);

                    boolean next = resultSet.next();

                    if (next)
                    {
                        setRediscoverProperties(resultSet);

                        if (UserDAO.enterReMonitorData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                        {
                            if (UserDAO.enterReResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                            {
                                packet = getReceivedPacket(response);

                                memory = CommonConstantUI.DOUBLE_ZERO;

                                if (UserDAO.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus))
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

            if (deviceType.equals(CommonConstantUI.STRING_ONE) || deviceType.equals(CommonConstantUI.LINUX_DEVICE))
            {
                ResultSet resultSet = null;

                Timestamp timestamp = null;

                SSHConnectionUtil sshConnectionUtil = null;

                try
                {
                    resultSet = UserDAO.getReMonitorData(ip, deviceType);

                    boolean next = resultSet.next();

                    if (next)
                    {
                        setRediscoverProperties(resultSet);
                    }

                    sshConnectionUtil = SSHConnectionUtil.getSSHObject(ip, CommonConstantUI.SSH_PORT, discoveryUsername, discoveryPassword, CommonConstantUI.SSH_TIMEOUT);

                    if(sshConnectionUtil != null)
                    {
                        uName_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_U_NAME_COMMAND, true);

                        free_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_FREE_COMMAND, true);

                        df_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_DISK_COMMAND, true);

                        ioStat_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_CPU_COMMAND, true);

                        ipStatus = CommonConstantUI.DEVICE_UP;

                        timestamp = new Timestamp(System.currentTimeMillis());

                        specificData = getSpecificData(uName_Output, free_Output, df_Output, ioStat_Output);

                        _logger.info("Command output : " + specificData);
                    }
                    else
                    {
                        _logger.warn("ssh object is null!");

                        ipStatus = CommonConstantUI.DEVICE_DOWN;

                        specificData = CommonConstantUI.STRING_NULL;

                        timestamp = new Timestamp(System.currentTimeMillis());

                    }

                    if (next)
                    {
                        if (UserDAO.enterReMonitorData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                        {
                            if (UserDAO.enterReResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                            {
                                packet = CommonConstantUI.STRING_NULL; // for manually polling -> set null instead of 0

                                memory = CommonConstantUI.DOUBLE_ZERO;

                                if (specificData != null && !specificData.equals(CommonConstantUI.STRING_NULL))
                                {
                                    memory = getFreeMemoryPercent(specificData);
                                }

                                if (UserDAO.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus))
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
            if (packet.equals(CommonConstantUI.STRING_ZERO))
            {
                ipStatus = CommonConstantUI.DEVICE_DOWN;
            }
            else
            {
                ipStatus = CommonConstantUI.DEVICE_UP;
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
        double free = CommonConstantUI.DOUBLE_ZERO;

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

    public static Double getUsedMemoryPercent(String linuxResponse)
    {
        double used = CommonConstantUI.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalMemory = Double.parseDouble(responseData[3].trim());

            double usedMemory = Double.parseDouble(responseData[4].trim());

            used = (usedMemory / totalMemory) * 100;

            used = Double.parseDouble(new DecimalFormat("##.##").format(used));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting used memory percent!");
        }

        return used;
    }

    public static Double getFreeSwapPercent(String linuxResponse)
    {
        double free = CommonConstantUI.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalSwap = Double.parseDouble(responseData[8].trim());

            double freeSwap = Double.parseDouble(responseData[10].trim());

            free = (freeSwap / totalSwap) * 100;

            free = Double.parseDouble(new DecimalFormat("##.##").format(free));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting used swap percent!");
        }

        return free;
    }

    public static Double getUsedSwapPercent(String linuxResponse)
    {
        double used = CommonConstantUI.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalSwap = Double.parseDouble(responseData[8].trim());

            double usedSwap = Double.parseDouble(responseData[9].trim());

            used = (usedSwap / totalSwap) * 100;

            used = Double.parseDouble(new DecimalFormat("##.##").format(used));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting used swap percent!");
        }

        return used;
    }

    public static String getOSVersion(String linuxResponse)
    {
        String OSVersion = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            OSVersion = responseData[6].trim();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting os version!");
        }

        return OSVersion;
    }

    public static String getCPUType(String linuxResponse)
    {
        String CPUType = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            CPUType = responseData[2].trim();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting cpu type!");
        }

        return CPUType;
    }

    public static String getSystemName(String linuxResponse)
    {
        String systemName = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            systemName = responseData[1].trim();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting system name!");
        }

        return systemName;
    }

    public static String getOSName(String linuxResponse)
    {
        String OS_Name = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            OS_Name = responseData[7].trim();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting os name!");
        }

        return OS_Name;
    }

    public static StringBuffer getDeviceType(String linuxResponse)
    {
        StringBuffer Device_Type = null;

        try
        {
            String[] responseData = linuxResponse.split(",");

            Device_Type = new StringBuffer(responseData[0].trim());

            Device_Type = Device_Type.deleteCharAt(0);
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting device type!");
        }

        return Device_Type;
    }

    public static StringBuffer getSystemCPUPercent(String linuxResponse)
    {
        StringBuffer CPU_System = null;

        try
        {
            String[] responseData = linuxResponse.split(",");

            CPU_System = new StringBuffer(responseData[13].trim());

            CPU_System = CPU_System.deleteCharAt(CPU_System.length() - 1);
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting system cpu!");
        }

        return CPU_System;
    }

    public static String getUserCPUPercent(String linuxResponse)
    {
        String CPU_USer = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            CPU_USer = responseData[12].trim();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting user cpu!");
        }

        return CPU_USer;
    }

    public static String getDiskPercent(String linuxResponse)
    {
        String disk = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            disk = responseData[11].trim();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting disk!");
        }

        return disk;
    }

    public static String getRTTTime(String subString)
    {
        String rttTime = CommonConstantUI.STRING_ZERO;

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
        String receivedPacket = CommonConstantUI.NULL;

        String packetLoss = CommonConstantUI.NULL;

        try
        {
            receivedPacket = subString.substring(subString.indexOf("transmitted") + 13, subString.indexOf("transmitted") + 14);

            switch (receivedPacket)
            {
                case CommonConstantUI.STRING_ZERO  : packetLoss = CommonConstantUI.STRING_HUNDRED;

                                                     break;

                case CommonConstantUI.STRING_ONE   : packetLoss = CommonConstantUI.STRING_SEVENTY_FIVE;

                                                     break;

                case CommonConstantUI.STRING_TWO   : packetLoss = CommonConstantUI.STRING_FIFTY;

                                                     break;

                case CommonConstantUI.STRING_THREE : packetLoss = CommonConstantUI.STRING_TWENTY_FIVE;

                                                     break;

                case CommonConstantUI.STRING_FOUR  : packetLoss = CommonConstantUI.STRING_ZERO;

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
        String sentPacket = CommonConstantUI.NULL;

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
        String packet = CommonConstantUI.NULL;

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

    public static String[] getDateTime()
    {
        String[] currentDate = null;

        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Date date = new Date();

            Calendar currentTime = Calendar.getInstance();

            currentTime.setTime(date);

            String firstTime = dateFormat.format(date);


            currentTime.add(Calendar.MINUTE, -5);

            Date date2 = currentTime.getTime();

            String secondTime = dateFormat.format(date2);


            currentTime.add(Calendar.MINUTE, -5);

            Date date3 = currentTime.getTime();

            String thirdTime = dateFormat.format(date3);


            currentTime.add(Calendar.MINUTE, -5);

            Date date4 = currentTime.getTime();

            String fourthTime = dateFormat.format(date4);


            currentTime.add(Calendar.MINUTE, -5);

            Date date5 = currentTime.getTime();

            String fifthTime = dateFormat.format(date5);


            currentTime.add(Calendar.MINUTE, -5);

            Date date6 = currentTime.getTime();

            String sixthTime = dateFormat.format(date6);


            currentTime.add(Calendar.MINUTE, -5);

            Date date7 = currentTime.getTime();

            String sevenTime = dateFormat.format(date7);


            currentTime.add(Calendar.MINUTE, -5);

            Date date8 = currentTime.getTime();

            String eightTime = dateFormat.format(date8);

            currentDate = new String[]{firstTime, secondTime, thirdTime, fourthTime, fifthTime, sixthTime, sevenTime, eightTime};

        }
        catch (Exception exception)
        {
            _logger.warn("still not get current date and time!");
        }

        return currentDate;
    }

}
