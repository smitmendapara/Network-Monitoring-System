package action.login;

import bean.LoginBean;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;

import org.apache.struts2.interceptor.SessionAware;

import service.LoginService;
import util.CommonConstant;
import util.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
                    HttpServletRequest httpServletRequest = ServletActionContext.getRequest();

                    HttpSession session = httpServletRequest.getSession();

                    session.setAttribute("loginUser", session.getId());

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

    public String currentLoginSession()
    {
        try
        {
            HttpServletRequest httpServletRequest = ServletActionContext.getRequest();

            HttpSession httpSession = httpServletRequest.getSession();

            loginBean.setUsername(httpSession.getAttribute("loginUser").toString());
        }
        catch (Exception exception)
        {
            _logger.error("session not found.", exception);
        }

        return CommonConstant.SUCCESS;
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
