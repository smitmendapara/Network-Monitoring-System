package dao;

import util.CommonConstantUI;

import util.Logger;

import java.sql.*;

import java.util.ArrayList;

import java.util.List;

public class UserDAO
{
    private int id;

    private String ip;

    private String deviceType;

    private boolean verifyCredential;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getNewId()
    {
        return this.id;
    }

    public void setNewId(int id)
    {
        this.id = id;
    }

    private Connection connection = null;

    private static final Logger _logger = new Logger();

    private static final UserDAO _dao = new UserDAO();

    private Connection getConnection()
    {
        String DATABASE_URL = CommonConstantUI.DATABASE_URL;

        String DATABASE_USERNAME = CommonConstantUI.DATABASE_USERNAME;

        String DATABASE_PASSWORD = CommonConstantUI.DATABASE_PASSWORD;

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

    private PreparedStatement getPreparedStatement(String sqlQuery)
    {
        PreparedStatement preparedStatement = null;

        try
        {
            connection = _dao.getConnection();

            preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.executeQuery();


        }
        catch (Exception exception)
        {
            _logger.warn("prepared statement is not ready!");
        }

        return preparedStatement;
    }

    private static void closeConnection(Connection connection)
    {
        try
        {
            if (!connection.isClosed())
            {
                connection.close();
            }
        }
        catch (Exception exception)
        {
            _logger.warn("connection is still not closed!");
        }
    }

    private static void closePreparedStatement(PreparedStatement preparedStatement)
    {
        try
        {
            if (!preparedStatement.isClosed())
            {
                preparedStatement.close();
            }
        }
        catch (Exception exception)
        {
            _logger.warn("prepared statement is still not closed!");
        }
    }

    private static List<List<String>> getDiscoveryArrayData(ResultSet resultSet)
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
            _logger.warn("not get array for discovery data!");
        }

