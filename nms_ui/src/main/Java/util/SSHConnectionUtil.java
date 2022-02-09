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

    public Session getSession()
    {
        return this.session;
    }

    public SSHConnectionUtil(String hostIp, int port, String username, String password, int timeout)
    {
        this.hostIp = hostIp;

        this.port = port;

        this.username = username;

        this.timeout = timeout;

        this.password = password;

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
            _logger.warn("connection disconnect!");
        }
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

    public boolean reCreateConnection()
    {
        boolean result = false;

        try
        {
            destroy();

            result = createConnection();
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

        Properties config = null;

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

    public boolean createConnection()
    {
        boolean connect = false;

        try
        {
            connect = connect();

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

    public static SSHConnectionUtil getSSHObject(String host, int port, String username, String password, int timeout)
    {
        SSHConnectionUtil sshConnectionUtil = null;

        try
        {
            sshConnectionUtil = new SSHConnectionUtil(host, port, username, password, timeout);

            if(!sshConnectionUtil.createConnection())
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

            sshConnectionUtil = null;
        }

        return sshConnectionUtil;
    }

    public String executeCommand(String command, boolean wait)
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

                channel.setInputStream(null);

                inputStream = channel.getInputStream();

                channel.connect();

                _logger.debug(hostIp + " - checking channel connection...");

                if (channel.isConnected())
                {
                    if (wait)
                    {
                        output.append(IOUtils.toString(inputStream));

                        output.append(CommonConstantUI.NEW_LINE);

                        _logger.debug(hostIp + " - command output -> \n" + output.toString());
                    }
                }
                else
                {
                    _logger.warn(hostIp + " - channel is not connected!");
                }
            }
            else
            {
                _logger.warn(hostIp + " - session is expired!");
            }

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
            _logger.error("command not executed properly!", exception);
        }

        return output.toString();
    }
}
