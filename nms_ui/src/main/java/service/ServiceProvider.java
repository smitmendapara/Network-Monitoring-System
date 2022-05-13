package service;

import dao.DAO;

import util.CommonConstantUI;

import util.Logger;

import util.SSHConnectionUtil;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.sql.Time;
import java.sql.Timestamp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Logger _logger = new Logger();

    public boolean provisionMonitor(int id, String deviceType)
    {
        boolean status = true;

        DAO dao = new DAO();

        try
        {
            if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
            {
                try
                {
                    response = "";

                    ipStatus = "Unknown";

                    if (dao.enterMonitorTableData(id, response, ipStatus))
                    {
                        _logger.debug("successfully data inserted into tb_monitor table!");
                    }
                    else
                    {
                        _logger.warn("still not inserted into tb_discover and tb_result table!");
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
                try
                {
                    response = "";

                    ipStatus = "Unknown";

                    if (dao.enterMonitorTableData(id, response, ipStatus))
                    {
                        _logger.debug("successfully data inserted into tb_monitor table!");
                    }
                    else
                    {
                        _logger.warn("still not inserted into tb_discover and tb_result table!");
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

    public boolean executeDeviceDiscovery(int id, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        boolean status = true;

        String response, ipStatus = CommonConstantUI.DEVICE_UP;

        DAO dao = new DAO();

        try
        {
            if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
            {
                try
                {
                    response = pingDeviceResponse(ip);

                    ipStatus = checkPingIpStatus(response);

                    String timestamp = new Timestamp(System.currentTimeMillis()).toString();

                    if (dao.updateDiscoverData(ip, deviceType, timestamp, ipStatus))
                    {
                        _logger.info("successfully device discovered data inserted...");
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
                String uName;

                SSHConnectionUtil sshConnectionUtil = null;

                try
                {
                    ipStatus = checkPingIpStatus(pingDeviceResponse(ip));

                    if (ipStatus.equals(CommonConstantUI.DEVICE_UP))
                    {
                        sshConnectionUtil = SSHConnectionUtil.getSSHObject(ip, CommonConstantUI.SSH_PORT, discoveryUsername, discoveryPassword, CommonConstantUI.SSH_TIMEOUT);

                        if(sshConnectionUtil != null)
                        {
                            uName = sshConnectionUtil.executeCommand(CommonConstantUI.U_NAME_COMMAND, true);

                            if (uName.trim().equals(CommonConstantUI.LINUX_DEVICE))
                            {
                                ipStatus = CommonConstantUI.DEVICE_UP;
                            }
                            else
                            {
                                status = false;
                            }
                        }
                        else
                        {
                            _logger.warn("ssh object is null!");

                            ipStatus = CommonConstantUI.DEVICE_DOWN;
                        }
                    }
                    else
                    {
                        ipStatus = CommonConstantUI.DEVICE_DOWN;
                    }

                    String timestamp = new Timestamp(System.currentTimeMillis()).toString();

                    if (dao.updateDiscoverData(ip, deviceType, timestamp, ipStatus))
                    {
                        _logger.info("successfully device discovered data inserted...");
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

    public boolean pollingDevice(int id, String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        boolean status = true;

        double memory, disk, cpu;

        String packet, uName_M, uName_R, hostName, uName_Output, free_Output, df_Output, ioStat_Output, specificData, uName;

        String response, ipStatus;

        DAO _dao = new DAO();

        try
        {
            if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
            {
                Timestamp timestamp;

                try
                {
                    timestamp = new Timestamp(System.currentTimeMillis());

                    response = pingDeviceResponse(ip);

                    ipStatus = checkPingIpStatus(response);

                    if (_dao.enterReMonitorData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                    {
                        packet = getReceivedPacket(response);

                        memory = CommonConstantUI.DOUBLE_ZERO;

                        disk = CommonConstantUI.DOUBLE_ZERO;

                        cpu = CommonConstantUI.DOUBLE_ZERO;

                        if (_dao.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus, cpu, disk))
                        {
                            _logger.debug("successfully data re-inserted into tb_monitor, tb_discovery & tb_dataDump table!");
                        }
                        else
                        {
                            _logger.debug("successfully data re-inserted into tb_discovery table!");
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
                    ipStatus = checkPingIpStatus(pingDeviceResponse(ip));

                    if (ipStatus.equals(CommonConstantUI.DEVICE_UP))
                    {
                        sshConnectionUtil = SSHConnectionUtil.getSSHObject(ip, CommonConstantUI.SSH_PORT, discoveryUsername, discoveryPassword, CommonConstantUI.SSH_TIMEOUT);

                        if(sshConnectionUtil != null)
                        {
                            uName = sshConnectionUtil.executeCommand(CommonConstantUI.U_NAME_COMMAND, true);

                            uName_M = sshConnectionUtil.executeCommand(CommonConstantUI.NAME_M_COMMAND, true);

                            uName_R = sshConnectionUtil.executeCommand(CommonConstantUI.NAME_R_COMMAND, true);

                            hostName = sshConnectionUtil.executeCommand(CommonConstantUI.HOSTNAME_COMMAND, true);

                            uName_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_U_NAME_COMMAND, true);

                            free_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_FREE_COMMAND, true);

                            df_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_DISK_COMMAND, true);

                            ioStat_Output = sshConnectionUtil.executeCommand(CommonConstantUI.LINUX_CPU_COMMAND, true);

                            ipStatus = CommonConstantUI.DEVICE_UP;

                            specificData = getSpecificData(uName, uName_M, uName_R, hostName, uName_Output, free_Output, df_Output, ioStat_Output);

                            _logger.info("Command output : " + specificData);
                        }
                        else
                        {
                            _logger.warn("ssh object is null!");

                            ipStatus = CommonConstantUI.DEVICE_DOWN;

                            specificData = CommonConstantUI.STRING_NULL;
                        }
                    }
                    else
                    {
                        ipStatus = CommonConstantUI.DEVICE_DOWN;

                        specificData = CommonConstantUI.STRING_NULL;
                    }

                    timestamp = new Timestamp(System.currentTimeMillis());

                    if (_dao.enterReMonitorData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                    {
                        packet = CommonConstantUI.STRING_ZERO;

                        memory = CommonConstantUI.DOUBLE_ZERO;

                        disk = CommonConstantUI.DOUBLE_ZERO;

                        cpu = CommonConstantUI.DOUBLE_ZERO;

                        if (specificData != null && !specificData.equals(CommonConstantUI.STRING_NULL))
                        {
                            memory = getMemoryPercent(specificData);

                            disk = getUsedDiskPercent(specificData);

                            cpu = getCPUPercent(specificData);
                        }

                        if (_dao.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus, cpu, disk))
                        {
                            _logger.debug("successfully data re-inserted into tb_monitor, tb_discover & tb_dataDump table!");
                        }
                        else
                        {
                            _logger.debug("successfully data re-inserted into tb_discovery table!");
                        }
                    }
                    else
                    {
                        _logger.warn("still not inserted data into tb_monitor table!");
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

    private String pingDeviceResponse(String ip)
    {
        String pingResult = "";

        String pingCmd = CommonConstantUI.PING_COMMAND + ip;

        Runtime runtime;

        Process process;

        BufferedReader bufferedInput = null;

        try
        {
            runtime = Runtime.getRuntime();

            process = runtime.exec(pingCmd);

            bufferedInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String inputLine;

            while ((inputLine = bufferedInput.readLine()) != null)
            {
                pingResult = pingResult.concat(inputLine);
            }

            bufferedInput.close();
        }
        catch (Exception exception)
        {
            _logger.error("ping device response null...", exception);
        }
        finally
        {
            if (bufferedInput != null)
            {
                try
                {
                    bufferedInput.close();
                }
                catch (Exception exception)
                {
                    _logger.error("buffer input not closed...", exception);
                }
            }
        }

        return pingResult.substring(pingResult.indexOf("-"));
    }

    public boolean addDevice(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        boolean status = true;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try
        {
            if (checkIp(ip, deviceType))
            {
                insertData(name, ip, discoveryUsername, discoveryPassword, deviceType, timestamp);
            }

            else
            {
                return false;
            }
        }
        catch (Exception exception)
        {
            status = false;
        }

        return status;
    }

    private boolean checkIp(String ip, String deviceType)
    {
        boolean status = true;

        DAO dao = new DAO();

        try
        {
            if (deviceType.equals(CommonConstantUI.STRING_ZERO))
            {
                deviceType = CommonConstantUI.PING_DEVICE;
            }
            if (deviceType.equals(CommonConstantUI.STRING_ONE))
            {
                deviceType = CommonConstantUI.LINUX_DEVICE;
            }

            status = dao.getDiscoverTB(ip, deviceType);
        }
        catch (Exception exception)
        {
            _logger.warn("something went wrong on checking existing ip!");
        }

        return status;
    }

    private void insertData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, Timestamp timestamp)
    {
        DAO dao = new DAO();

        try
        {
            if (dao.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, timestamp.toString()))
            {
               _logger.debug("successfully data inserted into tb_discovery table!");
            }
            else
            {
                _logger.warn("still not inserted into tb_discover table!");
            }
        }
        catch (Exception exception)
        {
            _logger.warn("data not inserted properly!");
        }
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

            double freeMemory = ( Double.parseDouble(responseData[5].trim()) / totalMemory ) * 100;

            free = String.valueOf(Math.round(freeMemory));
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

            double usedMemory = ( Double.parseDouble(responseData[4].trim()) / totalMemory ) * 100;

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

    public String getPreviousTime()
    {
        String previousTime = null;

        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Date currentDate = new Date();

            Calendar currentTime = Calendar.getInstance();

            currentTime.setTime(currentDate);

            currentTime.add(Calendar.MINUTE, -30);

            Date secondTime = currentTime.getTime();

            previousTime= dateFormat.format(secondTime);

        }
        catch (Exception exception)
        {

        }

        return previousTime;
    }

    public List<String> getCurrentTime()
    {
        List<String> timeRange = new ArrayList<>();

        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Date date = new Date();

            Calendar currentTime = Calendar.getInstance();

            currentTime.setTime(date);

            String firstTime = dateFormat.format(date);

            currentTime.add(Calendar.HOUR, -24);

            Date date2 = currentTime.getTime();

            String secondTime = dateFormat.format(date2);

            timeRange.add(firstTime);

            timeRange.add(secondTime);
        }
        catch (Exception exception)
        {
            _logger.warn("some thing went wrong on time range function!");
        }

        return timeRange;
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

                freeMemoryPercent = getFreeMemoryPercent(response) + " %";

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

    public List<List<String>> getFormData(List<HashMap<String, Object>> commonDataList)
    {
        List<String> list;

        List<List<String>> commonList = new ArrayList<>();

        try
        {
            for (HashMap<String, Object> map : commonDataList)
            {
                list = new ArrayList<>();

                list.add(map.get("Id").toString());

                list.add(map.get("Name").toString());

                list.add(map.get("IP").toString());

                if (map.get("Username") != null)
                {
                    list.add(map.get("Username").toString());
                }
                else
                {
                    list.add(map.get("Profile").toString());
                }

                if (map.get("DeviceType") != null)
                {
                    list.add(map.get("DeviceType").toString());
                }
                else
                {
                    list.add(map.get("Device").toString());
                }

                if (map.get("Status") != null)
                {
                    list.add(map.get("Status").toString());
                }

                commonList.add(list);
            }
        }
        catch (Exception exception)
        {

        }

        return commonList;
    }

    public List<Integer> getDeviceDetailsList(List<HashMap<String, Object>> upDeviceList, List<HashMap<String, Object>> downDeviceList)
    {
        List<Integer> deviceList = new ArrayList<>();

        try
        {
            for (HashMap<String, Object> upDevice : upDeviceList)
            {
                deviceList.add(Integer.parseInt(upDevice.get("COUNT(IP)").toString()));
            }
            for (HashMap<String, Object> downDevice : downDeviceList)
            {
                deviceList.add(Integer.parseInt(downDevice.get("COUNT(IP)").toString()));
            }
        }
        catch (Exception exception)
        {

        }

        return deviceList;
    }

    public String[] getTopMemoryCPUDiskDevice(List<HashMap<String, Object>> topDeviceList)
    {
        String[] topDevice = new String[topDeviceList.size() * 2];

        try
        {
            int iterator = 0;

            for (int i = 0; i < topDevice.length; i = i + 2)
            {
                topDevice[i] = topDeviceList.get(iterator).get("MAXPERCENT").toString();

                topDevice[i + 1] = topDeviceList.get(iterator).get("IP").toString();

                iterator++;
            }
        }
        catch (Exception exception)
        {

        }

        return topDevice;
    }

    public boolean validIP(String ipAddress)
    {
        boolean result = false;

        try
        {
            if (ipAddress == null)
            {
                return false;
            }

            String zeroTo255 = "(\\d{1,2}|(0|1)\\"
                    + "d{2}|2[0-4]\\d|25[0-5])";

            String regex = zeroTo255 + "\\." +
                            zeroTo255 + "\\."
                            + zeroTo255 + "\\."
                    + zeroTo255;

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(ipAddress);

            result = matcher.matches();
        }
        catch (Exception exception)
        {
            _logger.error("Ip not valid!", exception);
        }

        return result;
    }

    private Double getMemoryPercent(String linuxResponse)
    {
        double free = CommonConstantUI.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalMemory = Double.parseDouble(responseData[3].trim());

            double freeMemory = ( Double.parseDouble(responseData[5].trim()) / totalMemory ) * 100;

            free = Double.parseDouble(new DecimalFormat("##.##").format(freeMemory));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting free memory percent!");
        }

        return free;
    }

    private Double getUsedDiskPercent(String linuxResponse)
    {
        double disk = CommonConstantUI.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            disk = Double.parseDouble(responseData[11].trim());

            disk = Double.parseDouble(new DecimalFormat("##.##").format(disk));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting free memory percent!");
        }

        return disk;
    }

    private Double getCPUPercent(String linuxResponse)
    {
        double cpu = CommonConstantUI.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            cpu = Double.parseDouble(responseData[15].trim());

            cpu = Double.parseDouble(new DecimalFormat("##.##").format(cpu));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting free memory percent!");
        }

        return cpu;
    }

    private String getSpecificData(String uName, String uName_M, String uName_R, String hostName, String uName_output, String free_output, String df_Output, String ioStat_Output)
    {
        String data = null;

        try
        {
            String linux = uName.trim();

            String ubuntu = hostName.trim();

            String x86 = uName_M.trim();

            String[] freeSplit = free_output.split("\n");

            String totalMemory = freeSplit[1].substring(4, 19).trim();

            String usedMemory = freeSplit[1].substring(19, 31).trim();

            String freeMemory = freeSplit[1].substring(31, 43).trim();

            String osVersion = uName_R.trim();

            String osName = uName_output.substring(uName_output.indexOf("GNU"), uName_output.length()).trim();

            String totalSwap = freeSplit[2].substring(5, 19).trim();

            String usedSwap = freeSplit[2].substring(19, 31).trim();

            String freeSwap = freeSplit[2].substring(31, 43).trim();

            String[] dfSplit = df_Output.split("\n");

            String disk = dfSplit[dfSplit.length-1].substring(dfSplit[dfSplit.length-1].length() - 6, dfSplit[dfSplit.length-1].length()-3).trim();

            String[] ioSplit = ioStat_Output.split("\n");

            String CPU_System = ioSplit[3].substring(23, 31).trim();

            String CPU_User = ioSplit[3].substring(0, 15).trim();

            String sharedMemory = freeSplit[1].substring(43, 55).trim();

            String cacheMemory = freeSplit[1].substring(55, 67).trim();

            String CPU_Idle = ioSplit[3].substring(48, ioSplit[3].length()).trim();

            String []stringArray = {linux, ubuntu, x86, totalMemory, usedMemory, freeMemory, osVersion, osName, totalSwap, usedSwap, freeSwap, disk, CPU_User, CPU_System, sharedMemory, CPU_Idle, cacheMemory};

            data = Arrays.toString(stringArray);

        }
        catch (Exception exception)
        {
            _logger.warn("not get specific data from linux output!");
        }

        return data;
    }
}
