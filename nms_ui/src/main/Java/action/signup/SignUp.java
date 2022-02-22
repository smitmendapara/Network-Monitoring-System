package action.signup;

import dao.UserDAO;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

public class SignUp extends ActionSupport
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

    private static final Logger _logger = new Logger();

    private static final UserDAO _dao = new UserDAO();

    public String executeSignUp()
    {
        try
        {
            if (!username.equals("") && !password.equals(""))
            {
                if (_dao.enterSignUpData(username, password))
                {
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
            _logger.error("signUp not execute properly!", exception);
        }

        return null;
    }
}
