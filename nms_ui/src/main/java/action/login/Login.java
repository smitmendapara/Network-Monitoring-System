package action.login;

import bean.LoginBean;
import com.opensymphony.xwork2.ModelDriven;
import dao.DAO;

import org.apache.struts2.dispatcher.SessionMap;

import org.apache.struts2.interceptor.SessionAware;

import util.CommonConstantUI;
import util.Logger;

import java.util.Map;

public class Login implements SessionAware, ModelDriven<LoginBean>
{
    private SessionMap<String,Object> sessionMap;

    private LoginBean loginBean = new LoginBean();

    private static final Logger _logger = new Logger();

    // login user
    public String executeLogin()
    {
        DAO dao = new DAO();

        try
        {
            if (!loginBean.getUsername().equals("") && !loginBean.getPassword().equals(""))
            {
                if(dao.checkCredential(loginBean.getUsername(), loginBean.getPassword()))
                {
                    sessionMap.put("login", Boolean.TRUE);

                    sessionMap.put("username", loginBean.getUsername());

                    return CommonConstantUI.SUCCESS;
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("user credentials was wrong.", exception);
        }

        return CommonConstantUI.ERROR;
    }

    @Override
    public void setSession(Map<String, Object> map)
    {
        sessionMap = (SessionMap) map;
    }

    @Override
    public LoginBean getModel()
    {
        return loginBean;
    }
}
