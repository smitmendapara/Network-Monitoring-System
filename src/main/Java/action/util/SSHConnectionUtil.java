package action.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by smit on 18/1/22.
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

    private static final Logger _logger = new Logger();

    public Session getSession()
    {
        return this.session;
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

    public boolean reConnection()
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

        try
        {
            _logger.debug("checking connection with " + hostIp + " and port " + port);

            JSch jschObject = new JSch();

            session = jschObject.getSession(username, hostIp, port);

            if (password != null && password.trim().length() > 0)
            {
                session.setPassword(password);
            }

            Properties config = new Properties();

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

    public static SSHConnectionUtil getNewSSHObject(String host, int port, String username, String password, int timeout)
    {
        _logger.info("SSH to hostIp - " + host);

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

                        output.append(CommonConstantUI.NEW_LINE);

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
}
