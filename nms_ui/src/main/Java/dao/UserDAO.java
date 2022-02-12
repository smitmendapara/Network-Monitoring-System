package dao;

import util.CommonConstantUI;

import util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO
{
    private static int id;

    private static String ip;

    private static String deviceType;

    public static String getDeviceType() {
        return deviceType;
    }

    public static void setDeviceType(String deviceType) {
        UserDAO.deviceType = deviceType;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        UserDAO.ip = ip;
    }

    private static String DATABASE_URL = CommonConstantUI.DATABASE_URL;

    private static String DATABASE_USERNAME = CommonConstantUI.DATABASE_USERNAME;

    private static String DATABASE_PASSWORD = CommonConstantUI.DATABASE_PASSWORD;


    private static String selectUser = "SELECT * FROM TB_USER WHERE USER = ? AND PASSWORD = ?";

    private static String selectColumnDiscover = "SELECT ID, NAME, IP, DEVICE, USERNAME FROM TB_DISCOVER";

    private static String selectDiscoverTable = "SELECT ID, NAME, IP, DEVICE, USERNAME FROM TB_DISCOVER WHERE ID = ?";

    private static String selectIdDiscover = "SELECT ID FROM TB_DISCOVER WHERE IP = ? AND DEVICE = ?";

    private static String selectDiscover = "SELECT * FROM TB_DISCOVER WHERE IP = ? AND DEVICE = ?";

    private static String selectColumnResult = "SELECT ID, IP, PROFILE, DEVICETYPE FROM TB_RESULT";

    private static String selectColumnMonitor = "SELECT * FROM TB_MONITOR WHERE IP = ? AND DEVICETYPE = ?";

    private static String selectDiscoverData = "SELECT * FROM TB_DISCOVER WHERE ID = ?";

    private static String selectMonitorTable = "SELECT * FROM TB_MONITOR";

    private static String selectDataDump = "SELECT * FROM TB_DATADUMP WHERE ID = ? AND CURRENTTIME = ?";


    private static String insertUser = "INSERT INTO TB_USER(USER, PASSWORD) VALUES(?, ?)";

    private static String insertDiscover = "INSERT INTO TB_DISCOVER(NAME, IP, USERNAME, PASSWORD, DEVICE, RESPONSE, STATUS, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

    private static String insertResult = "INSERT INTO TB_RESULT(ID, IP, PROFILE, DEVICETYPE, RESPONSE, STATUS, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?)";

    private static String insertMonitor = "INSERT INTO TB_MONITOR(ID, NAME, IP, PROFILE, PASSWORD, DEVICETYPE, RESPONSE, STATUS, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static String insertDataDump = "INSERT INTO TB_DATADUMP(ID, IP, PACKET, MEMORY, DEVICE, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?)";


    private static String updateDiscover = "UPDATE TB_DISCOVER SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ?";

    private static String updateMonitor = "UPDATE TB_MONITOR SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ?";

    private static String updateResult = "UPDATE TB_RESULT SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ?";


    private static String deleteDiscover = "DELETE FROM TB_DISCOVER WHERE ID = ?";

    private static String idQuery = "SELECT * FROM TB_DISCOVER WHERE ID = ?";

    private static String findData = "SELECT * FROM TB_MONITOR WHERE ID = ?";

    private static Connection connection = null;

    public int getNewId()
    {
        return this.id;
    }

    public void setNewId(int id)
    {
        this.id = id;
    }

    public UserDAO()
    {

    }

    public UserDAO(int id, String ip, String deviceType)
    {
        this.id = id;

        this.ip = ip;

        this.deviceType = deviceType;
    }

    private static final Logger _logger = new Logger();

    private static final UserDAO _dao = new UserDAO();

    private Connection getConnection()
    {
        try
        {
            Class.forName("org.h2.Driver");

            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            return connection;
        }
        catch (Exception exception)
        {
            _logger.error("not connection established!", exception);
        }

        return connection;
    }

    private static PreparedStatement getPreparedStatement(String sqlQuery)
    {
        PreparedStatement preparedStatement = null;

        try
        {
            connection = _dao.getConnection();

            preparedStatement = connection.prepareStatement(sqlQuery);
        }
        catch (Exception exception)
        {
            _logger.warn("prepared statement is not ready!");
        }

        return preparedStatement;
    }

    private static Statement getStatement()
    {
        Statement statement = null;

        try
        {
            connection = _dao.getConnection();

            statement = connection.createStatement();
        }
        catch (Exception exception)
        {
            _logger.warn("statement is not ready!");
        }

        return statement;
    }

    public static void closeConnection(Connection connection)
    {
        try
        {
            if (!connection.isClosed() && connection != null)
            {
                connection.close();
            }
        }
        catch (Exception exception)
        {
            _logger.warn("connection is still not closed!");
        }
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement)
    {
        try
        {
            if (!preparedStatement.isClosed() && preparedStatement != null)
            {
                preparedStatement.close();
            }
        }
        catch (Exception exception)
        {
            _logger.warn("prepared statement is still not closed!");
        }
    }

    public static void closeStatement(Statement statement)
    {
        try
        {
            if (!statement.isClosed() && statement != null)
            {
                statement.close();
            }
        }
        catch (Exception exception)
        {
            _logger.warn("prepared statement is still not closed!");
        }
    }

    public static List<List<String>> getDiscoveryArrayData(ResultSet resultSet)
    {

        List<List<String>> discoverList = new ArrayList<>();

        List<String> list;

        try
        {
            while (resultSet.next())
            {
                list = new ArrayList<>();

                list.add(resultSet.getString(1));

                list.add(resultSet.getString(2));

                list.add(resultSet.getString(3));

                list.add(resultSet.getString(4));

                list.add(resultSet.getString(5));

                discoverList.add(list);
            }

        }
        catch (Exception exception)
        {
            _logger.warn("not get array for monitor data!");
        }

        return discoverList;
    }

    public static List<List<String>> getDiscoverTB()
    {
        Statement statement = null;

        ResultSet resultSet = null;

        List<List<String>> discoverList = new ArrayList<>();

        try
        {
            statement = getStatement();

            resultSet = statement.executeQuery(selectColumnDiscover);

            discoverList = getDiscoveryArrayData(resultSet);

        }
        catch (Exception exception)
        {
            _logger.error("not find discover table data!!", exception);
        }
        finally
        {
            closeConnection(connection);

            closeStatement(statement);

        }

        return discoverList;
    }

    public static ResultSet getMonitorTB()
    {
        ResultSet resultSet = null;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(selectDiscoverData);

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.error("not find monitor table data where id is given!", exception);
        }

        return resultSet;
    }

    public static ResultSet getMonitorTable()
    {
        ResultSet resultSet = null;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(selectMonitorTable);

            resultSet = preparedStatement.executeQuery();

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.warn("not find monitor table data!");
        }

        return resultSet;
    }

    public static boolean checkCredential(String username, String password)
    {
        boolean status = false;

        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        try
        {
            preparedStatement = getPreparedStatement(selectUser);

            preparedStatement.setString(1, username);

            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                status = true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("data not get properly!", exception);
        }

        return status;
    }

    public static boolean enterSignUpData(String username, String password)
    {
        boolean status = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(insertUser);

            preparedStatement.setString(1, username);

            preparedStatement.setString(2, password);

            if (preparedStatement.execute())
            {
                return true;
            }

            preparedStatement.close();

        }
        catch (Exception exception)
        {
            _logger.error("signUp data not inserted properly!", exception);

            status = false;
        }

        return status;
    }

    public static boolean enterDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean status = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(insertDiscover);

            if (deviceType.equals(CommonConstantUI.STRING_ZERO))
            {
                preparedStatement.setString(1, name);

                preparedStatement.setString(2, ip);

                preparedStatement.setString(3, CommonConstantUI.NULL);

                preparedStatement.setString(4, CommonConstantUI.NULL);

                preparedStatement.setString(5, "Ping");

                preparedStatement.setString(6, response);

                preparedStatement.setString(7, ipStatus);

                preparedStatement.setString(8, timestamp.substring(0, 16));

            }
            if (deviceType.equals(CommonConstantUI.STRING_ONE))
            {
                preparedStatement.setString(1, name);

                preparedStatement.setString(2, ip);

                preparedStatement.setString(3, discoveryUsername);

                preparedStatement.setString(4, discoveryPassword);

                preparedStatement.setString(5, "Linux");

                preparedStatement.setString(6, response);

                preparedStatement.setString(7, ipStatus);

                preparedStatement.setString(8, timestamp.substring(0, 16));
            }

            if (preparedStatement.execute())
            {
                return true;
            }

            preparedStatement.close();


            //kernel message for ping
        }
        catch (Exception exception)
        {
            _logger.error("discovery data not inserted properly!", exception);

            status = false;
        }

        return status;
    }

    private static void updateData(PreparedStatement preparedStatement, String response, String ipStatus, String timestamp, String ip)
    {
        try
        {
            preparedStatement.setString(1, response);

            preparedStatement.setString(2, ipStatus);

            preparedStatement.setString(3, timestamp.substring(0, 16));

            preparedStatement.setString(4, ip);
        }
        catch (Exception exception)
        {
            _logger.warn("data not updated!");
        }
    }

    public static boolean enterReMonitorData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean result = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(updateMonitor);

            updateData(preparedStatement, response, ipStatus, timestamp, ip);

            if (preparedStatement.execute())
            {
                return true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("monitor data not updated properly!", exception);

            result = false;
        }

        return result;
    }

    public static boolean enterReDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean status = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(updateDiscover);

            updateData(preparedStatement, response, ipStatus, timestamp, ip);

            if (preparedStatement.execute())
            {
                return true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("discovery data not updated properly!", exception);

            status = false;
        }

        return status;
    }

    public static boolean enterResultTableData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean result = true;

        PreparedStatement preparedStatement = null;

        PreparedStatement result_statement = null;

        ResultSet resultSet = null;

        try
        {
            preparedStatement = getPreparedStatement(selectIdDiscover);

            preparedStatement.setString(1, ip);

            if (deviceType.equals(CommonConstantUI.STRING_ZERO))
            {
                deviceType = CommonConstantUI.PING_DEVICE;
            }
            else
            {
                deviceType = CommonConstantUI.LINUX_DEVICE;
            }

            preparedStatement.setString(2, deviceType);

            resultSet = preparedStatement.executeQuery();

            result_statement = getPreparedStatement(insertResult);

            while (resultSet.next())
            {
                if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
                {
                    result_statement.setString(1, resultSet.getString(1));

                    result_statement.setString(2, ip);

                    result_statement.setString(3, CommonConstantUI.NULL);

                    result_statement.setString(4, "Ping");

                    result_statement.setString(5, response);

                    result_statement.setString(6, ipStatus);

                    result_statement.setString(7, timestamp.substring(0, 16));
                }
                else
                {
                    result_statement.setString(1, resultSet.getString(1));

                    result_statement.setString(2, ip);

                    result_statement.setString(3, discoveryUsername);

                    result_statement.setString(4, "Linux");

                    result_statement.setString(5, response);

                    result_statement.setString(6, ipStatus);

                    result_statement.setString(7, timestamp.substring(0, 16));
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

    public static boolean enterReResultTableData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean result = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(updateResult);

            updateData(preparedStatement, response, ipStatus, timestamp, ip);

            if (preparedStatement.execute())
            {
                return true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("not updated data into result table!", exception);

            result = false;
        }

        return result;
    }

    public static boolean enterMonitorTableData(int id)
    {
        boolean result = true;

        Timestamp timestamp = null;

        PreparedStatement preparedStatement = null;

        PreparedStatement preparedStatement1 = null;

        PreparedStatement preparedStatement2 = null;

        ResultSet resultSet1 = null;

        ResultSet resultSet = null;

        try
        {
            preparedStatement2 = getPreparedStatement(findData);

            preparedStatement2.setInt(1, id);

            resultSet1 = preparedStatement2.executeQuery();

            if (resultSet1.next())
            {
                return false;
            }

            preparedStatement = getPreparedStatement(insertMonitor);

            preparedStatement1 = getPreparedStatement(idQuery);

            preparedStatement1.setInt(1, id);

            resultSet = preparedStatement1.executeQuery();

            timestamp = new Timestamp(System.currentTimeMillis());

            while (resultSet.next())
            {
                preparedStatement.setInt(1, id);

                preparedStatement.setString(2, resultSet.getString(2));

                preparedStatement.setString(3, resultSet.getString(3));

                preparedStatement.setString(4, resultSet.getString(4));

                preparedStatement.setString(5, resultSet.getString(5));

                preparedStatement.setString(6, resultSet.getString(6));

                preparedStatement.setString(7, resultSet.getString(7));

                preparedStatement.setString(8, resultSet.getString(8));

                preparedStatement.setString(9, timestamp.toString().substring(0, 16));
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

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(deleteDiscover);

            preparedStatement.setInt(1, idAttribute);

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

    public static ResultSet getReDiscoveryData(String ip, String deviceType)
    {
        ResultSet resultSet = null;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(selectDiscover);

            preparedStatement.setString(1, ip);

            preparedStatement.setString(2, deviceType);

            resultSet  = preparedStatement.executeQuery();

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.error("not find re discover table data!!", exception);
        }

        return resultSet;
    }

    public static ResultSet getReMonitorData(String ip, String deviceType)
    {
        ResultSet resultSet = null;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(selectColumnMonitor);

            preparedStatement.setString(1, ip);

            preparedStatement.setString(2, deviceType);

            resultSet  = preparedStatement.executeQuery();

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.error("not find re monitor table data!!", exception);
        }

        return resultSet;
    }

    public static ResultSet getDashboardData()
    {
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        try
        {
            preparedStatement = getPreparedStatement(selectColumnMonitor);

            preparedStatement.setString(1, ip);

            preparedStatement.setString(2, deviceType);

            resultSet = preparedStatement.executeQuery();

            return resultSet;
        }
        catch (Exception exception)
        {
            _logger.error("not find dashboard data!!", exception);

            resultSet = null;
        }

        return resultSet;
    }

    public static String getUpdatedPacket(int id, String ip, String time)
    {
        String packet = null;

        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        try
        {
            preparedStatement = getPreparedStatement(selectDataDump);

            preparedStatement.setInt(1, id);

            preparedStatement.setString(2, time);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                packet = resultSet.getString(3);
            }

        }
        catch (Exception exception)
        {
            _logger.warn("error on getting updated packet!");
        }

        return packet;
    }

    public static Double getUpdatedMemory(int id, String ip, String time)
    {
        Double memoryPercent = null;

        PreparedStatement preparedStatement = null;

        ResultSet resultSet;

        try
        {
            preparedStatement = getPreparedStatement(selectDataDump);

            preparedStatement.setInt(1, id);

            preparedStatement.setString(2, time);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                memoryPercent = resultSet.getDouble(4);
            }

        }
        catch (Exception exception)
        {
            _logger.warn("error on getting updated memory!");
        }

        return memoryPercent;
    }

    public static boolean enterDataDump(int id, String ip, String packet, Double memory, String deviceType, String time, String ipStatus)
    {
        boolean status = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement(insertDataDump);

            preparedStatement.setInt(1, id);

            preparedStatement.setString(2, ip);

            preparedStatement.setString(3, packet);

            preparedStatement.setDouble(4, memory);

            preparedStatement.setString(5, deviceType);

            preparedStatement.setString(6, time.substring(0, 16));

            preparedStatement.setString(7, ipStatus);

            if (preparedStatement.execute())
            {
                return true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("not inserted data into tb_dataDump!", exception);

            status = false;
        }

        return status;
    }
}

