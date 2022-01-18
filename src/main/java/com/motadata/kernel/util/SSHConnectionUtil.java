package com.motadata.kernel.util;

import com.jcraft.jsch.ChannelExec;

import com.jcraft.jsch.JSch;

import com.jcraft.jsch.Session;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * Created by smit on 4/1/22.
 */

public class SSHConnectionUtil
{
    private Session session = null;

    private final int port;

    private final int timeout;

    private final String hostIp;

    private final String username;

    private final String password;

    private final String keyFilePath;

    private final String passPhrase;

    String NEW_LINE = System.lineSeparator();

    private static final Logger _logger = new Logger();

    public Session getSession()
    {
        return this.session;
    }

    public static SSHConnectionUtil getSSHObject(String hostIp, int port, String username, String password, int timeOut)
    {
        return new SSHConnectionUtil(hostIp, port, username, password, timeOut);
    }

    public static SSHConnectionUtil getSSHObject(String hostIp, String username, String password, int timeOut)
    {
        return new SSHConnectionUtil(hostIp, username, password, timeOut);
    }

    public SSHConnectionUtil(String hostIp, String username, String password)
    {

        this(hostIp, 22, username, password, null, null, 60);
    }

    public SSHConnectionUtil(String hostIp, String username, String password, int timeout)
    {

        this(hostIp, 22, username, password, null, null, timeout);
    }

    public SSHConnectionUtil(String hostIp, int port, String username, String password, int timeout)
    {

        this(hostIp, port, username, password, null, null, timeout);
    }

    public SSHConnectionUtil(String hostIp, int port, String username, String password, String keyFilePath, String passPhrase, int timeout)
    {
        this.hostIp = hostIp;

        this.port = port;

        this.username = username;

        this.keyFilePath = keyFilePath;

        this.passPhrase = passPhrase;

        this.timeout = timeout;

        this.password = password;
    }

    public boolean init()
    {
        boolean connect = false;

        try
        {
            if (keyFilePath != null && keyFilePath.trim().length() != 0)
            {
                connect = connectUsingKey();
            }
            else
            {
                connect = connect();
            }

            if (connect)
            {
                _logger.debug("connected to " + hostIp);
            }
            else
            {
                _logger.warn("failed to connect to " + hostIp);
            }
        }
        catch (Exception exception)
        {
            _logger.error("still now connected!", exception);
        }
        return connect;
    }

    public void destroy()
    {
        try
        {
            disconnect();
        }
        catch (Exception exception)
        {
            _logger.error("still not disconnect!", exception);
        }
    }

    public boolean reInit()
    {
        boolean result = false;

        try
        {
            destroy();

            result = init();
        }
        catch (Exception exception)
        {
            _logger.error("not destroy!", exception);
        }

        return result;
    }

    private boolean connect()
    {
        boolean connected = false;

        try
        {
            _logger.debug("checking connection with " + hostIp + " and port " + port);

            JSch jschObject = new JSch();

            session = jschObject.getSession(username, hostIp, port);

            if (password != null && password.trim().length() > 0)
            {
                session.setPassword(password);
            }

            session.setConfig("StrictHostKeyChecking", "no");

            session.connect(timeout * 1000);

            session.setTimeout(timeout * 1000);

            if (session.isConnected())
            {
                connected = true;

                _logger.debug(hostIp + " - connection established!");
            }
            else
            {
                _logger.warn(hostIp + " - connection failed");
            }
        }
        catch (Exception exception)
        {
            _logger.error("still not connect...", exception);
        }

        return connected;
    }

    private boolean connectUsingKey()
    {
        _logger.debug("checking connection with " + hostIp + " and port" + port + "");

        boolean connected = false;

        try
        {
            JSch jschObect = new JSch();

            session = jschObect.getSession(username, hostIp, port);

            if (passPhrase != null)
            {
                jschObect.addIdentity(keyFilePath, passPhrase);
            }
            else
            {
                jschObect.addIdentity(keyFilePath);
            }

            session.setConfig("StrictHostKeyChecking", "no");

            session.connect(timeout * 1000);

            session.setTimeout(timeout * 1000);

            if (session.isConnected())
            {
                connected = true;

                _logger.debug(hostIp + " - connection done!");
            }
            else
            {
                _logger.warn(hostIp + " - connection failed!");
            }
        }
        catch (Exception exception)
        {
            _logger.error("connect using key failed!", exception);
        }

        return connected;
    }

    private void disconnect()
    {
        try
        {
            if (session != null)
            {
                session.disconnect();
            }
        }
        catch (Exception exception)
        {
            _logger.warn("disconnect problem!");
        }
    }

    public String executeCommand(String command)
    {
        String execute = null;

        try
        {
            execute = executeCommand(command, true);
        }
        catch (Exception exception)
        {
            _logger.error("command execution failed!", exception);
        }

        return execute;
    }

    public String executeCommand(String command, boolean wait)
    {
        _logger.debug(this.hostIp + " - executing command " + command);

        ChannelExec channel = null;

        InputStream inputStream = null;

        StringBuilder output = new StringBuilder();

        try
        {
            if (this.session != null)
            {
                channel = (ChannelExec) this.session.openChannel("exec");

                channel.setCommand(command);

                channel.setInputStream(null);

                inputStream = channel.getInputStream();

                channel.connect();

                _logger.debug(this.hostIp + " - checking channel connection...");

                if (channel.isConnected())
                {
                    _logger.debug(this.hostIp + " - channel is currently connected...");

                    if (wait)
                    {
                        output.append(IOUtils.toString(inputStream));

                        output.append(NEW_LINE);

                        _logger.debug(this.hostIp + " - command output -> " + output.toString());
                    }
                }
                else
                {
                    _logger.warn(this.hostIp + " - channel is not connected!");
                }
            }
            else
            {
                _logger.warn(this.hostIp + " - session is expired!");
            }

            if (inputStream != null)
            {
                inputStream.close();

                _logger.debug(this.hostIp + " - channel is closed for communication!");
            }

            if (channel != null && !channel.isClosed())
            {
                channel.disconnect();

                _logger.debug(this.hostIp + " - channel is disconnected!");
            }

        }
        catch (Exception exception)
        {
            _logger.error("command not executed properly!", exception);
        }

        return output.toString();
    }

    public static SSHConnectionUtil getNewSSHObject(String host, int port, String username, String password, int timeout)
    {
        _logger.info("SSH to hostIp - " + host);

        SSHConnectionUtil sshUtil = null;

        try
        {
            sshUtil = new SSHConnectionUtil(host, port, username, password, timeout);

            if(!sshUtil.init())
            {
                _logger.info("SSH failed for the hostIp " + host);

                return null;
            }

            _logger.info("Successful SSH to hostIp " + host);
        }
        catch (Exception exception)
        {
            _logger.info("SSH failed for the hostIp " + host);

            _logger.error("failed for get SSH object", exception);

            sshUtil = null;
        }

        return sshUtil;
    }
}