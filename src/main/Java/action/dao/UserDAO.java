package action.dao;

import action.util.CommonConstantUI;

import action.util.Logger;

import java.sql.*;

/**
 * Created by smit on 31/12/21.
 */
public class UserDAO
{
    static String URL = CommonConstantUI.DATABASE_URL;

    static String USER = "motadata";

    static String PASS = "motadata";

    private static int newId;

    private static String ip;

    public UserDAO()
    {

    }

    public UserDAO(int id, String ip)
    {
        this.newId = id;

        this.ip = ip;
    }

    public int getNewId()
    {
        return this.newId;
    }

    public void setNewId(int id)
    {
        this.newId = id;
    }

    private static final Logger _logger = new Logger();

    static UserDAO _dao = new UserDAO();

    static String select_query = "SELECT * FROM TB_USER WHERE USER = ? AND PASSWORD = ?";

    static String tableInsert_query = "SELECT ID, NAME, IP FROM TB_DISCOVER";

    static String re_query = "SELECT * FROM TB_DISCOVER WHERE IP = ?";

    static String insert_query = "INSERT INTO TB_USER(USER, PASSWORD) VALUES(?, ?)";

    static String result_query = "INSERT INTO TB_RESULT(ID, IP, PROFILE, DEVICETYPE) VALUES(?, ?, ?, ?)";

    static String discover_query = "INSERT INTO TB_DISCOVER(NAME, IP, USERNAME, PASSWORD, DEVICE, RESPONSE, STATUS) VALUES(?, ?, ?, ?, ?, ?, ?)";

    static String query = "SELECT ID, IP, PROFILE, DEVICETYPE FROM TB_RESULT";

    static String Id_query = "SELECT ID FROM TB_DISCOVER WHERE IP = ?";

    static String delete_query = "DELETE FROM TB_DISCOVER WHERE ID = ?";

    static String monitor_query = "INSERT INTO TB_MONITOR(ID, IP, PROFILE, DEVICETYPE) VALUES(?, ?, ?, ?)";

    static String idQuery = "SELECT * FROM TB_DISCOVER WHERE ID = ?";

    static String m_select = "SELECT ID, IP, DEVICETYPE FROM TB_MONITOR";

    static String d_update = "UPDATE TB_DISCOVER SET RESPONSE = ? WHERE IP = ?";

//    static String d_select = "SELECT * FROM TB_DISCOVER WHERE IP = ?";

    private static Connection getConnection()
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

    public static String getResultSet()
    {
        Statement statement = null;

        ResultSet resultSet = null;

        String string = "<tr class=\"disc__data\">" + "<td><input type=\"hidden\" name=\"key\" id=\"key\" value=\"zero\" >zero</td>" + "<td><input type=\"checkbox\" id=\"check\" checked></td>" + "<td >one</td>" + "<td>0</td>" + "<td>two</td>" + "<td>three</td>" + "</tr>";

        try
        {
            Connection connection = getConnection();

            statement = connection.createStatement();

            resultSet = statement.executeQuery(query + " WHERE ID=" + _dao.getNewId());

            while (resultSet.next())
            {
                String string0 = resultSet.getString(1);

                String string1 = resultSet.getString(2);

                String string2 = resultSet.getString(3);

                String string3 = resultSet.getString(4);

                string = string.replace("zero", string0);

                string = string.replace("one", string1);

                if (resultSet.getString(3) == null)
                {
                    string = string.replace("two", "");
                }
                else
                {
                    string = string.replace("two", string2);
                }

                string = string.replace("three", string3);
            }

            resultSet.close();

        }
        catch (Exception exception)
        {
            _logger.error("not get resultSet!", exception);
        }

        return string;
    }

