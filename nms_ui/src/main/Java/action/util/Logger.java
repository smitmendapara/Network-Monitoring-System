package action.util;

import org.joda.time.DateTime;

import org.joda.time.format.DateTimeFormat;

import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;

import java.io.FileWriter;

import java.io.IOException;

/**
 * Created by smit on 3/1/22.
 */
public class Logger
{
    private static final Logger _logger = new Logger();

    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMMM-yyyy");

    private static String date_Formate;

    private static String PATH = CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + "log/";


    public void info(String message)
    {
        BufferedWriter writer = null;

        try
        {
            date_Formate = getDateFormat();

            writer = new BufferedWriter(new FileWriter(PATH + date_Formate +"-info.log", true));

            writer.write(message + "\n");
        }
        catch (IOException exception)
        {
            _logger.error("not write message into info.log file", exception);
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException ignore)
            {
                _logger.warn("writer not close!");
            }
        }
    }

    public void error(String message, Throwable throwable)
    {
        BufferedWriter writer = null;

        try
        {
            date_Formate = getDateFormat();

            writer = new BufferedWriter(new FileWriter(PATH + date_Formate +"-error.log", true));

            writer.write(message + "\n");

            StackTraceElement[] stackTrace = throwable.getStackTrace();// stack trace retrieve from throwable

            for (int i = 0; i < stackTrace.length; i++)
            {
                System.out.println("Index " + i + " of stack trace" + " array contains = " + stackTrace[i].toString());
            }

        }
        catch (IOException exception)
        {
            _logger.error("not write message into info.log file", exception);
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException ignore)
            {
                _logger.warn("writer not close!");
            }
        }
    }

    public void warn(String message)
    {
        BufferedWriter writer = null;

        try
        {
            date_Formate = getDateFormat();

            writer = new BufferedWriter(new FileWriter(PATH + date_Formate +"-warn.log", true));

            writer.write(message + "\n");
        }
        catch (IOException exception)
        {
            _logger.error("not write message into info.log file", exception);
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException ignore)
            {
                _logger.warn("writer not close!");
            }
        }
    }

    public void debug(String message)
    {
        BufferedWriter writer = null;

        try
        {
            date_Formate = getDateFormat();

            writer = new BufferedWriter(new FileWriter(PATH + date_Formate +"-debug.log", true));

            writer.write(message + "\n");
        }
        catch (IOException exception)
        {
            _logger.error("not write message into info.log file", exception);
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException ignore)
            {
                _logger.warn("writer not close!");
            }
        }
    }

    private String getDateFormat()
    {
        DateTime dateTime = DateTime.now();

        return formatter.print(dateTime);
    }
}
