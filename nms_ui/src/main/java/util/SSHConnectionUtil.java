package util;

import com.jcraft.jsch.ChannelExec;

import com.jcraft.jsch.JSch;

import com.jcraft.jsch.Session;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import java.util.Properties;

public class SSHConnectionUtil
{
    private Session session = null;

    private int port;

    private int timeout;

    private String hostIp;

    private String username;

    private String password;

    private static final Logger _logger = new Logger();

    public SSHConnectionUtil(String hostIp, int port, String username, String password, int timeout)
    {
        this.hostIp = hostIp;

        this.port = port;

        this.username = username;

        this.timeout = timeout;

        this.password = password;

    }

    public void disconnect()
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
            _logger.warn("connection disconnect!");
        }
    }

    private boolean createConnection()
    {
        boolean connected = false;

        Properties config;

        try
        {
            JSch jschObject = new JSch();

            session = jschObject.getSession(username, hostIp, port);

            if (password != null && password.trim().length() > 0)
            {
                session.setPassword(password);
            }

            config = new Properties();

            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);

            session.connect(timeout * 1000);

            if (session.isConnected())
            {
                connected = true;
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

    public static SSHConnectionUtil getSSHObject(String host, int port, String username, String password, int timeout)
    {
        SSHConnectionUtil sshConnectionUtil;

        try
        {
            sshConnectionUtil = new SSHConnectionUtil(host, port, username, password, timeout);

            if(!sshConnectionUtil.createConnection())
            {
                _logger.info("SSH failed for the hostIp " + host);

                sshConnectionUtil = null;
            }
        }
        catch (Exception exception)
        {
            _logger.error("failed for get SSH object", exception);

            sshConnectionUtil = null;
        }

        return sshConnectionUtil;
    }

    public String executeCommand(String command)
    {
        ChannelExec channel = null;

        InputStream inputStream = null;

        StringBuilder output = new StringBuilder();

        try
        {
            if (session != null)
            {
                channel = (ChannelExec) session.openChannel("exec");

                channel.setCommand(command);

                inputStream = channel.getInputStream();

                channel.connect();

                // wait for command output
                while (channel.isConnected())
                {
                    Thread.sleep(100);
                }

                output.append(IOUtils.toString(inputStream));

                output.append(CommonConstant.NEW_LINE);

                _logger.debug(hostIp + " - command output -> \n" + output.toString());
            }
            else
            {
                _logger.warn(hostIp + " - session is expired!");
            }

        }
        catch (Exception exception)
        {
            _logger.error("command not executed properly!", exception);
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }

                if (channel != null && !channel.isClosed())
                {
                    channel.disconnect();
                }
            }
            catch (Exception exception)
            {
                _logger.error("input stream and channel is not closed.", exception);
            }
        }

        return output.toString();
    }
}
