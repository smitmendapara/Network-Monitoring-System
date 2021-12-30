package com.motadata.action.login;

public class LoginModel
{
    protected int id;

    protected String username;

    protected String password;

    public LoginModel(int id, String username, String password)
    {
        super();

        this.id = id;

        this.username = username;

        this.password = password;
    }

    public LoginModel(String username, String password)
    {
        this.username = username;

        this.password = password;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

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
}
