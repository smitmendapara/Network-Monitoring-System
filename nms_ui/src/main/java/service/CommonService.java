package service;

import util.Logger;

import java.text.SimpleDateFormat;

import java.util.*;

public class CommonService
{
    private static final Logger _logger = new Logger();

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
            _logger.error("didn't get the previous time.", exception);
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

                list.add(map.get("Password").toString());

                commonList.add(list);
            }
        }
        catch (Exception exception)
        {

        }

        return commonList;
    }

    public List<Integer> getDeviceDetailsList(List<HashMap<String, Object>> upDeviceList, List<HashMap<String, Object>> downDeviceList, List<HashMap<String, Object>> unknownDeviceList)
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
            if (unknownDeviceList != null)
            {
                for (HashMap<String, Object> unknownDevice : unknownDeviceList)
                {
                    deviceList.add(Integer.parseInt(unknownDevice.get("COUNT(IP)").toString()));
                }
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

            for (int index = 0; index < topDevice.length; index = index + 2)
            {
                topDevice[index] = topDeviceList.get(iterator).get("MAXPERCENT").toString();

                topDevice[index + 1] = topDeviceList.get(iterator).get("IP").toString();

                iterator++;
            }
        }
        catch (Exception exception)
        {

        }

        return topDevice;
    }
}