    public static ResultSet getDiscoverTB()
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
            _logger.error("not find discover table data!!", exception);
        }

        return resultSet;
    }

    public static ResultSet getMonitorTB()
    {
        Statement statement = null;

        ResultSet resultSet = null;

        try
        {
            Connection connection = getConnection();

            statement = connection.createStatement();

            resultSet = statement.executeQuery(m_select);

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.error("not find monitor table data!", exception);
        }

        return resultSet;
    }

    public static boolean check(String username, String password)
    {
        boolean status = false;

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
                status = true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("data not get properly!", exception);
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

    public static boolean enterDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response)
    {
        boolean status = true;

        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement discover_statement = connection.prepareStatement(discover_query);

            if (deviceType.equals("0"))
            {
                discover_statement.setString(1, name);

                discover_statement.setString(2, ip);

                discover_statement.setString(3, CommonConstantUI.NULL);

                discover_statement.setString(4, CommonConstantUI.NULL);

                discover_statement.setString(5, "Ping");

                discover_statement.setString(6, response);

                discover_statement.setString(7, "true");

            }
            if (deviceType.equals("1"))
            {
                discover_statement.setString(1, name);

                discover_statement.setString(2, ip);

                discover_statement.setString(3, discoveryUsername);

                discover_statement.setString(4, discoveryPassword);

                discover_statement.setString(5, "Linux");

                discover_statement.setString(6, response);

                discover_statement.setString(7, "true");
            }

            if (discover_statement.execute())
            {
                return true;
            }

            discover_statement.close();


            //kernel message for ping
        }
        catch (Exception exception)
        {
            _logger.error("discovery data not inserted properly!", exception);

            status = false;
        }

        return status;
    }

    public static boolean enterReDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response)
    {
        boolean status = true;

        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement preparedStatement = connection.prepareStatement(d_update);

            if (deviceType.equals("Ping"))
            {
                preparedStatement.setString(1, response);

                preparedStatement.setString(2, ip);
            }
            else if (deviceType.equals("Linux"))
            {
                preparedStatement.setString(1, response);

                preparedStatement.setString(2, ip);
            }

            if (preparedStatement.execute())
            {
                return true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("discovery data not inserted properly!", exception);

            status = false;
        }

        return status;
    }

    public static boolean enterResultTableData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        boolean result = true;

        try
        {
            Connection connection = getConnection();

            PreparedStatement statement = connection.prepareStatement(Id_query);

            statement.setString(1, ip);

            ResultSet resultSet = statement.executeQuery();

            PreparedStatement result_statement = connection.prepareStatement(result_query);

            while (resultSet.next())
            {
                if (deviceType.equals("0"))
                {
                    result_statement.setString(1, resultSet.getString(1));

                    result_statement.setString(2, ip);

                    result_statement.setString(3, CommonConstantUI.NULL);

                    result_statement.setString(4, "Ping");
                }
                else
                {
                    result_statement.setString(1, resultSet.getString(1));

                    result_statement.setString(2, ip);

                    result_statement.setString(3, discoveryUsername);

                    result_statement.setString(4, "Linux");
                }
            }

            if (result_statement.execute())
            {
                return true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("not inserted data into result table!", exception);

            result = false;
        }

        return result;
    }

    public static boolean enterMonitorTableData(int id)
    {
        boolean result = true;

        try
        {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(monitor_query);

            PreparedStatement preparedStatement1 = connection.prepareStatement(idQuery);

            preparedStatement1.setInt(1, id);

            ResultSet resultSet = preparedStatement1.executeQuery();

            while (resultSet.next())
            {
                preparedStatement.setInt(1, id);

                preparedStatement.setString(2, resultSet.getString(3));

                preparedStatement.setString(3, resultSet.getString(4));

                preparedStatement.setString(4, resultSet.getString(6));
            }

            _logger.info("append into monitor table!");

            if (preparedStatement.execute())
            {
                return true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("data not inserted into tb_monitor table!", exception);

            result = false;
        }

        return result;
    }
    public static boolean deleteDiscoverTableData(int idAttribute)
    {
        boolean status = true;

        try
        {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(delete_query);

            preparedStatement.setInt(1, idAttribute); // setString(1, String.valueOf(idAttribute));

            if (preparedStatement.execute())
            {
                return true;
            }

            preparedStatement.close();
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on deleted row!", exception);

            status = false;
        }

        return status;
    }

    public static ResultSet getReDiscoveryData(String ip)
    {
        Statement statement = null;

        ResultSet resultSet = null;

        try
        {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(re_query);

            preparedStatement.setString(1, ip);

            resultSet  = preparedStatement.executeQuery();

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.error("not find re discover table data!!", exception);
        }

        return resultSet;
    }

    public static ResultSet getDashboardData()
    {
        PreparedStatement preparedStatement;

        ResultSet resultSet = null;

        try
        {
            Connection connection = getConnection();

            preparedStatement = connection.prepareStatement(re_query);

            preparedStatement.setString(1, ip);

            resultSet = preparedStatement.executeQuery();

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.error("not find re discover table data!!", exception);

            resultSet = null;
        }

        return resultSet;
    }
}

