package helper;

import dao.ConnectionPoolIml;
import javax.servlet.http.HttpServlet;

public class ConnectionPoolServlet extends HttpServlet
{
    @Override
    public void init()
    {
        ConnectionPoolIml connectionPoolIml = new ConnectionPoolIml();

        connectionPoolIml.createPool();


    }
}
