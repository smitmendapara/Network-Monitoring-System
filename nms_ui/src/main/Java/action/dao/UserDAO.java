package action.dao;

import action.util.CommonConstant;
import action.util.Logger;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by smit on 31/12/21.
 */
public class UserDAO
{
    private static final Logger _logger = new Logger();

    private static int newId;

    public static int getNewId()
    {
        return newId;
    }

    public void setNewId(int id)
    {
        this.newId = id;
    }

    static String URL = CommonConstant.DATABASE_URL;

    static String USER = "motadata";

    static String PASS = "motadata";

    static String select_query = "SELECT * FROM TB_USER WHERE USER = ? AND PASSWORD = ?";

    static String tableInsert_query = "SELECT ID, NAME, IP FROM TB_DISCOVER";

    static String insert_query = "INSERT INTO TB_USER(USER, PASSWORD) VALUES(?, ?)";

    static String result_query = "INSERT INTO TB_RESULT(ID, IP, PROFILE, DEVICETYPE) VALUES(?, ?, ?, ?)";

    static String discover_query = "INSERT INTO TB_DISCOVER(NAME, IP, USERNAME, PASSWORD, DEVICE) VALUES(?, ?, ?, ?, ?)";

    static String query = "SELECT ID, IP, PROFILE, DEVICETYPE FROM TB_RESULT";

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

    public static String getResultSet()
    {
        Statement statement = null;

        ResultSet resultSet = null;

        String string = "<tr class=\"disc__data\">" + "<td><input type=\"checkbox\"></td>" + "<td>one</td>" + "<td>0</td>" + "<td>two</td>" + "<td>three</td>" + "</tr>";

        try
        {
            Connection connection = getConnection();

            statement = connection.createStatement();

            resultSet = statement.executeQuery(query + " WHERE ID=" + UserDAO.getNewId());

            while (resultSet.next())
            {
                String string1 = resultSet.getString(2);

                String string2 = resultSet.getString(3);

                String string3 = resultSet.getString(4);


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

    public static ResultSet getTB()
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
            _logger.error("not get resultSet!", exception);
        }

        return resultSet;
    }

//    public static ResultSet getMonitorizeData(int id)
//    {
//        PreparedStatement statement = null;
//
//        ResultSet resultSet = null;
//
//        try
//        {
//            Connection connection = getConnection();
//
//            statement = connection.prepareStatement(query);
//
//            statement.setInt(1, id);
//
//            resultSet = statement.executeQuery();
//
//            return resultSet;
//        }
//        catch (Exception exception)
//        {
//            _logger.error("not get resultSet", exception);
//        }
//
//        return resultSet;
//    }

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

    public static boolean enterDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        boolean status = true;

        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement discover_statement = connection.prepareStatement(discover_query);

            PreparedStatement result_statement = connection.prepareStatement(result_query);

            if (deviceType.equals("0"))
            {
                discover_statement.setString(1, name);

                discover_statement.setString(2, ip);

                discover_statement.setString(3, CommonConstant.NULL);

                discover_statement.setString(4, CommonConstant.NULL);

                discover_statement.setString(5, "Ping");

            }
            else
            {
                discover_statement.setString(1, name);

                discover_statement.setString(2, ip);

                discover_statement.setString(3, discoveryUsername);

                discover_statement.setString(4, discoveryPassword);

                discover_statement.setString(5, "Linux");
            }

            if (enterResultTableData(name, ip, discoveryUsername, discoveryPassword, deviceType, connection))
            {
                if (discover_statement.execute() && result_statement.execute())
                {
                    return true;
                }
            }

            discover_statement.close();

            result_statement.close();


            //kernel message for ping
        }
        catch (Exception exception)
        {
            _logger.error("discovery data not inserted properly!", exception);

            status = false;
        }

        return status;
    }

    private static boolean enterResultTableData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, Connection connection)
    {
        boolean result = true;

        String Id_query = "SELECT * FROM TB_DISCOVER WHERE IP = " + "'" + ip + "'";

        try
        {
            PreparedStatement result_statement = connection.prepareStatement(result_query);

            PreparedStatement statement = connection.prepareStatement(Id_query);

            ResultSet resultSet = statement.executeQuery();

            if (deviceType.equals("0"))
            {
                result_statement.setString(1, resultSet.getString(1));

                result_statement.setString(2, ip);

                result_statement.setString(3, CommonConstant.NULL);

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
        catch (Exception exception)
        {
            _logger.error("not inserted data into result table!", exception);

            result = false;
        }
        return result;
    }

//    public static boolean enterData(ResultSet resultSet)
//    {
//        String Id;
//
//        String IP;
//
//        String Profile;
//
//        String DeviceType;
//
//        boolean status = true;
//
//        try
//        {
//            Class.forName("org.h2.Driver");
//
//            Connection connection = DriverManager.getConnection(URL, USER, PASS);
//
//            PreparedStatement statement = connection.prepareStatement(result_query);
//
//            while (resultSet.next())
//            {
//                Id = resultSet.getString(1);
//
//                statement.setString(1, Id);
//
//
//                IP = resultSet.getString(2);
//
//                statement.setString(2, IP);
//
//
//                Profile = resultSet.getString(3);
//
//                statement.setString(3, Profile);
//
//
//                DeviceType = resultSet.getString(4);
//
//                statement.setString(4, DeviceType);
//            }
//
//            statement.executeUpdate();
//
//            return status;
//        }
//        catch (Exception exception)
//        {
//            _logger.error("data not inserted in result table!", exception);
//
//            status = false;
//        }
//
//        return status;
//    }

    public static ResultSet getTableData()
    {
        Statement statement = null;

        ResultSet resultSet = null;

        Connection connection = null;

        try
        {
            connection = getConnection();

            statement = connection.createStatement();

            resultSet = statement.executeQuery(query + " WHERE Id=" + UserDAO.getNewId());

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.error("not fetch table data!", exception);
        }

//        finally
//        {
//            try
//            {
//                if (statement != null && !statement.isClosed())
//                {
//                    statement.close();
//                }
//                if (connection != null)
//                {
//                    connection.close();
//                }
//            }
//            catch (Exception exception)
//            {
//
//            }
//        }

        return resultSet;
    }
}
