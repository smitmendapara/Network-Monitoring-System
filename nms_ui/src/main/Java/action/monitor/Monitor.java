package action.monitor;

import action.dao.UserDAO;
import com.opensymphony.xwork2.ActionSupport;

import java.sql.ResultSet;

/**
 * Created by smit on 10/1/22.
 */
public class Monitor extends ActionSupport
{
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String executeMonitor()
    {
        ResultSet resultSet = UserDAO.getMonitorizeData(id);

        boolean result = UserDAO.enterData(resultSet);

        if (result)
        {
            return "success";
        }

        return "error";
    }
}
