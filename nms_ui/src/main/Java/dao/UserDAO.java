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

    private boolean databaseAffected;

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

    private List<List<String>> resultTableData;

    private List<List<String>> discoverTableData;

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

    private List<List<String>> getDiscoveryArrayData(ResultSet resultSet)
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

    private List<List<String>> getArrayData(ResultSet resultSet, String tableName)
    {
        List<List<String>> parentList = new ArrayList<>();

        List<String> list;

        try
        {
            if (resultSet != null && !tableName.equals("TB_DATADUMP"))
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
            else
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

                    parentList.add(list);
                }
            }

        }
        catch (Exception exception)
        {
            _logger.warn("not get array for monitor data!");
        }

        return parentList;
    }

    private List<List<String>> getResultTableArrayData(ResultSet resultSet, List<Object> condition)
    {
        List<List<String>> parentList = new ArrayList<>();

        List<String> list;

        try
        {
            while (resultSet.next())
            {
                list = new ArrayList<>();

                list.add(resultSet.getString(1));

                list.add((String) condition.get(0));

                list.add((String) condition.get(2));

                list.add((String) condition.get(3));

                list.add((String) condition.get(4));

                list.add((String) condition.get(5));

                list.add((String) condition.get(6));

                parentList.add(list);
            }
        }
        catch (Exception exception)
        {
            _logger.warn("some thng went wrong on creating result table array list!");
        }

        return parentList;
    }

    private List<List<String>> getDiscoverTableData(ResultSet resultSet, List<Object> condition)
    {
        List<List<String>> parentList = new ArrayList<>();

        List<String> list;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try
        {
            while (resultSet.next())
            {
                list = new ArrayList<>();

                list.add(String.valueOf(condition.get(0)));

                list.add(resultSet.getString(2));

                list.add(resultSet.getString(3));

                list.add(resultSet.getString(4));

                list.add(resultSet.getString(5));

                list.add(resultSet.getString(6));

                list.add(resultSet.getString(7));

                list.add(resultSet.getString(8));

                list.add(timestamp.toString().substring(0, 16));

                parentList.add(list);
            }
        }
        catch (Exception exception)
        {
            _logger.warn("some thing went wrong on getting discover table data!");
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

            if (operation.equals(CommonConstantUI.DB_SELECT) && tableName.equals(CommonConstantUI.DB_TB_USER))
            {
                for (int i = 0; i < condition.size(); i++)
                {
                    preparedStatement.setString(i + 1, (String) condition.get(i));
                }

                verifyCredential = preparedStatement.execute();
            }

            if (operation.equals(CommonConstantUI.DB_INSERT) && tableName.equals(CommonConstantUI.DB_TB_USER))
            {
                for (int i = 0; i < condition.size(); i++)
                {
                    preparedStatement.setString(i + 1, (String) condition.get(i));
                }

                databaseAffected = affectedRow(preparedStatement);
            }

            if (operation.equals(CommonConstantUI.DB_SELECT) && tableName.equals(CommonConstantUI.DB_TB_DISCOVER))
            {
                if (condition != null)
                {
                    if (condition.size() == 1)
                    {
                        preparedStatement.setInt(1, (Integer) condition.get(0));

                        resultSet = preparedStatement.executeQuery();

                        discoverTableData = getDiscoverTableData(resultSet, condition);
                    }
                    else
                    {
                        preparedStatement.setString(1, (String) condition.get(0));

                        preparedStatement.setString(2, (String) condition.get(1));

                        resultSet = preparedStatement.executeQuery();

                        if (condition.get(condition.size() - 1) instanceof Boolean)
                        {
                            resultTableData = getResultTableArrayData(resultSet, condition);

                        }
                        else
                        {
                            dataList = getArrayData(resultSet, tableName);
                        }
                    }
                }
                else
                {
                    resultSet = preparedStatement.executeQuery();

                    dataList = getDiscoveryArrayData(resultSet);
                }
            }

            if (operation.equals(CommonConstantUI.DB_INSERT) && tableName.equals(CommonConstantUI.DB_TB_DISCOVER))
            {
                for (int i = 0; i < condition.size(); i++)
                {
                    preparedStatement.setString(i + 1, (String) condition.get(i));
                }

                databaseAffected = affectedRow(preparedStatement);
            }

            if (operation.equals(CommonConstantUI.DB_INSERT) && tableName.equals(CommonConstantUI.DB_TB_MONITOR))
            {
                for (List<String> list : discoverTableData)
                {
                    for (int i = 0; i < list.size(); i++)
                    {
                        preparedStatement.setString(i + 1, list.get(i));
                    }
                }

                databaseAffected = affectedRow(preparedStatement);
            }

            if (operation.equals(CommonConstantUI.DB_SELECT) && tableName.equals(CommonConstantUI.DB_TB_MONITOR))
            {
                if (condition != null)
                {
                    if (condition.get(0) instanceof Integer)
                    {
                        preparedStatement.setInt(1, (Integer) condition.get(0));
                    }
                    else
                    {
                        for (int i = 0; i < condition.size(); i++)
                        {
                            preparedStatement.setString(i + 1, (String) condition.get(i));
                        }
                    }

                    resultSet = preparedStatement.executeQuery();

                    dataList = getArrayData(resultSet, tableName);
                }
                else
                {
                    resultSet = preparedStatement.executeQuery();

                    dataList = getArrayData(resultSet, tableName);
                }
            }

            if (operation.equals(CommonConstantUI.DB_UPDATE))
            {
                if (tableName.equals(CommonConstantUI.DB_TB_DISCOVER) || tableName.equals(CommonConstantUI.DB_TB_MONITOR) || tableName.equals(CommonConstantUI.DB_TB_RESULT))
                {
                    updateData(preparedStatement, condition.get(0).toString(), condition.get(1).toString(), condition.get(2).toString(), condition.get(3).toString(), condition.get(4).toString());

                    databaseAffected = affectedRow(preparedStatement);
                }

            }

            if (operation.equals(CommonConstantUI.DB_INSERT) && tableName.equals(CommonConstantUI.DB_TB_RESULT))
            {
                for (List<String> list : resultTableData)
                {
                    for (int i = 0; i < list.size(); i++)
                    {
                        preparedStatement.setString(i + 1, list.get(i));
                    }
                }

                databaseAffected = affectedRow(preparedStatement);
            }

            if (tableName.equals(CommonConstantUI.DB_TB_DATADUMP))
            {
                if (operation.equals(CommonConstantUI.DB_SELECT))
                {
                    for (int i = 0; i < condition.size(); i++)
                    {
                        if (i == 0)
                        {
                            preparedStatement.setInt(i + 1, (Integer) condition.get(i));
                        }
                        else
                        {
                            preparedStatement.setString(i + 1, (String) condition.get(i));
                        }
                    }

                    resultSet = preparedStatement.executeQuery();

                    dataList = getArrayData(resultSet, tableName);
                }

                if (operation.equals(CommonConstantUI.DB_INSERT))
                {
                    for (int i = 0; i < condition.size(); i++)
                    {
                        if (i == 0)
                        {
                            preparedStatement.setInt(i + 1, (Integer) condition.get(i));
                        }
                        else if (i == 3)
                        {
                            preparedStatement.setDouble(i + 1, (Double) condition.get(i));
                        }
                        else
                        {
                            preparedStatement.setString(i + 1, (String) condition.get(i));
                        }
                    }

                    databaseAffected = affectedRow(preparedStatement);
                }
            }

            if (operation.equals(CommonConstantUI.DB_DELETE))
            {
                if (tableName.equals(CommonConstantUI.DB_TB_DISCOVER) || tableName.equals(CommonConstantUI.DB_TB_RESULT))
                {
                    preparedStatement.setInt(1, (Integer) condition.get(0));

                    databaseAffected = affectedRow(preparedStatement);
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

    private boolean affectedRow(PreparedStatement preparedStatement)
    {
        databaseAffected = false;

        try
        {
            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1)
            {
                databaseAffected = true;
            }
        }
        catch (Exception exception)
        {
            _logger.warn("row not affected!");
        }

        return databaseAffected;
    }

    public List<List<String>> getDiscoverTB()
    {
        List<List<String>> discoverList = new ArrayList<>();

        try
        {
            discoverList = getData("SELECT ID, NAME, IP, DEVICE, USERNAME FROM TB_DISCOVER", "SELECT", "TB_DISCOVER", null);

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
        boolean status = false;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(username);

            conditionList.add(password);

            getData("INSERT INTO TB_USER(USER, PASSWORD) VALUES(?, ?)", "INSERT", "TB_USER", conditionList);

            if (databaseAffected)
            {
                status = true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("signUp data not inserted properly!", exception);
        }

        return status;
    }

    public boolean enterDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean status = true;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            if (deviceType.equals(CommonConstantUI.STRING_ZERO) || deviceType.equals(CommonConstantUI.PING_DEVICE))
            {
                conditionList.add(name);

                conditionList.add(ip);

                conditionList.add(CommonConstantUI.NULL);

                conditionList.add(CommonConstantUI.NULL);

                conditionList.add(CommonConstantUI.PING_DEVICE);

                conditionList.add(response);

                conditionList.add(ipStatus);

                conditionList.add(timestamp.substring(0, 16));
            }

            if (deviceType.equals(CommonConstantUI.STRING_ONE) || deviceType.equals(CommonConstantUI.LINUX_DEVICE))
            {
                conditionList.add(name);

                conditionList.add(ip);

                conditionList.add(discoveryUsername);

                conditionList.add(discoveryPassword);

                conditionList.add(CommonConstantUI.LINUX_DEVICE);

                conditionList.add(response);

                conditionList.add(ipStatus);

                conditionList.add(timestamp.substring(0, 16));
            }

            getData("INSERT INTO TB_DISCOVER(NAME, IP, USERNAME, PASSWORD, DEVICE, RESPONSE, STATUS, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?)", "INSERT", "TB_DISCOVER", conditionList);

            if (databaseAffected)
            {
                status = true;
            }

            //kernel message for ping
        }
        catch (Exception exception)
        {
            _logger.error("discovery data not inserted properly!", exception);

            status = false;
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

    public boolean enterReDiscoveryData(String ip, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean status = false;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(response);

            conditionList.add(ipStatus);

            conditionList.add(timestamp.substring(0, 16));

            conditionList.add(ip);

            conditionList.add(deviceType);

            getData("UPDATE TB_DISCOVER SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ? AND DEVICE = ?", "UPDATE", "TB_DISCOVER", conditionList);

            if (databaseAffected)
            {
                status = true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("discovery data not updated properly!", exception);
        }

        return status;
    }

    public boolean enterResultTableData(String ip, String discoveryUsername, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean result = false;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            if (deviceType.equals(CommonConstantUI.STRING_ZERO))
            {
                deviceType = CommonConstantUI.PING_DEVICE;
            }
            else
            {
                deviceType = CommonConstantUI.LINUX_DEVICE;
            }

            conditionList.add(ip);

            conditionList.add(deviceType);

            conditionList.add(CommonConstantUI.NULL);

            conditionList.add(CommonConstantUI.PING_DEVICE);

            conditionList.add(response);

            conditionList.add(ipStatus);

            conditionList.add(timestamp.substring(0, 16));

            conditionList.add(true);

            getData("SELECT * FROM TB_DISCOVER WHERE IP = ? AND DEVICE = ?", "SELECT", "TB_DISCOVER", conditionList);

            getData("INSERT INTO TB_RESULT(ID, IP, PROFILE, DEVICETYPE, RESPONSE, STATUS, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?)", "INSERT", "TB_RESULT", null);

            if (databaseAffected)
            {
                result = true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("not inserted data into result table!", exception);
        }

        return result;
    }

    public boolean enterReResultTableData(String ip, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean status = false;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(response);

            conditionList.add(ipStatus);

            conditionList.add(timestamp.substring(0, 16));

            conditionList.add(ip);

            conditionList.add(deviceType);

            getData("UPDATE TB_RESULT SET RESPONSE = ?, STATUS = ?, CURRENTTIME = ? WHERE IP = ? AND DEVICE = ?", "UPDATE", "TB_DISCOVER", conditionList);

            if (databaseAffected)
            {
                status = true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("result table data not updated properly!", exception);
        }

        return status;
    }

    public boolean checkIpMonitor(int id)
    {
        boolean status = false;

        List<Object> conditionList = new ArrayList<>();

        List<List<String>> ipList;

        try
        {
            conditionList.add(id);

            ipList = getData("SELECT * FROM TB_MONITOR WHERE ID = ?", "SELECT", "TB_MONITOR", conditionList);

            if (ipList.size() == 0)
            {
                status = true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("checking monitor ip is already exist or not!", exception);
        }

        return status;
    }

    public boolean enterMonitorTableData(int id)
    {
        boolean result = false;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(id);

            getData("SELECT * FROM TB_DISCOVER WHERE ID = ?", "SELECT", "TB_DISCOVER", conditionList);

            getData("INSERT INTO TB_MONITOR(ID, NAME, IP, PROFILE, PASSWORD, DEVICETYPE, RESPONSE, STATUS, CURRENTTIME) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", "INSERT", "TB_MONITOR", null);

            if (databaseAffected)
            {
                result = true;
            }

        }
        catch (Exception exception)
        {
            _logger.error("data not inserted into tb_monitor table!", exception);
        }

        return result;
    }

    public boolean deleteDiscoverTableData(int id)
    {
        boolean status = false;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(id);

            getData("DELETE FROM TB_DISCOVER WHERE ID = ?", "DELETE", "TB_DISCOVER", conditionList);

            getData("DELETE FROM TB_RESULT WHERE ID = ?", "DELETE", "TB_DISCOVER", conditionList);

            if (databaseAffected)
            {
                status = true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on deleted row!", exception);
        }

        return status;
    }

    public List<List<String>> getReDiscoveryData(String ip, String deviceType)
    {
        List<List<String>> rediscoveryList = new ArrayList<>();

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(ip);

            conditionList.add(deviceType);

            rediscoveryList = getData("SELECT * FROM TB_DISCOVER WHERE IP = ? AND DEVICE = ?", "SELECT", "TB_DISCOVER", conditionList);
        }
        catch (Exception exception)
        {
            _logger.error("not find re discover table data!!", exception);
        }

        return rediscoveryList;
    }

    public List<List<String>> getReMonitorData(String ip, String deviceType)
    {
        List<List<String>> reMonitorList = new ArrayList<>();

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(ip);

            conditionList.add(deviceType);

            reMonitorList = getData("SELECT * FROM TB_MONITOR WHERE IP = ? AND DEVICETYPE = ?", "SELECT", "TB_MONITOR", conditionList);
        }
        catch (Exception exception)
        {
            _logger.error("not find re monitor table data!!", exception);
        }

        return reMonitorList;
    }

    public List<List<String>> getDashboardData()
    {
        List<List<String>> reMonitorList = new ArrayList<>();

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(ip);

            conditionList.add(deviceType);

            reMonitorList = getData("SELECT * FROM TB_MONITOR WHERE IP = ? AND DEVICETYPE = ?", "SELECT", "TB_MONITOR", conditionList);
        }
        catch (Exception exception)
        {
            _logger.error("not find dashboard data!!", exception);
        }

        return reMonitorList;
    }

    public String getUpdatedPacket(int id, String ip, String time, String deviceType)
    {
        String packet = null;

        List<List<String>> packetList;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(id);

            conditionList.add(ip);

            conditionList.add(deviceType);

            conditionList.add(time);

            packetList = getData("SELECT * FROM TB_DATADUMP WHERE ID = ? AND IP = ? AND DEVICE = ? AND CURRENTTIME = ?", "SELECT", "TB_DATADUMP", conditionList);

            if (packetList.size() != 0)
            {
                packet = packetList.get(0).get(2);
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

        List<List<String>> memoryList;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(id);

            conditionList.add(ip);

            conditionList.add(deviceType);

            conditionList.add(time);

            memoryList = getData("SELECT * FROM TB_DATADUMP WHERE ID = ? AND IP = ? AND DEVICE = ? AND CURRENTTIME = ?", "SELECT", "TB_DATADUMP", conditionList);

            if (memoryList.size() != 0)
            {
                memoryPercent = Double.parseDouble(memoryList.get(0).get(3));
            }

        }
        catch (Exception exception)
        {
            _logger.warn("error on getting updated memory!");
        }

        return memoryPercent;
    }

    public boolean  enterDataDump(int id, String ip, String packet, Double memory, String deviceType, String time, String ipStatus)
    {
        boolean status = false;

        List<Object> conditionList = new ArrayList<>();

        try
        {
            conditionList.add(id);

            conditionList.add(ip);

            conditionList.add(packet);

            conditionList.add(memory);

            conditionList.add(deviceType);

            conditionList.add(time.substring(0, 16));

            conditionList.add(ipStatus);

            getData("INSERT INTO TB_DATADUMP(ID, IP, PACKET, MEMORY, DEVICE, CURRENTTIME, STATUS) VALUES(?, ?, ?, ?, ?, ?, ?)", "INSERT", "TB_DATADUMP", conditionList);

            if (databaseAffected)
            {
                status = true;
            }
        }
        catch (Exception exception)
        {
            _logger.error("not inserted data into tb_dataDump!", exception);
        }

        return status;
    }
}

