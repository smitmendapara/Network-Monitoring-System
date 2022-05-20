package service;

import bean.LoginBean;
import dao.ConnectionDAO;
import util.CommonConstant;
import util.Logger;

public class LoginService
{
    private static final Logger _logger = new Logger();

    public static boolean verifyLogin(LoginBean loginBean)
    {
        boolean status = CommonConstant.FALSE;

        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            status = connectionDao.checkCredential(loginBean.getUsername(), loginBean.getPassword());
        }
        catch (Exception exception)
        {
            _logger.error("still not login.", exception);
        }

        return status;
    }
}
