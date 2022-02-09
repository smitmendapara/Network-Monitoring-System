package action.dashboard;

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

            resultSet = UserDAO.getDashboardData();

            if (resultSet != null)
            {
                while (resultSet.next())
                {
                    bean = new DashboardBean();

                    bean.setIP(resultSet.getString(3));

                    bean.setId(resultSet.getInt(1));

                    bean.setDevice(resultSet.getString(6));

                    bean.setName(resultSet.getString(2));

                    if (resultSet.getString(4) == null)
                    {
                        profile = "null";

                        bean.setUsername(profile);
                    }
                    else
                    {
                        bean.setUsername(resultSet.getString(4));
                    }

                    bean.setStatus(resultSet.getString(8));

                    bean.setCurrentTime(resultSet.getString(9));

                    bean.setResponse(resultSet.getString(7));

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
