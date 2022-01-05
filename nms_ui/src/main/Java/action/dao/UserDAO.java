package action.dao;

import action.util.CommonConstant;
import action.util.Logger;

import java.sql.*;

/**
 * Created by smit on 31/12/21.
 */
public class UserDAO
{
    private static final Logger _logger = new Logger();

    static String URL = CommonConstant.DATABASE_URL;

    static String USER = "motadata";

    static String PASS = "motadata";

    static String select_query = "SELECT * FROM TB_USER WHERE USER = ? AND PASSWORD = ?";

    static String tableInsert_query = "SELECT NAME, IP FROM TB_DISCOVER";

    static String insert_query = "INSERT INTO TB_USER(USER, PASSWORD) VALUES(?, ?)";

    static String discover_query = "INSERT INTO TB_DISCOVER(NAME, IP, USERNAME, PASSWORD, DEVICE) VALUES(?, ?, ?, ?, ?)";

    public static Connection getConnection()
    {
        Connection connection = null;

        try
        {
            Class.forName("org.h2.Driver");

            connection = DriverManager.getConnection(URL, USER, PASS);

            return connection;
        }
        catch (Exception exception)
        {
            _logger.error("not connection established!", exception);
        }

        return connection;
    }

    public static ResultSet getResultSet()
    {
        Statement statement = null;

        ResultSet resultSet = null;

        try
        {
            Connection connection = getConnection();

            statement = connection.createStatement();

            resultSet = statement.executeQuery(tableInsert_query);

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.error("not connection established!", exception);
        }
        finally
        {
            try
            {
                if (statement != null && !statement.isClosed())
                {
                    statement.close();
                }
            }
            catch (Exception ignored)
            {

            }
        }

        return resultSet;
    }

    public static boolean check(String username, String password)
    {
        boolean status = true;

        PreparedStatement statement = null;

        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            statement = connection.prepareStatement(select_query);

            statement.setString(1, username);

            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                return true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("data not get properly!", exception);

            status = false;
        }
        finally
        {
            try
            {
                if (statement != null && !statement.isClosed())
                {
                    statement.close();
                }
            }
            catch (Exception ignored)
            {

            }
        }
        return status;
    }

    public static boolean enterSignUpData(String username, String password)
    {
        boolean status = true;

        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement statement = connection.prepareStatement(insert_query);

            statement.setString(1, username);

            statement.setString(2, password);

            if (statement.execute())
            {
                return true;
            }

            statement.close();

        }
        catch (Exception exception)
        {
            _logger.error("signUp data not inserted properly!", exception);

            status = false;
        }
        return status;
    }

    public static boolean enterDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        boolean status = true;

        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement statement = connection.prepareStatement(discover_query);

            if (deviceType.equals("0"))
            {
                statement.setString(1, name);

                statement.setString(2, ip);

                statement.setString(3, CommonConstant.NULL);

                statement.setString(4, CommonConstant.NULL);

                statement.setString(5, "ping");
            }
            else
            {
                statement.setString(1, name);

                statement.setString(2, ip);

                statement.setString(3, discoveryUsername);

                statement.setString(4, discoveryPassword);

                statement.setString(5, "linux");
            }

            if (statement.execute())
            {
                return true;
            }

            statement.close();
        }
        catch (Exception exception)
        {
            _logger.error("discovery data not inserted properly!", exception);

            status = false;
        }

        return status;
    }
}
