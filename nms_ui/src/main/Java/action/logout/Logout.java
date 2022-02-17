package action.logout;

import org.apache.struts2.dispatcher.SessionMap;

import org.apache.struts2.interceptor.SessionAware;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.util.Map;

public class Logout extends ActionSupport implements SessionAware
{
    private SessionMap<String,Object> sessionMap;

    private static final Logger _logger = new Logger();

    public String executeLogout()
    {
        try
        {
            if (sessionMap != null)
            {
                sessionMap.invalidate();
            }

            return "success";
        }
        catch (Exception exception)
        {
            _logger.error("logout not execute properly!", exception);
        }

        return "error";
    }

    @Override
    public void setSession(Map<String, Object> map)
    {
        sessionMap = (SessionMap) map;
    }
}
