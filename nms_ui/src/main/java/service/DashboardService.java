package service;

import bean.DashboardBean;
import dao.ConnectionDAO;
import util.CommonConstant;
import util.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DashboardService
{
    private static final Logger _logger = new Logger();

    public void evaluateDeviceDashboardData(DashboardBean dashboardBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            List<HashMap<String, Object>> dashboardDataList = connectionDao.getDashboardData(dashboardBean.getIp(), dashboardBean.getDeviceType());

            for (HashMap<String, Object> dashboardData : dashboardDataList)
            {
                dashboardBean.setId(dashboardBean.getId());

                dashboardBean.setName(dashboardData.get("Name").toString());

                dashboardBean.setIp(dashboardData.get("IP").toString());

                if (dashboardData.get("Profile").equals(CommonConstant.STRING_NULL))
                {
                    dashboardBean.setDiscoveryUsername(CommonConstant.STRING_NULL);
                }
                else
                {
                    dashboardBean.setDiscoveryPassword(dashboardData.get("Profile").toString());
                }

                dashboardBean.setDeviceType(dashboardData.get("DeviceType").toString());

                if (dashboardData.get("DeviceType").equals(CommonConstant.LINUX_DEVICE) && dashboardData.get("Status").equals(CommonConstant.DEVICE_DOWN))
                {
                    String linuxResponse[] = getLinuxData(dashboardData.get("Response").toString(), dashboardData.get("Status").toString());

                    dashboardBean.setResponse(linuxResponse);
                }
                else if (dashboardData.get("DeviceType").equals(CommonConstant.LINUX_DEVICE) && dashboardData.get("Status").equals(CommonConstant.DEVICE_UP))
                {
                    String linuxResponse[] = getLinuxData(dashboardData.get("Response").toString(), dashboardData.get("Status").toString());

                    dashboardBean.setResponse(linuxResponse);
                }
                else
                {
                    String pingResponse[] = getPingData(dashboardData.get("Response").toString());

                    dashboardBean.setResponse(pingResponse);
                }

                dashboardBean.setStatus(dashboardData.get("Status").toString());

                List<Integer> statusPercent = connectionDao.getStatusPercent(dashboardData.get("IP").toString(), dashboardData.get("DeviceType").toString());

                int statusArray[] = new int[statusPercent.size()];

                for (int index = 0; index < statusPercent.size(); index++)
                {
                    statusArray[index] = statusPercent.get(index);
                }

                dashboardBean.setPercent(statusArray);

                String dateTime[] = getDateTime();

                dashboardBean.setCurrentTime(dateTime);

                float dataPoints[] = new float[dateTime.length];

                if (dashboardData.get("DeviceType").equals(CommonConstant.PING_DEVICE))
                {
                    for (int index = 0; index < dateTime.length; index++)
                    {
                        dataPoints[index] = connectionDao.getUpdatedPacket(dashboardBean.getId(), dashboardData.get("IP").toString(), dateTime[index], dashboardData.get("DeviceType").toString());
                    }

                    dashboardBean.setDataPoints(dataPoints);

                    dashboardBean.setTitle("Received Packet");
                }
                else
                {
                    for (int index = 0; index < dateTime.length; index++)
                    {
                        dataPoints[index] = connectionDao.getUpdatedMemory(dashboardBean.getId(), dashboardData.get("IP").toString(), dateTime[index], dashboardData.get("DeviceType").toString());
                    }

                    dashboardBean.setDataPoints(dataPoints);

                    dashboardBean.setTitle("Free Memory (%)");
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched device dashboard dat. ", exception);
        }
    }

    public void evaluateHomeDashboardData(DashboardBean dashboardBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            List<Integer> statusList = connectionDao.getDeviceStatusList();

            if (statusList.size() > 0)
            {
                dashboardBean.setTotalDevice(statusList.get(0) + statusList.get(1) + statusList.get(2));

                dashboardBean.setUpDevice(statusList.get(0));

                dashboardBean.setDownDevice(statusList.get(1));

                dashboardBean.setUnknownDevice(statusList.get(2));
            }

            List<Integer> deviceTypeList = connectionDao.getDeviceTypeList();

            if (deviceTypeList.size() > 0)
            {
                dashboardBean.setPingDevice(deviceTypeList.get(0));

                dashboardBean.setLinuxDevice(deviceTypeList.get(1));
            }

            String []topMemoryDeviceDetails = connectionDao.getTopMemoryDeviceDetails();

            String []topCPUDeviceDetails = connectionDao.getTopCPUDeviceDetails();

            String []topDiskDeviceDetails = connectionDao.getTopDiskDeviceDetails();

            dashboardBean.setTopMemory(topMemoryDeviceDetails);

            dashboardBean.setTopCPU(topCPUDeviceDetails);

            dashboardBean.setTopDisk(topDiskDeviceDetails);
        }
        catch (Exception exception)
        {
            _logger.error("home dashboard data not fetched.", exception);
        }
    }

    private String[] getDateTime()
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

    private String getPacketLoss(String subString)
    {
        String receivedPacket;

        String packetLoss = CommonConstant.NULL;

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

    private String getSentPacket(String subString)
    {
        String sentPacket = CommonConstant.NULL;

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
        String receivedPacket = CommonConstant.NULL;

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

    private String[] getPingData(String response)
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

    private String[] getLinuxData(String response, String ipStatus)
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

    private String getRTTTime(String subString)
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

    private String getFreeMemoryPercent(String linuxResponse)
    {
        String free = CommonConstant.STRING_ZERO;

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
        String used = CommonConstant.STRING_ZERO;

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
        String free = CommonConstant.STRING_ZERO;

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
        String used = CommonConstant.STRING_ZERO;

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
        String os_Version = CommonConstant.NULL;

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
        String cpu_Type = CommonConstant.NULL;

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
        String systemName = CommonConstant.NULL;

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
        String os_Name = CommonConstant.NULL;

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
        String cpu_User = CommonConstant.NULL;

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
        String disk = CommonConstant.NULL;

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
}
