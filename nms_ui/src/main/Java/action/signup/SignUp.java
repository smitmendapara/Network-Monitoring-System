package action.signup;

import dao.UserDAO;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

public class SignUp extends ActionSupport
{
    private String username;

    private String password;

    private static final Logger _logger = new Logger();

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

    public String executeSignUp()
    {
        try
        {
            if (UserDAO.enterSignUpData(username, password))
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
            _logger.error("signUp not execute properly!", exception);
        }

        return null;
    }
}
