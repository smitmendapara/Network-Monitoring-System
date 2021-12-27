package com.motadata.kernel;

import com.motadata.kernel.dao.CommonConstant_DAO;
import com.motadata.kernel.dao.DataAccess;
import com.motadata.kernel.util.CommonConstant;
import com.motadata.kernel.util.HandlerThread;
import com.motadata.kernel.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BootManager
{
    private static final Logger _logger = new Logger();

    private static final DataAccess _dao = new DataAccess();

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

    private static void startKernel()
    {
        try
        {
            _logger.info("Starting h2 database...");

            if (starth2Database()) // first step start h2 database server
            {
                _logger.info("Started h2 database!");

                _logger.info("Verifying h2 database...");

                if (verifyh2Database()) // verify the h2 database
                {
                    _logger.info("Verified h2 database!");

                    _logger.info("Starting Jetty server...");

                    if (startJettyServer()) // start jetty server
                    {
                        _logger.info("Jetty server started!");

                        _logger.info("Load h2 database configuration...");

                        if (databaseConfigure()) // load h2 database configuration
                        {
                            _logger.info("h2 database configured!");

                            _logger.info("Starting NSQ server...");

                            if (startNSQServer()) // start nsq
                            {
                                _logger.info("NSQ server started!");
                            }
                            else
                            {
                                _logger.warn("still not started NSQ server...");
                            }
                        }
                        else
                        {
                            _logger.warn("still not configured h2 database...");
                        }
                    }
                    else
                    {
                        _logger.warn("still not started jetty server...");
                    }
                }
                else
                {
                    _logger.warn("still not verified h2 database...");
                }
            }
            else
            {
                _logger.warn("still not started h2 database...");
            }

            // init nsq consumer

            // start scheduler

            // implement discovery (ping,linux ssh)

            // initialize collector (ping,linux ssh)
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong to be start kernel!", exception);
        }

    }

    public static boolean starth2Database()
    {
        String baseDir = CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + "h2";

        boolean result = true;

        try
        {
            m_server = org.h2.tools.Server.createTcpServer("-baseDir", baseDir, "-tcpAllowOthers").start();

            w_server = org.h2.tools.Server.createWebServer("-baseDir", baseDir, "-webAllowOthers").start();

            System.out.println("-------------------------- start h2 Database ------------------------------");

            System.out.println();

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

    public static boolean verifyh2Database()
    {
        Connection connection = null;

        boolean result = true;

        try
        {
            connection = _dao.getConnection();
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
                    result = false;
                }
            }
            else
            {
                result = false;
            }
        }

        return result;
    }

    public static boolean startJettyServer() throws IOException
    {
        String jetty_PATH = CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + "jetty-distribution-9.4.44.v20210927/";

        String jettyCommand = "java -jar " + jetty_PATH + "start.jar";

        boolean result = true;

        try
        {
            Process process = execute_Command(jettyCommand);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String input = "";

            while ((input = reader.readLine()) != null)
            {

            }

            reader.close();

            System.out.println();

            System.out.println("----------------------------------- Jetty ----------------------------------");

            System.out.println();

            System.out.println("Jetty server started!");
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on the starting jetty server side!", exception);

            result = false;
        }

        return result;
    }

    public static boolean databaseConfigure()
    {
        HashMap<String, String> result = new HashMap<String, String>();

        boolean check = true;

        try
        {
            System.out.println();

            System.out.println("---------------------------- load h2 Database ------------------------------");

            Connection connection = _dao.getConnection();

            HashMap<Short, Object> properties = new HashMap<>();

            properties.put(CommonConstant_DAO.DATABASE_CONNECTION, connection);

            properties.put(CommonConstant_DAO.DATABASE_OPERATION, CommonConstant_DAO.SELECT);

            properties.put(CommonConstant_DAO.DATABASE_OPERATION, CommonConstant_DAO.INSERT);

            properties.put(CommonConstant_DAO.COLUMN_NAME, "ID,USER");

            properties.put(CommonConstant_DAO.TABLE_NAME, "TB_USER");

            ArrayList<String> data = new ArrayList<>();

            data.add("3, 'abc'");

            properties.put(CommonConstant_DAO.TABLE_DATA, data);

//            _dao.configureDB(properties);

            System.out.println();

            if (_dao.configureDB(properties))
            {
                System.out.println("successfully configured!");

                System.out.println();
            }

        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on the configuring h2 database server!", exception);

            check = false;
        }

        return check;
    }

    public static boolean startNSQServer() throws IOException
    {
        String nsq_PATH = CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + "nsq-1.2.1.linux-amd64.go1.16.6/bin/";

        String nsqLookUpCommand = nsq_PATH + "./nsqlookupd";

        String nsqdCommand = nsq_PATH + "./nsqd --lookupd-tcp-address=127.0.0.1:4160";

        boolean result = true;

        boolean flag = false;

        try
        {
            if (result)
            {
                CountDownLatch latch = new CountDownLatch(1);

                new HandlerThread(latch, nsqLookUpCommand).start();

                latch.await();

                flag = true;

                System.out.println("----------------------------------- NSQ ----------------------------------");

                System.out.println();

                System.out.println("nsqlookupd started!");
            }
            if (flag)
            {
                CountDownLatch latch = new CountDownLatch(1);

                new HandlerThread(latch, nsqdCommand).start();

                latch.await();

                System.out.println("nsqd started!");
            }
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on the starting NSQ server!", exception);

            result = false;
        }

        return result;
    }

    public static Process execute_Command(String command)
    {
        Runtime runtime;

        Process process = null;

        try
        {
            runtime = Runtime.getRuntime();

            process = runtime.exec(command);
        }
        catch (Exception exception)
        {
            _logger.error("not execute command properly!", exception);
        }

        return process;
    }
}