        return discoverList;
    }

    private static List<List<String>> getArrayData(ResultSet resultSet)
    {
        List<List<String>> parentList = new ArrayList<>();

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

                list.add(resultSet.getString(6));

                list.add(resultSet.getString(7));

                list.add(resultSet.getString(8));

                list.add(resultSet.getString(9));

                parentList.add(list);
            }

        }
        catch (Exception exception)
        {
            _logger.warn("not get array for monitor data!");
        }

        return parentList;
    }

    private List<List<String>> getData(String sqlQuery, String operation, String tableName, List<Object> condition)
    {
        ResultSet resultSet;

        PreparedStatement preparedStatement;

        List<List<String>> dataList = new ArrayList<>();

        try
        {
            connection = _dao.getConnection();

            preparedStatement = connection.prepareStatement(sqlQuery);

            if (operation.equals("SELECT") && tableName.equals("TB_USER"))
            {
                preparedStatement.setString(1, (String) condition.get(0));

                preparedStatement.setString(2, (String) condition.get(1));

                verifyCredential = preparedStatement.execute();
            }

            if (operation.equals("SELECT") && tableName.equals("TB_DISCOVER"))
            {
                resultSet = preparedStatement.executeQuery();

                dataList = getDiscoveryArrayData(resultSet);
            }

            if (operation.equals("SELECT") && tableName.equals("TB_MONITOR"))
            {

                if (condition.size() > 0)
                {
                    if (condition.get(0) instanceof Integer)
                    {
                        preparedStatement.setInt(1, (Integer) condition.get(0));
                    }
                    else
                    {
                        for (int i = 0; i < condition.size(); i++)
                        {
                            preparedStatement.setString(1, (String) condition.get(i));
                        }
                    }

                    resultSet = preparedStatement.executeQuery();

                    dataList = getArrayData(resultSet);
                }
                else if (condition == null)
                {
                    resultSet = preparedStatement.executeQuery();

                    dataList = getArrayData(resultSet);
                }
            }

        }
        catch (Exception exception)
        {
            _logger.warn("error on getting data from database!");
        }

        finally
        {
            try
            {
                if (connection != null && !connection.isClosed())
                {
                    connection.close();
                }
            }
            catch (Exception exception)
            {
                _logger.warn("connection is not closed!");
            }
        }

        return dataList;
    }

    public List<List<String>> getDiscoverTB()
    {
        List<List<String>> discoverList = new ArrayList<>();

        try
        {
            String tableName = "TB_DISCOVER";

            discoverList = getData("SELECT ID, NAME, IP, DEVICE, USERNAME FROM TB_DISCOVER", "SELECT", tableName, null);

        }
        catch (Exception exception)
        {
            _logger.error("not find discover table data!!", exception);
        }

        return discoverList;
    }

    public List<List<String>> getMonitorTB()
    {
        List<List<String>> monitorList = new ArrayList<>();

        List<Object> conditionList = new ArrayList<>();

        try
        {
            String tableName = "TB_MONITOR";

            conditionList.add(id);

            monitorList = getData("SELECT * FROM TB_DISCOVER WHERE ID = ?", "SELECT", tableName, conditionList);
        }
        catch (Exception exception)
        {
            _logger.error("not find monitor table data where id is given!", exception);
        }

        return monitorList;
    }

    public List<List<String>> getMonitorTable()
    {
        List<List<String>> monitorList = new ArrayList<>();

        try
        {
            monitorList = getData("SELECT * FROM TB_MONITOR", "SELECT", "TB_MONITOR", null);
        }
        catch (Exception exception)
        {
            _logger.warn("not find monitor table data!");
        }

        return monitorList;
    }

    public boolean checkCredential(String username, String password)
    {
        boolean status = false;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(username);

            conditionList.add(password);

            getData("SELECT * FROM TB_USER WHERE USER = ? AND PASSWORD = ?", "SELECT", "TB_USER", conditionList);

            if (verifyCredential)
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

    public boolean enterSignUpData(String username, String password)
    {
        boolean status = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement("INSERT INTO TB_USER(USER, PASSWORD) VALUES(?, ?)");

            preparedStatement.setString(1, username);

            preparedStatement.setString(2, password);

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1)
            {
                return true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("signUp data not inserted properly!", exception);

            status = false;
        }

        finally
        {
            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return status;
    }

    public boolean enterDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean status = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement("INSERT INTO TB_DISCOVER(NAME, IP, USERNAME, PASSWORD, DEVICE, RESPONSE, STATUS, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

            if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
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
            if (deviceType.equals(CommonConstantUI.STRING_ONE) || deviceType.equals(CommonConstantUI.LINUX_DEVICE))
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

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1)
            {
                return true;
            }

            //kernel message for ping
        }
        catch (Exception exception)
        {
            _logger.error("discovery data not inserted properly!", exception);

            status = false;
        }

        finally
        {
            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return status;
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

    public boolean enterReMonitorData(String ip, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean result = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement("UPDATE TB_MONITOR SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ? AND DEVICETYPE = ?");

            updateData(preparedStatement, response, ipStatus, timestamp, ip, deviceType);

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1)
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
            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return result;
    }

    public boolean enterReDiscoveryData(String ip, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean status = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement("UPDATE TB_DISCOVER SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ? AND DEVICE = ?");

            updateData(preparedStatement, response, ipStatus, timestamp, ip, deviceType);

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1)
            {
                return true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("discovery data not updated properly!", exception);

            status = false;
        }

        finally
        {
            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return status;
    }

    public boolean enterResultTableData(String ip, String discoveryUsername, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean result = true;

        ResultSet resultSet;

        PreparedStatement preparedStatement = null;

        PreparedStatement result_statement = null;
        try
        {
            preparedStatement = getPreparedStatement("SELECT ID FROM TB_DISCOVER WHERE IP = ? AND DEVICE = ?");

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

            result_statement = getPreparedStatement("INSERT INTO TB_RESULT(ID, IP, PROFILE, DEVICETYPE, RESPONSE, STATUS, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?)");

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

            int affectedRow = result_statement.executeUpdate();

            if (affectedRow == 1)
            {
                return true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("not inserted data into result table!", exception);

            result = false;
        }

        finally
        {
            closePreparedStatement(result_statement);

            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return result;
    }

    public boolean enterReResultTableData(String ip, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean result = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement("UPDATE TB_RESULT SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ? AND DEVICETYPE = ?");

            updateData(preparedStatement, response, ipStatus, timestamp, ip, deviceType);

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1)
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
            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return result;
    }

    public boolean checkIpMonitor(int id)
    {
        boolean status = true;

        PreparedStatement preparedStatement = null;

        ResultSet resultSet;

        try
        {
            preparedStatement = getPreparedStatement("SELECT * FROM TB_MONITOR WHERE ID = ?");

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                status = false;
            }
        }
        catch (Exception exception)
        {
            _logger.error("checking monitor ip is already exist or not!", exception);
        }

        finally
        {
            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return status;
    }

    public boolean enterMonitorTableData(int id)
    {
        boolean result = true;

        Timestamp timestamp;

        ResultSet resultSet;

        PreparedStatement preparedStatement = null;

        PreparedStatement preparedStatement1 = null;

        try
        {
            preparedStatement = getPreparedStatement("INSERT INTO TB_MONITOR(ID, NAME, IP, PROFILE, PASSWORD, DEVICETYPE, RESPONSE, STATUS, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");

            preparedStatement1 = getPreparedStatement("SELECT * FROM TB_DISCOVER WHERE ID = ?");

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

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1)
            {
                return true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("data not inserted into tb_monitor table!", exception);

            result = false;
        }

        finally
        {
            closePreparedStatement(preparedStatement1);

            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return result;
    }

    public boolean deleteDiscoverTableData(int id)
    {
        boolean status = true;

        PreparedStatement preparedStatement = null;

        try
        {
            preparedStatement = getPreparedStatement("DELETE FROM TB_DISCOVER WHERE ID = ?");

            preparedStatement.setInt(1, id);

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1)
            {
                return true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on deleted row!", exception);

            status = false;
        }

        finally
        {
            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return status;
    }

    private static ResultSet setDataCondition(PreparedStatement preparedStatement, String ip, String deviceType)
    {
        ResultSet resultSet = null;

        try
        {
            preparedStatement.setString(1, ip);

            preparedStatement.setString(2, deviceType);

            resultSet  = preparedStatement.executeQuery();
        }
        catch (Exception exception)
        {
            _logger.warn("not get the result set!");
        }

        return resultSet;
    }

    public List<List<String>> getReDiscoveryData(String ip, String deviceType)
    {
        ResultSet resultSet;

        PreparedStatement preparedStatement;

        List<List<String>> rediscoveryList = new ArrayList<>();

        try
        {
            preparedStatement = getPreparedStatement("SELECT * FROM TB_DISCOVER WHERE IP = ? AND DEVICE = ?");

            resultSet = setDataCondition(preparedStatement, ip, deviceType);

            rediscoveryList = getArrayData(resultSet);
        }
        catch (Exception exception)
        {
            _logger.error("not find re discover table data!!", exception);
        }

        return rediscoveryList;
    }

    public List<List<String>> getReMonitorData(String ip, String deviceType)
    {
        ResultSet resultSet;

        PreparedStatement preparedStatement = null;

        List<List<String>> reMonitorList = new ArrayList<>();

        try
        {
            preparedStatement = getPreparedStatement("SELECT * FROM TB_MONITOR WHERE IP = ? AND DEVICETYPE = ?");

            resultSet = setDataCondition(preparedStatement, ip, deviceType);

            reMonitorList = getArrayData(resultSet);
        }
        catch (Exception exception)
        {
            _logger.error("not find re monitor table data!!", exception);
        }

        finally
        {
            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return reMonitorList;
    }

    public List<List<String>> getDashboardData()
    {
        PreparedStatement preparedStatement = null;

        ResultSet resultSet;

        List<List<String>> reMonitorList = new ArrayList<>();

        try
        {
            preparedStatement = getPreparedStatement("SELECT * FROM TB_MONITOR WHERE IP = ? AND DEVICETYPE = ?");

            resultSet = setDataCondition(preparedStatement, ip, deviceType);

            reMonitorList = getArrayData(resultSet);
        }
        catch (Exception exception)
        {
            _logger.error("not find dashboard data!!", exception);
        }

        finally
        {
            closePreparedStatement(preparedStatement);

            closeConnection(connection);
        }

        return reMonitorList;
    }

    public String getUpdatedPacket(int id, String ip, String time, String deviceType)
    {
        String packet = null;

        PreparedStatement preparedStatement;

        ResultSet resultSet;

        try
        {
            preparedStatement = getPreparedStatement("SELECT * FROM TB_DATADUMP WHERE ID = ? AND IP = ? AND DEVICE = ? AND CURRENTTIME = ?");

            preparedStatement.setInt(1, id);

            preparedStatement.setString(2, ip);

            preparedStatement.setString(3, deviceType);

            preparedStatement.setString(4, time);

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

    public Double getUpdatedMemory(int id, String ip, String time, String deviceType)
    {
        Double memoryPercent = null;

        PreparedStatement preparedStatement;

        ResultSet resultSet;

        try
        {
            preparedStatement = getPreparedStatement("SELECT * FROM TB_DATADUMP WHERE ID = ? AND IP = ? AND DEVICE = ? AND CURRENTTIME = ?");

            preparedStatement.setInt(1, id);

            preparedStatement.setString(2, ip);

            preparedStatement.setString(3, deviceType);

            preparedStatement.setString(4, time);

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

    public boolean enterDataDump(int id, String ip, String packet, Double memory, String deviceType, String time, String ipStatus)
    {
        boolean status = true;

        PreparedStatement preparedStatement;

        try
        {
            preparedStatement = getPreparedStatement("INSERT INTO TB_DATADUMP(ID, IP, PACKET, MEMORY, DEVICE, CURRENTTIME, STATUS) VALUES(?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setInt(1, id);

            preparedStatement.setString(2, ip);

            preparedStatement.setString(3, packet);

            preparedStatement.setDouble(4, memory);

            preparedStatement.setString(5, deviceType);

            preparedStatement.setString(6, time.substring(0, 16));

            preparedStatement.setString(7, ipStatus);

            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1)
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

