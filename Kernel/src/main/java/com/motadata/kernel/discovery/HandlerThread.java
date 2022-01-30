package com.motadata.kernel.discovery;

import com.motadata.kernel.util.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

public class HandlerThread extends Thread
{
    private static final Logger _logger = new Logger();

    CountDownLatch latch;

    String command;

    public HandlerThread(CountDownLatch latch, String command)
    {
        this.latch = latch;

        this.command = command;
    }

    @Override
    public void run()
    {
        try
        {
            latch.countDown();

            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String input = "";

            while ((input = reader.readLine()) != null)
            {

            }

            reader.close();

        }
        catch (Exception exception)
        {
            _logger.error("",exception);
        }
    }
}
