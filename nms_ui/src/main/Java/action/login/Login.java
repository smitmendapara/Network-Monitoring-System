package action.login;

import action.dao.UserDAO;

import action.util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

public class Login extends ActionSupport
{
    private int id;

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

    private static final Logger _logger = new Logger();

    HttpServletRequest request = ServletActionContext.getRequest();

    HttpSession session = request.getSession();

    public String executeLogin()
    {
        try
        {
            if(UserDAO.checkCredential(username, password))
            {
                session.setAttribute("login", "true");

                session.setAttribute("username", username);

                return "success";
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
}
