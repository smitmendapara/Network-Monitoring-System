package helper;

import dao.ConnectionPoolIml;
import util.Logger;

import javax.servlet.http.HttpServlet;

public class CommonServlet extends HttpServlet
{
    private static final Logger _logger = new Logger();

    @Override
    public void init()
    {
        try
        {
            if (BootManager.initializeDatabase())
            {
                ConnectionPoolIml connectionPoolIml = new ConnectionPoolIml();

                //TODO what if your connection not created as per your need ?
                connectionPoolIml.createPool();

                //TODO what if your schedular failts to start
                BootManager.startScheduler();

                Thread pollingThread = new Thread(new PollingInitializer());

                pollingThread.start();

                Thread provisionThread = new Thread(new ParallelDiscovery());

                provisionThread.start();
            }
        }
        catch (Exception exception)
        {
            _logger.error("servlet not init successfully!", exception);
        }
    }
}
