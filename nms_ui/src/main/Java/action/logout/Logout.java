package action.logout;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

/**
 * Created by smit on 3/1/22.
 */
public class Logout extends ActionSupport
{
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
            exception.printStackTrace();
        }

        return "error";
    }
}
