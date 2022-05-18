package helper;

import dao.ConnectionPoolIml;
import service.BootManager;
import service.ParallelProvision;

import javax.servlet.http.HttpServlet;

public class ConnectionPoolServlet extends HttpServlet
{
    //TODO removed if it is not being used
    @Override
    public void init()
    {
        BootManager.startKernel();

        ConnectionPoolIml connectionPoolIml = new ConnectionPoolIml();

        connectionPoolIml.createPool();

        BootManager.startScheduler();

        Thread provisionThread = new Thread(new ParallelProvision());

        provisionThread.start();
    }
}
