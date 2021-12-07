package com.motadata.kernel;

import com.motadata.kernel.util.CommonConstant;
import com.motadata.kernel.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class BootManager
{
    private static final Logger _logger = new Logger();

    private static org.h2.tools.Server m_server = null;

    private static org.h2.tools.Server w_server = null;

    public static void main(String[] args) throws IOException
    {
        try
        {
            Class.forName("org.h2.Driver");

            startKernel();


        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on the initially execution!", exception);
        }

    }

    private static void startKernel() throws IOException
    {
        try
        {
            _logger.info("Starting h2 database");

            if (starth2Database()) // first step start h2 database server
            {
                _logger.info("Started h2 database");

                _logger.info("Verifying h2 database");

                if (verifyh2Database()) // verify the h2 database
                {
                    _logger.info("Verified h2 database");

                    _logger.info(" Starting Jetty server");

                    if (startJettyServer()) // start jetty server
                    {
                        _logger.info("Jetty server started");
                    }
                    else
                    {
                        _logger.warn("not jetty server started yet...");
                    }
                }
                else
                {
                    _logger.warn("not database verified yet...");
                }
            }
            else
            {
                _logger.warn("not database started yet...");
            }

            // load h2 database configuration

            // start nsq

            // init nsq consumer

            // start scheduler

            // implement discovery (ping,linux ssh)

            // initilzie collector (ping,linux ssh)
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong to be start kernel!", exception);
        }

    }

    public static boolean starth2Database() throws IOException
    {
        String baseDir = CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + "h2";

        boolean result = true;

        try
        {
            m_server = org.h2.tools.Server.createTcpServer("-baseDir", baseDir, "-tcpAllowOthers").start();

            w_server = org.h2.tools.Server.createWebServer("-baseDir", baseDir, "-webAllowOthers").start();

            System.out.println("h2 database tcp server started! " + m_server);

            System.out.println("h2 database web server started! " + w_server);
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on the creating database side!",exception);

            result = false;
        }

        return result;
    }

    public static boolean verifyh2Database() throws IOException
    {
        String DB_URL = "jdbc:h2:tcp://localhost/configdb;DATABASE_TO_UPPER=false;IGNORECASE=TRUE;MODE=PostgreSQL;AUTO_RECONNECT=TRUE;AUTO_SERVER=TRUE;MV_STORE=FALSE;MVCC=FALSE";

        String USER = "motadata";

        String PASS = "motadata";

        Connection connection = null;

        boolean result = true;

        try
        {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on the verifying database side!", exception);
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (Exception exception)
                {
                    _logger.error("something went wrong on the database connection side!", exception);

                    result = false;
                }
            }
        }

        return result;
    }

    public static boolean startJettyServer() throws IOException
    {
        String jettyCommand = "java -jar /home/smit/Documents/Server/Jetty/jetty-distribution-9.4.44.v20210927/start.jar";

        boolean result = true;

        try
        {
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(jettyCommand);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String input = "";

            while ((input = reader.readLine()) != null)
            {

            }

            reader.close();

            System.out.println("Jetty server started!");
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on the starting jetty server!", exception);

            result = false;
        }

        return result;
    }
}
