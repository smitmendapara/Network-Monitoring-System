package action.dashboard;

import action.dao.UserDAO;
import action.util.Logger;
import com.opensymphony.xwork2.ActionSupport;

import java.sql.ResultSet;

/**
 * Created by smit on 17/1/22.
 */
public class Dashboard extends ActionSupport
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

    private static final Logger _logger = new Logger();

//    private static final UserDAO _dao = new UserDAO();

    public String showDashboardData()
    {
        String deviceType = null;

        try
        {
            UserDAO _dao = new UserDAO(id, ip);

            ResultSet resultSet = UserDAO.getDashboardData();

            while (resultSet.next())
            {
                deviceType = resultSet.getString(6);
            }

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
            _logger.warn("dashboard page not found!");
        }

        return null;
    }
}
