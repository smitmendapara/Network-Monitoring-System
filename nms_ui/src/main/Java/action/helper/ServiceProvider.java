package action.helper;

import dao.UserDAO;

import util.CommonConstantUI;

import util.Logger;

import util.SSHConnectionUtil;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Arrays;

import java.util.Calendar;

import java.util.Date;

import java.util.List;

public class ServiceProvider
{
    private int id;

    private String name;

    private String ip;

    private String discoveryUsername;

    private String discoveryPassword;

    private String deviceType;

    private String ipStatus;

    private String response;

    private String uName_Output = null;

    private String free_Output = null;

    private String df_Output = null;

    private String ioStat_Output = null;

    private String specificData = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getIpStatus() {
        return ipStatus;
    }

    public void setIpStatus(String ipStatus) {
        this.ipStatus = ipStatus;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private List<List<String>> dataList;

    private static final Logger _logger = new Logger();

    private static final UserDAO _dao = new UserDAO();

    public boolean checkDiscovery()
    {
        boolean status = true;

        try
        {
            if (checkIpExist(id, ip, deviceType))
            {
                if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
                {
                    Runtime runtime;

                    Process process;

                    Timestamp timestamp;

                    BufferedReader bufferedInput;

                    String pingResult = "";

                    String pingCmd = CommonConstantUI.PING_COMMAND + ip;

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

                        ipStatus = checkPingIpStatus(response);

                        dataList = _dao.getReDiscoveryData(ip, deviceType);

                        if (!dataList.isEmpty())
                        {
                            setRediscoverProperties(dataList);

                            if (_dao.enterReDiscoveryData(ip, deviceType, response, ipStatus, timestamp.toString()))
                            {
                                if (_dao.enterReResultTableData(ip, deviceType, response, ipStatus, timestamp.toString()))
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
                            if (_dao.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                            {
                                if (_dao.enterResultTableData(ip, discoveryUsername, deviceType, response, ipStatus, timestamp.toString()))
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
                    Timestamp timestamp;

                    SSHConnectionUtil sshConnectionUtil = null;

                    try
                    {
                        dataList = _dao.getReDiscoveryData(ip, deviceType);

                        if (!dataList.isEmpty())
                        {
                            setRediscoverProperties(dataList);
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

                        if (!dataList.isEmpty())
                        {
                            if (_dao.enterReDiscoveryData(ip, deviceType, specificData, ipStatus, timestamp.toString()))
                            {
                                if (_dao.enterReResultTableData(ip, deviceType, specificData, ipStatus, timestamp.toString()))
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
                        else if (!dataList.isEmpty())
                        {
                            if (_dao.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                            {
                                if (_dao.enterResultTableData(ip, discoveryUsername, deviceType, specificData, ipStatus, timestamp.toString()))
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
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on discovery side!", exception);

            status = false;
        }

        return status;

    }

    private boolean checkIpExist(int id, String ip, String deviceType)
    {
        boolean status = true;

        try
        {
            List<List<String>> discoverTB = _dao.getDiscoverTB();

            if (deviceType.equals("0"))
            {
                deviceType = "Ping";
            }
            if (deviceType.equals("1"))
            {
                deviceType = "Linux";
            }

            for (List<String> subList : discoverTB)
            {
                if (Integer.parseInt(subList.get(0)) != id && subList.get(2).equals(ip) && subList.get(3).equals(deviceType))
                {
                    status = false;
                }
            }
        }
        catch (Exception exception)
        {
            _logger.warn("something went wrong on checking existing ip!");
        }

        return status;
    }

    public boolean pollingDevice()
    {
        boolean status = true;

        try
        {
            if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
            {
                String pingResult = "";

                String pingCmd = CommonConstantUI.PING_COMMAND + ip;

                Runtime runtime;

                Process process;

                Timestamp timestamp;

                BufferedReader bufferedInput;

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

                    ipStatus = checkPingIpStatus(response);

                    dataList = _dao.getReMonitorData(ip, deviceType);

                    if (!dataList.isEmpty())
                    {
                        setRediscoverProperties(dataList);

                        if (_dao.enterReMonitorData(ip, deviceType, response, ipStatus, timestamp.toString()))
                        {
                            if (_dao.enterReResultTableData(ip, deviceType, specificData, ipStatus, timestamp.toString()))
                            {
                                String packet = getReceivedPacket(response);

                                double memory = CommonConstantUI.DOUBLE_ZERO;

                                if (_dao.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus))
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
                Timestamp timestamp;

                SSHConnectionUtil sshConnectionUtil = null;

                try
                {
                    dataList = _dao.getReMonitorData(ip, deviceType);

                    if (!dataList.isEmpty())
                    {
                        setRediscoverProperties(dataList);
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

                    if (!dataList.isEmpty())
                    {
                        if (_dao.enterReMonitorData(ip, deviceType, specificData, ipStatus, timestamp.toString()))
                        {
                            if (_dao.enterReResultTableData(ip, deviceType, specificData, ipStatus, timestamp.toString()))
                            {
                                String packet = CommonConstantUI.STRING_NULL; // for manually polling -> set null instead of 0

                                double memory = CommonConstantUI.DOUBLE_ZERO;

                                if (specificData != null && !specificData.equals(CommonConstantUI.STRING_NULL))
                                {
                                    memory = Double.parseDouble(getFreeMemoryPercent(specificData));
                                }

                                if (_dao.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus))
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

    private String getSpecificData(String uName_output, String free_output, String df_Output, String ioStat_Output)
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

    private void checkStatus(String packet)
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

    private String checkPingIpStatus(String subString)
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

    private String getFreeMemoryPercent(String linuxResponse)
    {
        String free = CommonConstantUI.STRING_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalMemory = Double.parseDouble(responseData[3].trim());

            double usedMemory = Double.parseDouble(responseData[4].trim());

            double sharedMemory = Double.parseDouble(responseData[14].trim());

            double bufferMemory = Double.parseDouble(responseData[15].substring(0, responseData[15].length() - 1).trim());

            double freeMemory = ( (totalMemory - usedMemory - sharedMemory - bufferMemory) / totalMemory ) * 100;

            free = String.valueOf(Math.round(freeMemory)) + " %";
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting free memory percent!");
        }

        return free;
    }

    private String getUsedMemoryPercent(String linuxResponse)
    {
        String used = CommonConstantUI.STRING_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalMemory = Double.parseDouble(responseData[3].trim());

            double freeMemory = Double.parseDouble(responseData[5].trim());

            double sharedMemory = Double.parseDouble(responseData[14].trim());

            double bufferMemory = Double.parseDouble(responseData[15].substring(0, responseData[15].length() - 1).trim());

            double usedMemory = ( (totalMemory - freeMemory - sharedMemory - bufferMemory) / totalMemory ) * 100;

            used = String.valueOf(Math.round(usedMemory)) + " %";
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting used memory percent!");
        }

        return used;
    }

    private String getFreeSwapPercent(String linuxResponse)
    {
        String free = CommonConstantUI.STRING_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalSwap = Double.parseDouble(responseData[8].trim());

            double freeSwap = Double.parseDouble(responseData[10].trim());

            double freePercent = (freeSwap / totalSwap) * 100;

            free = String.valueOf(Math.round(freePercent)) + " %";
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting used swap percent!");
        }

        return free;
    }

    private String getUsedSwapPercent(String linuxResponse)
    {
        String used = CommonConstantUI.STRING_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalSwap = Double.parseDouble(responseData[8].trim());

            double usedSwap = Double.parseDouble(responseData[9].trim());

            double usedPercent = (usedSwap / totalSwap) * 100;

            used = String.valueOf(Math.round(usedPercent)) + " %";
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting used swap percent!");
        }

        return used;
    }

    private String getOSVersion(String linuxResponse)
    {
        String os_Version = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            os_Version = responseData[6].trim();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting os version!");
        }

        return os_Version;
    }

    private String getCPUType(String linuxResponse)
    {
        String cpu_Type = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            cpu_Type = responseData[2].trim();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting cpu type!");
        }

        return cpu_Type;
    }

    private String getSystemName(String linuxResponse)
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

    private String getOSName(String linuxResponse)
    {
        String os_Name = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            os_Name = responseData[7].trim();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting os name!");
        }

        return os_Name;
    }

    private String getDeviceType(String linuxResponse)
    {
        StringBuffer device_Type;

        String deviceType = null;

        try
        {
            String[] responseData = linuxResponse.split(",");

            device_Type = new StringBuffer(responseData[0].trim());

            device_Type = device_Type.deleteCharAt(0);

            deviceType = device_Type.toString();
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting device type!");
        }

        return deviceType;
    }

    private String getSystemCPUPercent(String linuxResponse)
    {
        String cpu_System = null;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double cpu = Double.parseDouble(responseData[13].trim());

            cpu_System = String.valueOf(Math.round(cpu)) + " %";
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting system cpu!");
        }

        return cpu_System;
    }

    private String getUserCPUPercent(String linuxResponse)
    {
        String cpu_User = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double cpuUser = Double.parseDouble(responseData[12].trim());

            cpu_User = String.valueOf(Math.round(cpuUser)) + " %";
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting user cpu!");
        }

        return cpu_User;
    }

    private String getDiskPercent(String linuxResponse)
    {
        String disk = CommonConstantUI.NULL;

        try
        {
            String[] responseData = linuxResponse.split(",");

            disk = responseData[11].trim() + " %";
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting disk!");
        }

        return disk;
    }

    private String getRTTTime(String subString)
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

    private String getPacketLoss(String subString)
    {
        String receivedPacket;

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

    private String getSentPacket(String subString)
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

    private String getReceivedPacket(String subString)
    {
        String receivedPacket = CommonConstantUI.NULL;

        try
        {
            receivedPacket = subString.substring(subString.indexOf("transmitted") + 13, subString.indexOf("transmitted") + 14);
        }
        catch (Exception exception)
        {
            _logger.warn("packet not receive!");
        }

        return receivedPacket;
    }

    private void setRediscoverProperties(List<List<String>> dataList)
    {
        try
        {
            name = dataList.get(0).get(1);

            discoveryUsername = dataList.get(0).get(3);

            discoveryPassword = dataList.get(0).get(4);

            deviceType = dataList.get(0).get(5);

        }
        catch (Exception exception)
        {
            _logger.warn("not set your re discover data properties!");
        }
    }

    public String[] getDateTime()
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

    public String[] getPingData(String response)
    {
        String receivedPacket, sentPacket, rttTime, packetLoss;

        String pingResponse[] = new String[0];

        try
        {
            receivedPacket  = getReceivedPacket(response);

            sentPacket  = getSentPacket(response);

            rttTime  = getRTTTime(response);

            packetLoss  = getPacketLoss(response);

            pingResponse = new String[]{sentPacket, packetLoss, receivedPacket, rttTime};

        }
        catch (Exception exception)
        {
            _logger.warn("ping data manipulation is failed!");
        }

        return pingResponse;
    }

    public String[] getLinuxData(String response, String ipStatus)
    {
        String deviceName, systemName, cpuType, osVersion, osName, rtt_Time, diskPercent, systemCPUPercent, userCPUPercent;

        String usedMemoryPercent, freeMemoryPercent, usedSwapPercent, freeSwapPercent;

        String[] linuxResponse = new String[0];

        try
        {
            if (response != null && ipStatus.equals("Up"))
            {
                deviceName = getDeviceType(response);

                systemName = getSystemName(response);

                cpuType = getCPUType(response);

                osVersion = getOSVersion(response);

                osName = getOSName(response);

                usedMemoryPercent = getUsedMemoryPercent(response);

                freeMemoryPercent = getFreeMemoryPercent(response);

                rtt_Time = getRTTTime(response);

                userCPUPercent = getUserCPUPercent(response);

                systemCPUPercent = getSystemCPUPercent(response);

                usedSwapPercent = getUsedSwapPercent(response);

                freeSwapPercent = getFreeSwapPercent(response);

                diskPercent = getDiskPercent(response);

                linuxResponse = new String[]{deviceName, systemName, cpuType, osVersion, osName, usedMemoryPercent, freeMemoryPercent, rtt_Time, userCPUPercent, systemCPUPercent, usedSwapPercent, freeSwapPercent, diskPercent};

            }
            else
            {
                deviceName = systemName = cpuType = osVersion = osName = usedMemoryPercent = freeMemoryPercent = rtt_Time = userCPUPercent = systemCPUPercent = usedSwapPercent = freeSwapPercent = diskPercent = "";

                linuxResponse = new String[]{deviceName, systemName, cpuType, osVersion, osName, usedMemoryPercent, freeMemoryPercent, rtt_Time, userCPUPercent, systemCPUPercent, usedSwapPercent, freeSwapPercent, diskPercent};
            }
        }
        catch (Exception exception)
        {
            _logger.warn("linux data manipulation is failed!");
        }

        return linuxResponse;
    }

}
