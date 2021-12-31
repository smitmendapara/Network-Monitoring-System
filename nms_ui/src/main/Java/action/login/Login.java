package action.login;

import action.dao.UserDAO;

import com.opensymphony.xwork2.ActionSupport;

public class Login extends ActionSupport
{
    private int id;

    private String username;

    private String password;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String executeLogin()

    {
        if(UserDAO.check(username, password)) // UserDAO.check(username, password)
        {
            return "success";
        }
        else
        {
            return "error";
        }
    }

}
