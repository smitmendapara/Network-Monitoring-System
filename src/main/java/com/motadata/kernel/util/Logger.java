package com.motadata.kernel.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by smit on 6/12/21.
 */
public class Logger
{
    private static FileWriter fileWriter;

    private static File log_File;

    private static DateTime current_Date;

    private static String PATH = "/home/smit/IdeaProjects/Kernel/Log/";

    private static DateTimeFormatter formatter;

    public void info(String message)
    {
        try
        {
            current_Date = get_Date();

            log_File = new File(PATH + current_Date+"-info.log");

            fileWriter = new FileWriter(log_File);

            fileWriter.write(current_Date + message);

            fileWriter.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public void error(String message, Throwable throwable)
    {
        try
        {
            current_Date = get_Date();

            log_File = new File(PATH +"-error.log");

            fileWriter = new FileWriter(log_File);

            fileWriter.write(current_Date + message);

            fileWriter.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public void warn(String message)
    {
        try
        {
            current_Date = get_Date();

            log_File = new File(PATH +"-warn.log");

            fileWriter = new FileWriter(log_File);

            fileWriter.write(current_Date + message);

            fileWriter.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    public void debug(String message)
    {
        try
        {
            current_Date = get_Date();

            log_File = new File(PATH +"-debug.log");

            fileWriter = new FileWriter(log_File);

            fileWriter.write(current_Date + message);

            fileWriter.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    // get current date

    public static DateTime get_Date()
    {
        DateTime date = new DateTime();

        return date;
    }
}
