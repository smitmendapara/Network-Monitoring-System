package action.dashboard;

import action.helper.ServiceProvider;

import bean.DashboardBean;

import dao.UserDAO;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;

import java.util.List;

public class Dashboard extends ActionSupport
{
    private int id;

    private String ip;

    private String deviceType;

    private String[] response;

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

    private List<DashboardBean> beanList = null;

    public List<DashboardBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DashboardBean> beanList) {
        this.beanList = beanList;
    }

    private static final UserDAO _dao = new UserDAO();

    private static final Logger _logger = new Logger();

    private ServiceProvider serviceProvider = new ServiceProvider();

    public String executeDashboard()
    {
        _dao.setNewId(id);

        _dao.setIp(ip);

        _dao.setDeviceType(deviceType);

        try
        {
            if (deviceType.equals("Ping"))
            {
                return "success";
            }
            else
            {
                return "error";
            }
        }
        catch (Exception exception)
        {
            _logger.warn("dashboard execution is failed!");
        }

        return null;
    }

    public String getDashboardData()
    {
        DashboardBean bean;

        List<List<String>> dashboardList;

        try
        {
            beanList = new ArrayList<>();

            dashboardList = _dao.getDashboardData();

            if (!dashboardList.isEmpty())
            {
                for (List<String> subList : dashboardList)
                {
                    bean = new DashboardBean();

                    bean.setId(Integer.parseInt(subList.get(0)));

                    bean.setName(subList.get(1));

                    bean.setIP(subList.get(2));

                    if (subList.get(3) == null)
                    {
                        String profile = "null";

                        bean.setUsername(profile);
                    }
                    else
                    {
                        bean.setUsername(subList.get(3));
                    }

                    bean.setDevice(subList.get(5));

                    if (subList.get(5).equals("Linux") && subList.get(7).equals("Down"))
                    {
                        String linuxResponse[] = serviceProvider.getLinuxData(subList.get(6), subList.get(7));

                        bean.setResponse(linuxResponse);
                    }
                    else if (subList.get(5).equals("Linux") && subList.get(7).equals("Up"))
                    {
                        String linuxResponse[] = serviceProvider.getLinuxData(subList.get(6), subList.get(7));

                        bean.setResponse(linuxResponse);
                    }
                    else
                    {
                        String pingResponse[] = serviceProvider.getPingData(subList.get(6));

                        bean.setResponse(pingResponse);
                    }

                    bean.setStatus(subList.get(7));

                    String dateTime[] = serviceProvider.getDateTime();

                    bean.setCurrentTime(dateTime);

                    String receivedPacket[] = new String[dateTime.length];

                    String memoryStorage[] = new String[dateTime.length];

                    if (subList.get(5).equals("Ping"))
                    {
                        for (int i = 0; i < dateTime.length; i++)
                        {
                            receivedPacket[i] = _dao.getUpdatedPacket(Integer.parseInt(subList.get(0)), subList.get(2), dateTime[i], subList.get(5));
                        }

                        bean.setPacket(receivedPacket);
                    }
                    else
                    {
                        for (int i = 0; i < dateTime.length; i++)
                        {
                            memoryStorage[i] = String.valueOf(_dao.getUpdatedMemory(Integer.parseInt(subList.get(0)), subList.get(2), dateTime[i], subList.get(5)));
                        }

                        bean.setMemory(memoryStorage);
                    }

                    beanList.add(bean);
                }
            }
        }
        catch (Exception exception)
        {
            _logger.warn("dashboard data not found!");
        }

        return "success";
    }
}
