package com.motadata.kernel.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger
{
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

    public void error(String message, Throwable throwable) throws IOException
    {
        BufferedWriter writer = null;

        try
        {
            date_Formate = getDateFormat();

            writer = new BufferedWriter(new FileWriter(PATH + date_Formate +"-error.log", true));

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

        LocalDate localDate =  new LocalDate(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMMM-yyyy");

        return formatter.print(localDate);
    }
}
