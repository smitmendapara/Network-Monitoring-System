package dao;

import action.helper.ServiceProvider;
import util.CommonConstantUI;
import util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DAO
{
    private Connection connection = null;

    private static final Logger _logger = new Logger();

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

    public boolean checkCredential(String username, String password)
    {
        boolean status = false;

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> credentialsList = executeSELECT(connection, CommonConstantUI.DB_TB_USER, "*", "WHERE USER = '" + username + "' AND PASSWORD = '" + password + "'");

            if (credentialsList.size() != 0)
            {
                status = Boolean.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("data not get properly!", exception);
        }

        return status;
    }

    public boolean enterDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String timestamp)
    {
        boolean status = false;

        ArrayList<String> data = new ArrayList<>();

        try
        {
            if (deviceType.equals("0"))
            {
                deviceType = CommonConstantUI.PING_DEVICE;
            }
            else
            {
                deviceType = CommonConstantUI.LINUX_DEVICE;
            }

            data.add("'" + name + "', '" + ip + "', '" + discoveryUsername + "', '" + discoveryPassword + "', '" + deviceType + "', '" + timestamp.substring(0, 16) + "'");

            data.add("(NAME, IP, USERNAME, PASSWORD, DEVICE, CURRENTTIME)");

            connection = getConnection();

            int affectedRows = executeINSERT(connection, CommonConstantUI.DB_TB_DISCOVER, data.get(0), data.get(1));

            if (affectedRows != 0)
            {
                status = Boolean.TRUE;
            }
        }
        catch (Exception exception)
        {

        }

        return status;
    }

    public boolean enterMonitorTableData(int id, String response, String ipStatus)
    {
        boolean status = false;

        ArrayList<String> data = new ArrayList<>();

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> discoverDataList = executeSELECT(connection, CommonConstantUI.DB_TB_DISCOVER, "*", "WHERE ID = " + id);

            for (HashMap<String, Object> map : discoverDataList)
            {
                data.add(id + ", '" + map.get("Name") + "', '" + map.get("IP") + "', '" + map.get("Username") + "', '" + map.get("Password") + "', '" + map.get("Device") + "', '" + response + "', '" + ipStatus + "', '" + map.get("CurrentTime") + "'");
            }

            connection = getConnection();

            int affectedRows = executeINSERT(connection, CommonConstantUI.DB_TB_MONITOR, data.get(0), "");

            if (affectedRows != 0)
            {
                status = Boolean.TRUE;
            }
        }
        catch (Exception exception)
        {

        }

        return status;
    }

    public boolean checkIpMonitor(String ip, String deviceType)
    {
        boolean status = false;

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> monitorIpList = executeSELECT(connection, CommonConstantUI.DB_TB_MONITOR, "*", "WHERE IP = '" + ip + "' AND DEVICETYPE = '" + deviceType + "'");

            if (monitorIpList == null)
            {
                status = Boolean.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("ip not checked for provision...!", exception);
        }

        return status;

    }

    public List<HashMap<String, Object>> getMonitorDetails(int id)
    {
        try
        {
            connection = getConnection();

            return executeSELECT(connection, CommonConstantUI.DB_TB_DISCOVER, "*", "WHERE ID = " + id);
        }
        catch (Exception exception)
        {

        }

        return null;
    }

    public List<HashMap<String, Object>> getDashboardData(String ip, String deviceType)
    {
        try
        {
            connection = getConnection();

            return executeSELECT(connection, CommonConstantUI.DB_TB_MONITOR, "*", "WHERE IP = '" + ip + "' AND DEVICETYPE = '" + deviceType + "'");
        }
        catch (Exception exception)
        {

        }

        return null;
    }

    public List<Integer> getStatusPercent(String ip, String deviceType)
    {
        double up = 0, down = 0, total;

        List<Integer> statusPercent = new ArrayList<>();

        ServiceProvider serviceProvider = new ServiceProvider();

        try
        {
            connection = getConnection();

            List<String> currentTime = serviceProvider.getCurrentTime();

            List<HashMap<String, Object>> totalFrequency = executeSELECT(connection, CommonConstantUI.DB_TB_DATADUMP, "STATUS, COUNT(IP)", "WHERE IP = '" + ip + "' AND DEVICE = '" + deviceType + "' AND CURRENTTIME BETWEEN '" + currentTime.get(1) + "' AND '" + currentTime.get(0) + "' GROUP BY STATUS ORDER BY STATUS DESC");

            if (totalFrequency != null)
            {
                if (totalFrequency.size() == 2)
                {
                    up = Double.parseDouble(totalFrequency.get(0).get("COUNT(IP)").toString());

                    down = Double.parseDouble(totalFrequency.get(1).get("COUNT(IP)").toString());
                }
                if (totalFrequency.size() == 1)
                {
                    if (totalFrequency.get(0).get("STATUS").equals("Up"))
                    {
                        up = Double.parseDouble(totalFrequency.get(0).get("COUNT(IP)").toString());
                    }
                    else
                    {
                        down = Double.parseDouble(totalFrequency.get(0).get("COUNT(IP)").toString());
                    }
                }
            }

            total = up + down;

            statusPercent.add((int) Math.round((up / total) * 100));

            statusPercent.add((int) Math.round((down / total) * 100));
        }
        catch (Exception exception)
        {

        }

        return statusPercent;
    }

    public String getUpdatedPacket(int id, String ip, String time, String deviceType)
    {
        String packet = null;

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> updatedPacket = executeSELECT(connection, CommonConstantUI.DB_TB_DATADUMP, "*", "WHERE ID = " + id + " AND IP = '" + ip + "' AND DEVICE = '" + deviceType + "' AND CURRENTTIME = '" + time + "'");

            if (updatedPacket != null)
            {
                packet = (String) updatedPacket.get(0).get("Packet");
            }
        }
        catch (Exception exception)
        {

        }

        return packet;
    }

    public Double getUpdatedMemory(int id, String ip, String time, String deviceType)
    {
        Double memoryPercent = null;

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> memoryMap = executeSELECT(connection, CommonConstantUI.DB_TB_DATADUMP, "*", "WHERE ID = " + id + " AND IP = '" + ip + "' AND DEVICE = '" + deviceType + "' AND CURRENTTIME = '" + time + "'");

            if (memoryMap != null)
            {
                memoryPercent = (Double) memoryMap.get(0).get("Memory");
            }
        }
        catch (Exception exception)
        {

        }

        return memoryPercent;
    }

    public List<String> getEditFieldsData(String ip, String deviceType)
    {
        List<String> editData = new ArrayList<>();

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> editMap = executeSELECT(connection, CommonConstantUI.DB_TB_DISCOVER, "*", "WHERE IP = '" + ip + "' AND DEVICE = '" + deviceType + "'");

            if (editMap.size() == 1)
            {
                editData.add(editMap.get(0).get("Id").toString());

                editData.add((String) editMap.get(0).get("Name"));

                editData.add((String) editMap.get(0).get("IP"));

                editData.add((String) editMap.get(0).get("Username"));

                editData.add((String) editMap.get(0).get("Password"));

                editData.add((String) editMap.get(0).get("Device"));
            }
        }
        catch (Exception exception)
        {

        }
        return editData;
    }

    public boolean insertUpdatedDeviceData(int id, String ip, String deviceType, String name, String discoveryUsername, String discoveryPassword)
    {
        boolean status = false;

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> updateDataExist = executeSELECT(connection, CommonConstantUI.DB_TB_DISCOVER, "*", "WHERE ID != " + id + " AND IP = '" + ip + "' AND DEVICE = '" + deviceType + "'");

            if (updateDataExist != null)
            {
                return Boolean.FALSE;
            }

            connection = getConnection();

            String currentTime = new Timestamp(System.currentTimeMillis()).toString().substring(0, 16);

            int affectedRow = executeUPDATE(connection, CommonConstantUI.DB_TB_DISCOVER, "WHERE ID = " + id, "IP = '" + ip + "', NAME = '" + name + "', CURRENTTIME = '" + currentTime + "', USERNAME = '" + discoveryUsername + "', PASSWORD = '" + discoveryPassword + "'");

            if (affectedRow != 0)
            {
                status = Boolean.TRUE;
            }
        }
        catch (Exception exception)
        {

        }

        return status;
    }

    public boolean getDiscoverTB(String ip, String deviceType)
    {
        boolean status = false;

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> discoverList = executeSELECT(connection, CommonConstantUI.DB_TB_DISCOVER, "*", "WHERE IP = '" + ip + "' AND DEVICE = '" + deviceType + "'");

            if (discoverList == null)
            {
                status = Boolean.TRUE;
            }

        }
        catch (Exception exception)
        {

        }

        return status;
    }

    public List<List<String>> getDiscoverData()
    {
        List<List<String>> discoverList = new ArrayList<>();

        ServiceProvider serviceProvider = new ServiceProvider();

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> discoverDataList = executeSELECT(connection, CommonConstantUI.DB_TB_DISCOVER, "*", "");

            discoverList = serviceProvider.getFormData(discoverDataList);
        }
        catch (Exception exception)
        {

        }

        return discoverList;
    }

    public List<List<String>> getMonitorForm(int id)
    {
        List<List<String>> monitorList = null;

        ServiceProvider serviceProvider = new ServiceProvider();

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> monitorDataList = executeSELECT(connection, CommonConstantUI.DB_TB_DISCOVER, "*", "WHERE ID = " + id);

            monitorList = serviceProvider.getFormData(monitorDataList);

        }
        catch (Exception exception)
        {

        }

        return monitorList;
    }

    public List<List<String>> getMonitorTable()
    {
        List<List<String>> monitorList = null;

        ServiceProvider serviceProvider = new ServiceProvider();

        try
        {
            connection = getConnection();

            List<HashMap<String, Object>> monitorDataList = executeSELECT(connection, CommonConstantUI.DB_TB_MONITOR, "*", "");

            monitorList = serviceProvider.getFormData(monitorDataList);
        }
        catch (Exception exception)
        {

        }

        return monitorList;
    }

    public boolean deleteMonitorData(int id)
    {
        boolean status = false;

        try
        {
            connection = getConnection();

            int affected = executeDELETE(connection, CommonConstantUI.DB_TB_MONITOR, "WHERE ID = " + id);

            if (affected != 0)
            {
                status = Boolean.TRUE;
            }
        }
        catch (Exception exception)
        {

        }

        return status;
    }

    public boolean deleteDiscoverTableData(int id)
    {
        boolean status = false;

        try
        {
            connection = getConnection();

            int affected = executeDELETE(connection, CommonConstantUI.DB_TB_DISCOVER, "WHERE ID = " + id);

            if (affected != 0)
            {
                status = Boolean.TRUE;
            }
        }
        catch (Exception exception)
        {

        }

        return status;
    }

    private int executeDELETE(Connection connection, String tableName, String condition)
    {
        int affectedRow = 0;

        try
        {
            if (!connection.isClosed())
            {
                Statement statement = connection.createStatement();

                if (statement != null)
                {
                    affectedRow = statement.executeUpdate("DELETE FROM " + tableName + " " + condition);

                    statement.close();
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("delete query not executed!", exception);
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
            catch (Exception ignored)
            {

            }
        }

        return affectedRow;
    }

    private int executeUPDATE(Connection connection, String tableName, String condition, String updateColumns)
    {
        int affectedRow = 0;

        try
        {
            if (!connection.isClosed())
            {
                Statement statement = connection.createStatement();

                if (statement != null)
                {
                    affectedRow = statement.executeUpdate("UPDATE " + tableName + " SET " + updateColumns + " " + condition);

                    statement.close();
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("update query not executed!", exception);
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
            catch (Exception ignored)
            {

            }
        }

        return affectedRow;
    }

    private int executeINSERT(Connection connection, String tableName, String tableRow, String columns)
    {
        int affectedRow = 0;

        try
        {
            if (!connection.isClosed())
            {
                Statement statement = connection.createStatement();

                if (statement != null)
                {
                    affectedRow = statement.executeUpdate("INSERT INTO " + tableName + columns + " VALUES (" + tableRow + ")");

                    statement.close();
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("insert query not executed!", exception);
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
            catch (Exception ignored)
            {

            }
        }

        return affectedRow;
    }

    private List<HashMap<String, Object>> executeSELECT(Connection connection, String tableName, String column, String condition)
    {
        List<HashMap<String, Object>> tableData = null;

        HashMap<String, Object> tableRow;

        ResultSetMetaData metaData;

        try
        {
            if (!connection.isClosed())
            {
                Statement statement = connection.createStatement();

                if (statement != null)
                {
                    ResultSet resultSet = statement.executeQuery("SELECT " + column + " FROM " + tableName + " " + condition);

                    while (resultSet.next())
                    {
                        if (tableData == null)
                        {
                            tableData = new ArrayList<>();
                        }

                        tableRow = new HashMap<>();

                        metaData = resultSet.getMetaData();

                        if (metaData != null)
                        {
                            for (int index = 1; index <= metaData.getColumnCount(); index++)
                            {
                                tableRow.put(metaData.getColumnName(index), resultSet.getObject(index));
                            }

                            tableData.add(tableRow);
                        }

                    }

                    statement.close();
                }
            }
        }
        catch (Exception exception)
        {
            _logger.error("select query invalid...", exception);
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
            catch (Exception ignored)
            {

            }
        }

        return tableData;
    }
}
