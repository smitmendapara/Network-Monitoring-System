package action.logout;

import org.apache.struts2.dispatcher.SessionMap;

import org.apache.struts2.interceptor.SessionAware;

import util.CommonConstantUI;
import util.Logger;

import java.util.Map;

public class Logout implements SessionAware
{
    private SessionMap<String,Object> sessionMap;

    private static final Logger _logger = new Logger();

    // logout user
    public String executeLogout()
    {
        try
        {
            if (sessionMap != null)
            {
                sessionMap.invalidate();
            }
        }
        catch (Exception exception)
        {
            _logger.error("user not logged out.", exception);
        }

        return CommonConstantUI.SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> map)
    {
        sessionMap = (SessionMap) map;
    }
}
