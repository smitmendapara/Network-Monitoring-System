package service;

import org.h2.tools.Server;
import util.CommonConstantUI;
import util.Logger;

import java.util.Timer;

public class BootManager
{
    private static final Logger _logger = new Logger();

    public static void startKernel()
    {
        try
        {
            Class.forName("org.h2.Driver");

            _logger.info("Starting h2 database...");

            if (startH2Database()) // first step start h2 database server
            {
                _logger.info("Started h2 database!");
            }
            else
            {
                _logger.warn("still not started h2 database...");
            }
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong to be start kernel!", exception);
        }

    }

    private static boolean startH2Database()
    {
        boolean result = true;

        Server tcp_Server, web_Server;

        String baseDir = CommonConstantUI.CURRENT_DIR + CommonConstantUI.PATH_SEPARATOR + "h2";

        try
        {
            tcp_Server = Server.createTcpServer("-baseDir", baseDir, CommonConstantUI.TCP_OPTION).start();

            web_Server = Server.createWebServer("-baseDir", baseDir, CommonConstantUI.WEB_OPTION).start();

            System.out.println("-------------------------- start h2 Database ------------------------------");

            System.out.println();

            System.out.println("h2 database tcp server started! " + tcp_Server);

            System.out.println("h2 database web server started! " + web_Server);
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on the creating database side!",exception);

            result = false;
        }

        return result;
    }

    public static boolean startScheduler()
    {
        boolean result = true;

        try
        {
            Scheduler scheduler = new Scheduler();

            Timer timer = new Timer();

            timer.scheduleAtFixedRate(scheduler, 60 * 1000, 60 * 1000);

            Thread schedulerThread = new Thread(scheduler);

            schedulerThread.start();
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong to be start scheduler", exception);

            result = false;
        }

        return result;
    }
}
