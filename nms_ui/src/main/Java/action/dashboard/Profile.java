package action.dashboard;

import action.util.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

public class Profile
{
    private static final Logger _logger = new Logger();

    public String executeProfile()
    {
        try
        {
            HttpServletRequest request = ServletActionContext.getRequest();

            HttpSession session = request.getSession();

            String string = (String)session.getAttribute("login");

            if(string != null && !string.equals(""))
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
            _logger.error("profile not properly executed!", exception);
        }

        return null;
    }
}
