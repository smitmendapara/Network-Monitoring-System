package action.dashboard;

import com.opensymphony.xwork2.ModelDriven;
import service.CommonServiceProvider;

import bean.DashboardBean;

import dao.DAO;

import util.CommonConstantUI;

import util.Logger;

import java.util.HashMap;
import java.util.List;

public class Dashboard implements ModelDriven<DashboardBean>
{
    private DashboardBean dashboardBean = new DashboardBean();

    private static final Logger _logger = new Logger();

    // get particular device dashboard data
    public String getDeviceDashboardData()
    {
        DAO dao = new DAO();

        CommonServiceProvider commonServiceProvider = new CommonServiceProvider();

        try
        {
            List<HashMap<String, Object>> dashboardDataList = dao.getDashboardData(dashboardBean.getIp(), dashboardBean.getDeviceType());

            for (HashMap<String, Object> dashboardData : dashboardDataList)
            {
                dashboardBean.setId(dashboardBean.getId());

                dashboardBean.setName(dashboardData.get("Name").toString());

                dashboardBean.setIp(dashboardData.get("IP").toString());

                if (dashboardData.get("Profile").equals(""))
                {
                    dashboardBean.setDiscoveryUsername(CommonConstantUI.STRING_NULL);
                }
                else
                {
                    dashboardBean.setDiscoveryPassword(dashboardData.get("Profile").toString());
                }

                dashboardBean.setDeviceType(dashboardData.get("DeviceType").toString());

                if (dashboardData.get("DeviceType").equals(CommonConstantUI.LINUX_DEVICE) && dashboardData.get("Status").equals(CommonConstantUI.DEVICE_DOWN))
                {
                    String linuxResponse[] = commonServiceProvider.getLinuxData(dashboardData.get("Response").toString(), dashboardData.get("Status").toString());

                    dashboardBean.setResponse(linuxResponse);
                }
                else if (dashboardData.get("DeviceType").equals(CommonConstantUI.LINUX_DEVICE) && dashboardData.get("Status").equals(CommonConstantUI.DEVICE_UP))
                {
                    String linuxResponse[] = commonServiceProvider.getLinuxData(dashboardData.get("Response").toString(), dashboardData.get("Status").toString());

                    dashboardBean.setResponse(linuxResponse);
                }
                else
                {
                    String pingResponse[] = commonServiceProvider.getPingData(dashboardData.get("Response").toString());

                    dashboardBean.setResponse(pingResponse);
                }

                dashboardBean.setStatus(dashboardData.get("Status").toString());

                List<Integer> statusPercent = dao.getStatusPercent(dashboardData.get("IP").toString(), dashboardData.get("DeviceType").toString());

                int statusArray[] = new int[statusPercent.size()];

                for (int index = 0; index < statusPercent.size(); index++)
                {
                    statusArray[index] = statusPercent.get(index);
                }

                dashboardBean.setPercent(statusArray);

                String dateTime[] = commonServiceProvider.getDateTime();

                dashboardBean.setCurrentTime(dateTime);

                float dataPoints[] = new float[dateTime.length];

                if (dashboardData.get("DeviceType").equals(CommonConstantUI.PING_DEVICE))
                {
                    for (int index = 0; index < dateTime.length; index++)
                    {
                        dataPoints[index] = dao.getUpdatedPacket(dashboardBean.getId(), dashboardData.get("IP").toString(), dateTime[index], dashboardData.get("DeviceType").toString());
                    }

                    dashboardBean.setDataPoints(dataPoints);

                    dashboardBean.setTitle("Received Packet");
                }
                else
                {
                    for (int index = 0; index < dateTime.length; index++)
                    {
                        dataPoints[index] = dao.getUpdatedMemory(dashboardBean.getId(), dashboardData.get("IP").toString(), dateTime[index], dashboardData.get("DeviceType").toString());
                    }

                    dashboardBean.setDataPoints(dataPoints);

                    dashboardBean.setTitle("Free Memory (%)");
                }
            }
        }
        catch (Exception exception)
        {
            _logger.warn("particular device data not fetched.");
        }

        return CommonConstantUI.SUCCESS;
    }

    // get home dashboard matrix data
    public String getHomeDashboardData()
    {
        DAO dao = new DAO();

        try
        {
            List<Integer> statusList = dao.getDeviceStatusList();

            if (statusList.size() > 0)
            {
                dashboardBean.setTotalDevice(statusList.get(0) + statusList.get(1) + statusList.get(2));

                dashboardBean.setUpDevice(statusList.get(0));

                dashboardBean.setDownDevice(statusList.get(1));

                dashboardBean.setUnknownDevice(statusList.get(2));
            }

            List<Integer> deviceTypeList = dao.getDeviceTypeList();

            if (deviceTypeList.size() > 0)
            {
                dashboardBean.setPingDevice(deviceTypeList.get(0));

                dashboardBean.setLinuxDevice(deviceTypeList.get(1));
            }

            String []topMemoryDeviceDetails = dao.getTopMemoryDeviceDetails();

            String []topCPUDeviceDetails = dao.getTopCPUDeviceDetails();

            String []topDiskDeviceDetails = dao.getTopDiskDeviceDetails();

            dashboardBean.setTopMemory(topMemoryDeviceDetails);

            dashboardBean.setTopCPU(topCPUDeviceDetails);

            dashboardBean.setTopDisk(topDiskDeviceDetails);
        }
        catch (Exception exception)
        {
            _logger.error("home dashboard data not fetched.", exception);
        }

        return CommonConstantUI.SUCCESS;
    }

    @Override
    public DashboardBean getModel()
    {
        return dashboardBean;
    }
}
