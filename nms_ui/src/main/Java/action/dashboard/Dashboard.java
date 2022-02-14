package action.dashboard;

import action.helper.ServiceProvider;

import bean.DashboardBean;

import dao.UserDAO;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.ResultSet;

import java.util.ArrayList;

import java.util.List;

public class Dashboard extends ActionSupport
{
    private int id;

    private String ip;

    private String deviceType;

    private String response;

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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private static final Logger _logger = new Logger();

    ResultSet resultSet = null;

    List<List<String>> dashboardList = new ArrayList<>();

    DashboardBean bean = null;

    List<DashboardBean> beanList = null;

    public List<DashboardBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DashboardBean> beanList) {
        this.beanList = beanList;
    }

    UserDAO _dao = new UserDAO();

    public String showDashboardData()
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

        }
        return null;
    }

    public String execute()
    {
        int id = _dao.getNewId();

        String ip = _dao.getIp();

        String deviceType = _dao.getDeviceType();

        String profile;

        UserDAO _dao = new UserDAO(id, ip, deviceType);

        try
        {
            beanList = new ArrayList<DashboardBean>();

            dashboardList = UserDAO.getDashboardData();

            if (!dashboardList.isEmpty())
            {
                for (int k = 0; k < dashboardList.size(); k++)
                {
                    bean = new DashboardBean();

                    bean.setId(Integer.parseInt(dashboardList.get(k).get(0)));

                    bean.setName(dashboardList.get(k).get(1));

                    bean.setIP(dashboardList.get(k).get(2));

                    if (dashboardList.get(k).get(3) == null)
                    {
                        profile = "null";

                        bean.setUsername(profile);
                    }
                    else
                    {
                        bean.setUsername(dashboardList.get(k).get(3));
                    }

                    bean.setDevice(dashboardList.get(k).get(5));

                    if (dashboardList.get(k).get(5).equals("Linux") && dashboardList.get(k).get(7).equals("Down"))
                    {
                        bean.setResponse("");
                    }
                    else
                    {
                        bean.setResponse(dashboardList.get(k).get(6));
                    }

                    bean.setStatus(dashboardList.get(k).get(7));

                    String dateTime[] = ServiceProvider.getDateTime();

                    String receivedPacket[] = new String[dateTime.length];

                    String memoryStorage[] = new String[dateTime.length];

                    if (dashboardList.get(k).get(5).equals("Ping"))
                    {
                        for (int i = 0; i < dateTime.length; i++)
                        {
                            receivedPacket[i] = UserDAO.getUpdatedPacket(Integer.parseInt(dashboardList.get(k).get(0)), dashboardList.get(k).get(1), dateTime[i]);
                        }

                        bean.setPacket(receivedPacket);
                    }
                    else
                    {
                        for (int i = 0; i < dateTime.length; i++)
                        {
                            memoryStorage[i] = String.valueOf(UserDAO.getUpdatedMemory(Integer.parseInt(dashboardList.get(k).get(0)), dashboardList.get(k).get(1), dateTime[i]));
                        }

                        bean.setMemory(memoryStorage);
                    }

                    bean.setCurrentTime(dateTime);

                    beanList.add(bean);
                }
            }
        }
        catch (Exception exception)
        {
            _logger.warn("dashboard page not found!");
        }

        return "success";
    }
}
