package action.logout;

import action.util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

public class Logout extends ActionSupport
{
    private static final Logger _logger = new Logger();

    public String executeLogout()
    {
        try
        {
            HttpServletRequest request = ServletActionContext.getRequest();

            HttpSession session = request.getSession();

            session.removeAttribute("user");

            session.invalidate();

            return "success";
        }
        catch (Exception exception)
        {
            _logger.error("logout not execute properly!", exception);
        }

        return "error";
    }
}
