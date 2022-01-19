package action.dashboard;

import action.dao.UserDAO;

/**
 * Created by smit on 17/1/22.
 */
public class Dashboard
{
    private int id;

    private String ip;

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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    UserDAO _dao = new UserDAO();

    public String showDashboardData()
    {
        UserDAO _dao = new UserDAO(id, ip);

        return "success";
    }
}
