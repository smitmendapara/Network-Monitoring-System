package helper;

import dao.ConnectionPoolIml;
import util.Logger;

import javax.servlet.http.HttpServlet;

public class CommonServlet extends HttpServlet
{
    private static final Logger _logger = new Logger();

    public void init()
    {
        try
        {
            if (CommonServletHelper.initializeDatabase())
            {
                _logger.info("database successfully initialized!");

                ConnectionPoolIml connectionPoolIml = new ConnectionPoolIml();

                //TODO what if your connection not created as per your need ?
                if (connectionPoolIml.createPool())
                {
                    _logger.info("connection pool successfully created!");

                    //TODO what if your schedular failts to start
                    if (CommonServletHelper.startScheduler())
                    {
                        _logger.info("scheduler successfully started!");

                        Thread pollingThread = new Thread(new PollingInitializer());

                        pollingThread.start();

                        Thread provisionThread = new Thread(new ParallelDiscovery());

                        provisionThread.start();
                    }
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("servlet not init successfully!", exception);
        }
    }
}
