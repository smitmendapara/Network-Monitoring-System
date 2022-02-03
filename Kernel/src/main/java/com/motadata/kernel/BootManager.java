package com.motadata.kernel;

import com.motadata.kernel.dao.CommonConstant_DAO;

import com.motadata.kernel.dao.DataAccess;

import com.motadata.kernel.discovery.HandlerThread;

import com.motadata.kernel.util.CommonConstant;

import com.motadata.kernel.util.Logger;

import com.motadata.kernel.discovery.Scheduler;

import org.h2.tools.Server;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.sql.*;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.Timer;

import java.util.concurrent.CountDownLatch;

public class BootManager
{
    private static final Logger _logger = new Logger();

    private static final DataAccess _dao = new DataAccess();

    private static Server tcp_Server = null;

    private static Server web_Server = null;

    public static void main(String[] args)
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

            if (startH2Database()) // first step start h2 database server
            {
                _logger.info("Started h2 database!");

                _logger.info("Verifying h2 database...");

                if (verifyH2Database()) // verify the h2 database
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

                                if (startScheduler()) // start scheduler
                                {
                                    _logger.info("scheduler started!");
                                }
                                else
                                {
                                    _logger.warn("still not start scheduler...");
                                }
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

            // initialize collector (ping,linux ssh)
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong to be start kernel!", exception);
        }

    }

    private static boolean startScheduler()
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

    public static boolean startH2Database()
    {
        String baseDir = CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + "h2";

        boolean result = true;

        try
        {
            tcp_Server = Server.createTcpServer("-baseDir", baseDir, CommonConstant.TCP_OPTION).start();

            web_Server = Server.createWebServer("-baseDir", baseDir, CommonConstant.WEB_OPTION).start();

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

    public static boolean verifyH2Database()
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

    public static boolean startJettyServer()
    {
        String jettyPATH = CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + CommonConstant.JETTY_NAME;

        String jettyCommand = "java -jar " + jettyPATH + CommonConstant.JETTY_JAR;

        boolean result = true;

        try
        {
            Process process = executeCommand(jettyCommand);

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
        HashMap<Short, Object> properties = new HashMap<>();

        boolean result = true;

        try
        {
            System.out.println();

            System.out.println("---------------------------- load h2 Database ------------------------------");

            Connection connection = _dao.getConnection();

            setProperties(properties, connection);

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

            result = false;
        }

        return result;
    }

    private static void setProperties(HashMap<Short, Object> properties, Connection connection)
    {
        HashMap<String, ArrayList<String>> dataCollection = new HashMap<>();

        selectStatement(properties, connection);

        insertStatement(properties, connection, dataCollection);

        updateStatement(properties, connection);

        deleteStatement(properties, connection);

    }

    private static void deleteStatement(HashMap<Short, Object> properties, Connection connection)
    {
        properties.put(CommonConstant_DAO.DATABASE_CONNECTION, connection);

        properties.put(CommonConstant_DAO.TABLE_NAME, "TB_USER");

        properties.put(CommonConstant_DAO.DATABASE_CONDITION, "WHERE ID = 1");

        properties.put(CommonConstant_DAO.DATABASE_OPERATION, CommonConstant_DAO.DELETE);
    }

    private static void updateStatement(HashMap<Short, Object> properties, Connection connection)
    {
        properties.put(CommonConstant_DAO.DATABASE_CONNECTION, connection);

        properties.put(CommonConstant_DAO.TABLE_NAME, "TB_USER");

        properties.put(CommonConstant_DAO. UPDATE_COLUMN_NAME, "USER = 'Dharm'");

        properties.put(CommonConstant_DAO.DATABASE_CONDITION, "WHERE ID = 2");

        properties.put(CommonConstant_DAO.DATABASE_OPERATION, CommonConstant_DAO.UPDATE);
    }

    private static void insertStatement(HashMap<Short, Object> properties, Connection connection, HashMap<String, ArrayList<String>> dataCollection)
    {
        properties.put(CommonConstant_DAO.DATABASE_CONNECTION, connection);

        properties.put(CommonConstant_DAO.COLUMN_NAME, "ID,USER");

        properties.put(CommonConstant_DAO.TABLE_NAME, "TB_USER");

        properties.put(CommonConstant_DAO.DATABASE_OPERATION, CommonConstant_DAO.INSERT);

        insertLocalData(properties, dataCollection);
    }

    private static void insertLocalData(HashMap<Short, Object> properties, HashMap<String, ArrayList<String>> dataCollection)
    {
        ArrayList<String> data = new ArrayList<>();

        data.add("4, 'harsh', 'kaneria'");

        data.add("5, 'kishan', 'khirsariya'");

        data.add("6, 'parth', 'aghera'");

        data.add("7, 'harsh', 'boda'");

        dataCollection.put("TB_USER", data);

        properties.put(CommonConstant_DAO.TABLE_DATA, dataCollection);
    }

    private static void selectStatement(HashMap<Short, Object> properties, Connection connection)
    {
        properties.put(CommonConstant_DAO.DATABASE_CONNECTION, connection);

        properties.put(CommonConstant_DAO.COLUMN_NAME, "ID,USER");

        properties.put(CommonConstant_DAO.TABLE_NAME, "TB_USER");

        properties.put(CommonConstant_DAO.DATABASE_OPERATION, CommonConstant_DAO.SELECT);
    }

    public static boolean startNSQServer()
    {
        String nsq_PATH = CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + CommonConstant.NSQ_NAME;

        String nsqLookUpCommand = nsq_PATH + CommonConstant.NSQ_LOOKUP_COMMAND;

        String nsqDCommand = nsq_PATH + CommonConstant.NSQ_D_COMMAND;

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

                new HandlerThread(latch, nsqDCommand).start();

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

    public static Process executeCommand(String command)
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
