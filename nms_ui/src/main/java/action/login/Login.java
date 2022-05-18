package action.login;

import bean.LoginBean;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.dispatcher.SessionMap;

import org.apache.struts2.interceptor.SessionAware;

import service.LoginService;
import util.CommonConstant;
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
        try
        {
            if (!loginBean.getUsername().equals("") && !loginBean.getPassword().equals(""))
            {
                if(LoginService.verifyLogin(loginBean))
                {
                    sessionMap.put("username", loginBean.getUsername());

                    return CommonConstant.SUCCESS;
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("user credentials was wrong.", exception);
        }

        return CommonConstant.ERROR;
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
