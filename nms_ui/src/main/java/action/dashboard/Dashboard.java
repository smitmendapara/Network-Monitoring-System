package action.dashboard;

import service.ServiceProvider;

import monitorBean.DashboardBean;

import dao.DAO;

import util.CommonConstantUI;

import util.Logger;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class Dashboard
{
    private int id;

    private String ip;

    private String deviceType;

    private String[] response;

    private int[] percent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String[] getResponse() {
        return response;
    }

    public void setResponse(String[] response) {
        this.response = response;
    }

    public int[] getPercent() {
        return percent;
    }

    public void setPercent(int[] percent) {
        this.percent = percent;
    }

    private DashboardBean bean = null;

    private List<DashboardBean> beanList = null;

    public List<DashboardBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DashboardBean> beanList) {
        this.beanList = beanList;
    }

    private static final Logger _logger = new Logger();

    public String getDashboardData()
    {
        DAO dao = new DAO();

        ServiceProvider serviceProvider = new ServiceProvider();

        try
        {
            beanList = new ArrayList<>();

            List<HashMap<String, Object>> dashboardDataList = dao.getDashboardData(ip, deviceType);

            for (HashMap<String, Object> dashboardData : dashboardDataList)
            {
                bean = new DashboardBean();

                bean.setId(id);

                bean.setName(dashboardData.get("Name").toString());

                bean.setIP(dashboardData.get("IP").toString());

                if (dashboardData.get("Profile") == null)
                {
                    String profile = CommonConstantUI.STRING_NULL;

                    bean.setUsername(profile);
                }
                else
                {
                    bean.setUsername(dashboardData.get("Profile").toString());
                }

                bean.setDevice(dashboardData.get("DeviceType").toString());

                if (dashboardData.get("DeviceType").equals(CommonConstantUI.LINUX_DEVICE) && dashboardData.get("Status").equals(CommonConstantUI.DEVICE_DOWN))
                {
                    String linuxResponse[] = serviceProvider.getLinuxData(dashboardData.get("Response").toString(), dashboardData.get("Status").toString());

                    bean.setResponse(linuxResponse);
                }
                else if (dashboardData.get("DeviceType").equals(CommonConstantUI.LINUX_DEVICE) && dashboardData.get("Status").equals(CommonConstantUI.DEVICE_UP))
                {
                    String linuxResponse[] = serviceProvider.getLinuxData(dashboardData.get("Response").toString(), dashboardData.get("Status").toString());

                    bean.setResponse(linuxResponse);
                }
                else
                {
                    String pingResponse[] = serviceProvider.getPingData(dashboardData.get("Response").toString());

                    bean.setResponse(pingResponse);
                }

                bean.setStatus(dashboardData.get("Status").toString());

                List<Integer> statusPercent = dao.getStatusPercent(dashboardData.get("IP").toString(), dashboardData.get("DeviceType").toString());

                int statusArray[] = new int[statusPercent.size()];

                for (int index = 0; index < statusPercent.size(); index++)
                {
                    statusArray[index] = statusPercent.get(index);
                }

                bean.setPercent(statusArray);

                String dateTime[] = serviceProvider.getDateTime();

                bean.setCurrentTime(dateTime);

                float dataPoints[] = new float[dateTime.length];

                if (dashboardData.get("DeviceType").equals(CommonConstantUI.PING_DEVICE))
                {
                    for (int index = 0; index < dateTime.length; index++)
                    {
                        dataPoints[index] = dao.getUpdatedPacket(id, dashboardData.get("IP").toString(), dateTime[index], dashboardData.get("DeviceType").toString());
                    }

                    bean.setDataPoints(dataPoints);

                    bean.setTitle("Received Packet");
                }
                else
                {
                    for (int index = 0; index < dateTime.length; index++)
                    {
                        dataPoints[index] = dao.getUpdatedMemory(id, dashboardData.get("IP").toString(), dateTime[index], dashboardData.get("DeviceType").toString());
                    }

                    bean.setDataPoints(dataPoints);

                    bean.setTitle("Free Memory (%)");
                }

                beanList.add(bean);

            }
        }
        catch (Exception exception)
        {
            _logger.warn("dashboard data not found!");
        }

        return "success";
    }

    public String getHomeDashboardData()
    {
        DAO dao = new DAO();

        try
        {
            List<Integer> statusList = dao.getDeviceStatusList();

            beanList = new ArrayList<>();

            bean = new DashboardBean();

            if (statusList.size() > 0)
            {
                bean.setTotalDevice(statusList.get(0) + statusList.get(1));

                bean.setUpDevice(statusList.get(0));

                bean.setDownDevice(statusList.get(1));
            }

            List<Integer> deviceTypeList = dao.getDeviceTypeList();

            if (deviceTypeList.size() > 0)
            {
                bean.setPingDevice(deviceTypeList.get(0));

                bean.setLinuxDevice(deviceTypeList.get(1));
            }

            String []topMemoryDeviceDetails = dao.getTopMemoryDeviceDetails();

            String []topCPUDeviceDetails = dao.getTopCPUDeviceDetails();

            String []topDiskDeviceDetails = dao.getTopDiskDeviceDetails();

            bean.setTopMemory(topMemoryDeviceDetails);

            bean.setTopCPU(topCPUDeviceDetails);

            bean.setTopDisk(topDiskDeviceDetails);

            beanList.add(bean);
        }
        catch (Exception exception)
        {

        }

        return "success";
    }
}
