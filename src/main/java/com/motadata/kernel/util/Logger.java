package com.motadata.kernel.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger
{
    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMMM-yyyy");

    private static String date_Formate;

    private static String PATH = "/home/smit/IdeaProjects/Kernel/Log/";

    public void info(String message) throws IOException
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
            exception.printStackTrace();
        }
        finally
        {
            writer.close();
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

            // stack trace retrieve from throwable

        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            try {
                writer.close();
            } catch (IOException ignore) {

            }
        }
    }

    public void warn(String message) throws IOException
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
            exception.printStackTrace();
        }
        finally
        {
            writer.close();
        }
    }

    public void debug(String message) throws IOException
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
            exception.printStackTrace();
        }
        finally
        {
            writer.close();
        }
    }

    private String getDateFormat()
    {
        DateTime dateTime = DateTime.now();

        return formatter.print(dateTime);
    }
}
