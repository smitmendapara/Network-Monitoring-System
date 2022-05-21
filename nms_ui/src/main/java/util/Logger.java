package util;

import org.joda.time.DateTime;

import org.joda.time.format.DateTimeFormat;

import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;

import java.io.File;

import java.io.FileWriter;

import java.io.IOException;

public class Logger
{
    private String dateFormat;

    private File file = new File(CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + "log");

    private final String PATH = CommonConstant.CURRENT_DIR + CommonConstant.PATH_SEPARATOR + CommonConstant.DIRECTORY_NAME + CommonConstant.PATH_SEPARATOR;

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern(CommonConstant.LOGGER_DATE_FORMAT);

    private static final Logger _logger = new Logger();

    public void info(String message)
    {
        BufferedWriter writer = null;

        try
        {
            dateFormat = getDateFormat();

            file.mkdir();

            writer = new BufferedWriter(new FileWriter(PATH + dateFormat +"-info.log", CommonConstant.TRUE));

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
                if (writer != null)
                {
                    writer.close();
                }
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
            dateFormat = getDateFormat();

            file.mkdir();

            writer = new BufferedWriter(new FileWriter(PATH + dateFormat +"-error.log", CommonConstant.TRUE));

            writer.write(message + "\n");

            StackTraceElement[] stackTrace = throwable.getStackTrace();

            for (int index = 0; index < stackTrace.length; index++)
            {
                _logger.warn("error stacktrace -> " + stackTrace[index]);
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
                if (writer != null)
                {
                    writer.close();
                }
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
            dateFormat = getDateFormat();

            file.mkdir();

            writer = new BufferedWriter(new FileWriter(PATH + dateFormat +"-warn.log", CommonConstant.TRUE));

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
                if (writer != null)
                {
                    writer.close();
                }
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
            dateFormat = getDateFormat();

            file.mkdir();

            writer = new BufferedWriter(new FileWriter(PATH + dateFormat +"-debug.log", CommonConstant.TRUE));

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
                if (writer != null)
                {
                    writer.close();
                }
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
