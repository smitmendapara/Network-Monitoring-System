package com.motadata.kernel.dao;

import com.motadata.kernel.util.CommonConstant;

import com.motadata.kernel.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataAccess
{
    private static String DATABASE_URL = CommonConstant.DATABASE_URL;

    private static String DATABASE_USERNAME = CommonConstant.DATABASE_USERNAME;

    private static String DATABASE_PASSWORD = CommonConstant.DATABASE_PASSWORD;

    private static String selectMonitor = "SELECT * FROM TB_MONITOR WHERE IP = ? AND DEVICETYPE = ?";

    private static String insertDataDump = "INSERT INTO TB_DATADUMP(ID, IP, PACKET, MEMORY, DEVICE, CURRENTTIME, STATUS) VALUES(?, ?, ?, ?, ?, ?, ?)";

    private static String updateMonitor = "UPDATE TB_MONITOR SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ? AND DEVICETYPE = ?";

    private static String updateResult = "UPDATE TB_RESULT SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ? AND DEVICETYPE = ?";

    private static Connection connection = null;

    private static final DataAccess _dao = new DataAccess();

    private static final Logger _logger = new Logger();

    public Connection getConnection()
    {
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        }
        catch (Exception exception)
        {
            _logger.warn("connection is not ready!");
        }

        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sqlQuery)
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

    public static List<String> getMonitorArrayData(ResultSet resultSet)
    {
        List<String> monitorList = new ArrayList<>();

        try
        {
            while (resultSet.next())
            {
                monitorList.add(resultSet.getString(2));

                monitorList.add(resultSet.getString(4));

                monitorList.add(resultSet.getString(5));

                monitorList.add(resultSet.getString(6));
            }

        }
        catch (Exception exception)
        {
            _logger.warn("not get array for monitor data!");
        }

        return monitorList;
    }

    public static List<String> getReMonitorData(String ip, String deviceType)
    {
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        List<String> monitorList = new ArrayList<>();

        try
        {
            preparedStatement = getPreparedStatement(selectMonitor);

            preparedStatement.setString(1, ip);

            preparedStatement.setString(2, deviceType);

            resultSet = preparedStatement.executeQuery();

            monitorList = getMonitorArrayData(resultSet);

        }
        catch (Exception exception)
        {
            _logger.error("not find re discover table data!!", exception);
        }
        finally
        {
            closeConnection(connection);

            closePreparedStatement(preparedStatement);
        }

        return monitorList;
    }

    private static void updateData(PreparedStatement preparedStatement, String response, String ipStatus, String timestamp, String ip, String deviceType)
    {
        try
        {
            preparedStatement.setString(1, response);

            preparedStatement.setString(2, ipStatus);

            preparedStatement.setString(3, timestamp.substring(0, 16));

            preparedStatement.setString(4, ip);

            preparedStatement.setString(5, deviceType);
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

            updateData(preparedStatement, response, ipStatus, timestamp, ip, deviceType);

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
        finally
        {
            closeConnection(connection);

            closePreparedStatement(preparedStatement);
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

            updateData(preparedStatement, response, ipStatus, timestamp, ip, deviceType);

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
        finally
        {
            closeConnection(connection);

            closePreparedStatement(preparedStatement);
        }

        return result;
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
        finally
        {
            closeConnection(connection);

            closePreparedStatement(preparedStatement);
        }

        return status;
    }
}
