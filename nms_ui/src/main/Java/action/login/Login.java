package action.login;

import dao.UserDAO;

import org.apache.struts2.dispatcher.SessionMap;

import org.apache.struts2.interceptor.SessionAware;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.util.Map;

public class Login extends ActionSupport implements SessionAware
{
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private SessionMap<String,Object> sessionMap;

    private static final Logger _logger = new Logger();

    private static final UserDAO _dao = new UserDAO();

    public String executeLogin()
    {
        try
        {
            if (!username.equals("") && !password.equals(""))
            {
                if(_dao.checkCredential(username, password))
                {
                    sessionMap.put("login", true);

                    sessionMap.put("username", username);

                    return "success";
                }
                else
                {
                    return "error";
                }
            }
            else
            {
                return "error";
            }
        }
        catch (Exception exception)
        {
            _logger.error("user not login properly!", exception);
        }

        return null;
    }

    @Override
    public void setSession(Map<String, Object> map)
    {
        sessionMap = (SessionMap) map;
    }
}
