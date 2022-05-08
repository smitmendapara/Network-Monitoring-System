package action.dashboard;

import action.helper.ServiceProvider;

import bean.DashboardBean;

import dao.DAO;

import util.CommonConstantUI;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class Dashboard extends ActionSupport
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

    private List<DashboardBean> beanList = null;

    public List<DashboardBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DashboardBean> beanList) {
        this.beanList = beanList;
    }

    private static final Logger _logger = new Logger();

    private ServiceProvider serviceProvider = new ServiceProvider();

    public String getDashboardData()
    {
        DashboardBean bean;

        DAO dao = new DAO();

        try
        {
            beanList = new ArrayList<>();

            List<HashMap<String, Object>> dashboardDataList = dao.getDashboardData(ip, deviceType);

            for (HashMap<String, Object> dashboardData : dashboardDataList)
            {
                bean = new DashboardBean();

                bean.setId(id);

                bean.setName((String) dashboardData.get("Name"));

                bean.setIP((String) dashboardData.get("IP"));

                if (dashboardData.get("Profile") == null)
                {
                    String profile = CommonConstantUI.STRING_NULL;

                    bean.setUsername(profile);
                }
                else
                {
                    bean.setUsername((String) dashboardData.get("Profile"));
                }

                bean.setDevice((String) dashboardData.get("DeviceType"));

                if (dashboardData.get("DeviceType").equals(CommonConstantUI.LINUX_DEVICE) && dashboardData.get("Status").equals(CommonConstantUI.DEVICE_DOWN))
                {
                    String linuxResponse[] = serviceProvider.getLinuxData((String) dashboardData.get("Response"), (String) dashboardData.get("Status"));

                    bean.setResponse(linuxResponse);
                }
                else if (dashboardData.get("DeviceType").equals(CommonConstantUI.LINUX_DEVICE) && dashboardData.get("Status").equals(CommonConstantUI.DEVICE_UP))
                {
                    String linuxResponse[] = serviceProvider.getLinuxData((String) dashboardData.get("Response"), (String) dashboardData.get("Status"));

                    bean.setResponse(linuxResponse);
                }
                else
                {
                    String pingResponse[] = serviceProvider.getPingData((String) dashboardData.get("Response"));

                    bean.setResponse(pingResponse);
                }

                bean.setStatus((String) dashboardData.get("Status"));

                List<Integer> statusPercent = dao.getStatusPercent((String) dashboardData.get("IP"), (String) dashboardData.get("DeviceType"));

                int statusArray[] = new int[statusPercent.size()];

                for (int index = 0; index < statusPercent.size(); index++)
                {
                    statusArray[index] = statusPercent.get(index);
                }

                bean.setPercent(statusArray);

                String dateTime[] = serviceProvider.getDateTime();

                bean.setCurrentTime(dateTime);

                String receivedPacket[] = new String[dateTime.length];

                String memoryStorage[] = new String[dateTime.length];

                if (dashboardData.get("DeviceType").equals(CommonConstantUI.PING_DEVICE))
                {
                    for (int index = 0; index < dateTime.length; index++)
                    {
                        receivedPacket[index] = dao.getUpdatedPacket(id, (String) dashboardData.get("IP"), dateTime[index], (String) dashboardData.get("DeviceType"));
                    }

                    bean.setPacket(receivedPacket);
                }
                else
                {
                    for (int index = 0; index < dateTime.length; index++)
                    {
                        memoryStorage[index] = String.valueOf(dao.getUpdatedMemory(id, (String) dashboardData.get("IP"), dateTime[index], (String) dashboardData.get("DeviceType")));
                    }

                    bean.setMemory(memoryStorage);
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
}
