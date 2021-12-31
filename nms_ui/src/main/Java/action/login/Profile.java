package action.login;

import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by smit on 31/12/21.
 */
public class Profile
{
    public String execute()
    {
        HttpServletRequest request= ServletActionContext.getRequest();

        HttpSession session=request.getSession();

        String s=(String)session.getAttribute("login");

        if(s!=null && !s.equals(""))
        {
            return "success";
        }
        else
        {
            return "error";
        }
    }
}
