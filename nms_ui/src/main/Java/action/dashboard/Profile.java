package action.dashboard;

import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

public class Profile
{
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
            exception.printStackTrace();
        }

        return null;
    }
}
