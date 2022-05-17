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

                connectionPoolIml.createPool();

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
