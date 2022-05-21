package dao;

import service.CommonService;
import util.CommonConstant;
import util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectionDAO
{
    private Connection connection = null;

    private ConnectionPool connectionPool = new ConnectionPool();

    private static final Logger _logger = new Logger();

    public boolean checkCredential(String username, String password)
    {
        boolean status = CommonConstant.FALSE;

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> credentialsList = executeSELECT(connection, CommonConstant.DB_TB_USER, "*", "WHERE user = '" + username + "' AND password = '" + password + "'");

            if (credentialsList.size() != 0)
            {
                status = CommonConstant.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("access not granted.", exception);
        }

        return status;
    }

    public boolean enterDiscoveryData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String timestamp)
    {
        boolean status = CommonConstant.FALSE;

        ArrayList<String> data = new ArrayList<>();

        try
        {
            data.add("'" + name + "', '" + ip + "', '" + discoveryUsername + "', '" + discoveryPassword + "', '" + deviceType + "', '" + timestamp.substring(0, 16) + "', 'Unknown'");

            data.add("(Name, IP, Username, Password, Device, CurrentTime, Status)");

            connection = connectionPool.getConnection();

            int affectedRows = executeINSERT(connection, CommonConstant.DB_TB_DISCOVER, data.get(0), data.get(1));

            if (affectedRows != 0)
            {
                status = CommonConstant.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("didn't inserted discover device.", exception);
        }

        return status;
    }

    public boolean enterMonitorTableData(int id, String response, String ipStatus)
    {
        boolean status = CommonConstant.FALSE;

        ArrayList<String> data = new ArrayList<>();

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> discoverDataList = executeSELECT(connection, CommonConstant.DB_TB_DISCOVER, "*", "WHERE Id = " + id);

            for (HashMap<String, Object> map : discoverDataList)
            {
                data.add(id + ", '" + map.get("Name") + "', '" + map.get("IP") + "', '" + map.get("Username") + "', '" + map.get("Password") + "', '" + map.get("Device") + "', '" + response + "', '" + ipStatus + "', '" + map.get("CurrentTime") + "'");
            }

            int affectedRows = executeINSERT(connection, CommonConstant.DB_TB_MONITOR, data.get(0), "");

            if (affectedRows != 0)
            {
                status = CommonConstant.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("didn't inserted monitor device.", exception);
        }

        return status;
    }

    public boolean checkIpMonitor(String ip, String deviceType)
    {
        boolean status = CommonConstant.FALSE;

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> monitorIpList = executeSELECT(connection, CommonConstant.DB_TB_MONITOR, "*", "WHERE IP = '" + ip + "' AND DeviceType = '" + deviceType + "'");

            if (monitorIpList == null)
            {
                status = CommonConstant.TRUE;
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
            connection = connectionPool.getConnection();

            return executeSELECT(connection, CommonConstant.DB_TB_DISCOVER, "*", "WHERE Id = " + id);
        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched monitor device details.", exception);
        }

        return null;
    }

    public List<HashMap<String, Object>> getDashboardData(String ip, String deviceType)
    {
        try
        {
            connection = connectionPool.getConnection();

            return executeSELECT(connection, CommonConstant.DB_TB_MONITOR, "*", "WHERE IP = '" + ip + "' AND DeviceType = '" + deviceType + "'");
        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched dashboard data.", exception);
        }

        return null;
    }

    public List<Integer> getStatusPercent(String ip, String deviceType)
    {
        double up = 0, down = 0, total;

        List<Integer> statusPercent = new ArrayList<>();

        CommonService commonService = new CommonService();

        try
        {
            connection = connectionPool.getConnection();

            List<String> currentTime = commonService.getCurrentTime();

            List<HashMap<String, Object>> totalFrequency = executeSELECT(connection, CommonConstant.DB_TB_DATADUMP, "Status, COUNT(IP)", "WHERE IP = '" + ip + "' AND Device = '" + deviceType + "' AND CurrentTime BETWEEN '" + currentTime.get(1) + "' AND '" + currentTime.get(0) + "' GROUP BY Status ORDER BY Status DESC");

            if (totalFrequency != null)
            {
                if (totalFrequency.size() == 2)
                {
                    up = Double.parseDouble(totalFrequency.get(0).get("COUNT(IP)").toString());

                    down = Double.parseDouble(totalFrequency.get(1).get("COUNT(IP)").toString());
                }
                if (totalFrequency.size() == 1)
                {
                    if (totalFrequency.get(0).get("Status").equals("Up"))
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
            _logger.error("didn't fetched status percent.", exception);
        }

        return statusPercent;
    }

    public int getUpdatedPacket(int id, String ip, String time, String deviceType)
    {
        int packet = 0;

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> updatedPacket = executeSELECT(connection, CommonConstant.DB_TB_DATADUMP, "*", "WHERE Id = " + id + " AND IP = '" + ip + "' AND Device = '" + deviceType + "' AND CurrentTime = '" + time + "'");

            if (updatedPacket != null)
            {
                packet = Integer.parseInt(updatedPacket.get(0).get("Packet").toString());
            }
        }
        catch (Exception exception)
        {
            _logger.error("didn't updated device packet.", exception);
        }

        return packet;
    }

    public float getUpdatedMemory(int id, String ip, String time, String deviceType)
    {
        float memoryPercent = 0;

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> memoryMap = executeSELECT(connection, CommonConstant.DB_TB_DATADUMP, "*", "WHERE Id = " + id + " AND IP = '" + ip + "' AND Device = '" + deviceType + "' AND CurrentTime = '" + time + "'");

            if (memoryMap != null)
            {
                memoryPercent = Float.parseFloat(memoryMap.get(0).get("Memory").toString());
            }
        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched device updated memory.", exception);
        }

        return memoryPercent;
    }

    public List<String> getEditFieldsData(String ip, String deviceType)
    {
        List<String> editData = new ArrayList<>();

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> editMap = executeSELECT(connection, CommonConstant.DB_TB_DISCOVER, "*", "WHERE IP = '" + ip + "' AND Device = '" + deviceType + "'");

            if (editMap.size() == 1)
            {
                editData.add(editMap.get(0).get("Id").toString());

                editData.add(editMap.get(0).get("Name").toString());

                editData.add(editMap.get(0).get("IP").toString());

                editData.add( editMap.get(0).get("Username").toString());

                editData.add(editMap.get(0).get("Password").toString());

                editData.add(editMap.get(0).get("Device").toString());
            }
        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched edit device details.", exception);
        }
        return editData;
    }

    public boolean updatedDeviceData(int id, String ip, String deviceType, String name, String discoveryUsername, String discoveryPassword)
    {
        boolean status = CommonConstant.FALSE;

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> updateDataExist = executeSELECT(connection, CommonConstant.DB_TB_DISCOVER, "*", "WHERE Id != " + id + " AND IP = '" + ip + "' AND Device = '" + deviceType + "'");

            if (updateDataExist != null)
            {
                return Boolean.FALSE;
            }

            String currentTime = new Timestamp(System.currentTimeMillis()).toString().substring(0, 16);

            int affectedRow = executeUPDATE(connection, CommonConstant.DB_TB_DISCOVER, "WHERE Id = " + id, "IP = '" + ip + "', Name = '" + name + "', CurrentTime = '" + currentTime + "', Username = '" + discoveryUsername + "', Password = '" + discoveryPassword + "'");

            if (affectedRow != 0)
            {
                status = CommonConstant.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("didn't updated discover device.", exception);
        }

        return status;
    }

    public boolean updateDiscoverData(String ip, String deviceType, String timestamp, String ipStatus)
    {
        boolean status = CommonConstant.FALSE;

        try
        {
            connection = connectionPool.getConnection();

            int affected = executeUPDATE(connection, CommonConstant.DB_TB_DISCOVER, "WHERE IP = '" + ip + "' AND Device = '" + deviceType + "'", "CurrentTime = '" + timestamp.substring(0, 16) + "', Status = '" + ipStatus + "'");

            if (affected != 0)
            {
                status = CommonConstant.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("didn't updated discover device data.", exception);
        }

        return status;
    }

    public boolean getDiscoverTB(String ip, String deviceType)
    {
        boolean status = CommonConstant.FALSE;

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> discoverList = executeSELECT(connection, CommonConstant.DB_TB_DISCOVER, "*", "WHERE IP = '" + ip + "' AND Device = '" + deviceType + "'");

            if (discoverList == null)
            {
                status = CommonConstant.TRUE;
            }

        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched discover data details.", exception);
        }

        return status;
    }

    public List<List<String>> getDiscoverData()
    {
        List<List<String>> discoverList = new ArrayList<>();

        CommonService commonService = new CommonService();

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> discoverDataList = executeSELECT(connection, CommonConstant.DB_TB_DISCOVER, "*", "");

            discoverList = commonService.getFormData(discoverDataList);
        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched discover table data.", exception);
        }

        return discoverList;
    }

    public List<List<String>> getMonitorForm(int id)
    {
        List<List<String>> monitorList = null;

        CommonService commonService = new CommonService();

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> monitorDataList = executeSELECT(connection, CommonConstant.DB_TB_DISCOVER, "*", "WHERE Id = " + id);

            monitorList = commonService.getFormData(monitorDataList);

        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched monitor form.", exception);
        }

        return monitorList;
    }

    public List<List<String>> getMonitorTable()
    {
        List<List<String>> monitorList = null;

        CommonService commonService = new CommonService();

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> monitorDataList = executeSELECT(connection, CommonConstant.DB_TB_MONITOR, "*", "");

            monitorList = commonService.getFormData(monitorDataList);
        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched monitor table data.", exception);
        }

        return monitorList;
    }

    public List<Integer> getDeviceStatusList()
    {
        List<Integer> deviceList = new ArrayList<>();

        CommonService commonService = new CommonService();

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> upDevice = executeSELECT(connection, CommonConstant.DB_TB_MONITOR, "COUNT(IP)", "WHERE Status = 'Up'");

            List<HashMap<String, Object>> downDevice = executeSELECT(connection, CommonConstant.DB_TB_MONITOR, "COUNT(IP)", "WHERE Status = 'Down'");

            List<HashMap<String, Object>> unknownDevice = executeSELECT(connection, CommonConstant.DB_TB_MONITOR, "COUNT(IP)", "WHERE Status = 'Unknown'");

            deviceList = commonService.getDeviceDetailsList(upDevice, downDevice, unknownDevice);
        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched device status list.", exception);
        }

        return deviceList;
    }

    public List<Integer> getDeviceTypeList()
    {
        List<Integer> deviceList = new ArrayList<>();

        CommonService commonService = new CommonService();

        try
        {
            connection = connectionPool.getConnection();

            List<HashMap<String, Object>> pingDevice = executeSELECT(connection, CommonConstant.DB_TB_MONITOR, "COUNT(IP)", "WHERE DeviceType = 'Ping'");

            List<HashMap<String, Object>> linuxDevice = executeSELECT(connection, CommonConstant.DB_TB_MONITOR, "COUNT(IP)", "WHERE DeviceType = 'Linux'");

            deviceList = commonService.getDeviceDetailsList(pingDevice, linuxDevice, null);
        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched device type list.", exception);
        }

        return deviceList;
    }

    public String[] getTopMemoryDeviceDetails()
    {
        CommonService commonService = new CommonService();

        String[] topMemoryCPUDiskDevice = null;

        try
        {
            connection = connectionPool.getConnection();

            String previousTime = commonService.getPreviousTime();

            List<HashMap<String, Object>> topMemoryDevice = executeSELECT(connection, CommonConstant.DB_TB_DATADUMP, "MAX(Memory) AS MAXPERCENT, IP", "WHERE CurrentTime > '" + previousTime + "' GROUP BY IP ORDER BY MAXPERCENT DESC LIMIT 2");

            topMemoryCPUDiskDevice = commonService.getTopMemoryCPUDiskDevice(topMemoryDevice);

        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched top memory device details.", exception);
        }

        return topMemoryCPUDiskDevice;
    }

    public String[] getTopCPUDeviceDetails()
    {
        CommonService commonService = new CommonService();

        String[] topMemoryCPUDiskDevice = null;

        try
        {
            connection = connectionPool.getConnection();

            String previousTime = commonService.getPreviousTime();

            List<HashMap<String, Object>> topCpuDevice = executeSELECT(connection, CommonConstant.DB_TB_DATADUMP, "MAX(CPU) AS MAXPERCENT, IP", "WHERE CurrentTime > '" + previousTime + "' GROUP BY IP ORDER BY MAXPERCENT DESC LIMIT 2");

            topMemoryCPUDiskDevice = commonService.getTopMemoryCPUDiskDevice(topCpuDevice);

        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched top cpu device details.", exception);
        }

        return topMemoryCPUDiskDevice;
    }

    public String[] getTopDiskDeviceDetails()
    {
        CommonService commonService = new CommonService();

        String[] topMemoryCPUDiskDevice = null;

        try
        {
            connection = connectionPool.getConnection();

            String previousTime = commonService.getPreviousTime();

            List<HashMap<String, Object>> topDiskDevice = executeSELECT(connection, CommonConstant.DB_TB_DATADUMP, "MAX(Disk) AS MAXPERCENT, IP", "WHERE CurrentTime > '" + previousTime + "' GROUP BY IP ORDER BY MAXPERCENT DESC LIMIT 2");

            topMemoryCPUDiskDevice = commonService.getTopMemoryCPUDiskDevice(topDiskDevice);

        }
        catch (Exception exception)
        {
            _logger.error("didn't fetched top disk device details.", exception);
        }

        return topMemoryCPUDiskDevice;
    }

    public boolean deleteMonitorData(int id)
    {
        boolean status = CommonConstant.FALSE;

        try
        {
            connection = connectionPool.getConnection();

            int affected = executeDELETE(connection, CommonConstant.DB_TB_MONITOR, "WHERE Id = " + id);

            if (affected != 0)
            {
                status = CommonConstant.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("delete query not executed for monitor.", exception);
        }

        return status;
    }

    public boolean deleteDiscoverTableData(int id)
    {
        boolean status = CommonConstant.FALSE;

        try
        {
            connection = connectionPool.getConnection();

            int affected = executeDELETE(connection, CommonConstant.DB_TB_DISCOVER, "WHERE Id = " + id);

            if (affected != 0)
            {
                status = CommonConstant.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("delete query not executed for discover.", exception);
        }

        return status;
    }

    public boolean enterReMonitorData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, String response, String ipStatus, String timestamp)
    {
        boolean result = CommonConstant.FALSE;

        try
        {
            connection = connectionPool.getConnection();

            int affected = executeUPDATE(connection, CommonConstant.DB_TB_MONITOR, "WHERE IP = '" + ip + "' AND DeviceType = '" + deviceType + "'", "Response = '" + response + "', Status = '" + ipStatus + "', CurrentTime = '" + timestamp.substring(0, 16) + "'");

            if (affected != 0)
            {
                result = CommonConstant.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("monitor data not updated properly!", exception);
        }

        return result;
    }

    public boolean enterDataDump(int id, String ip, String packet, Double memory, String deviceType, String time, String ipStatus, Double cpu, Double disk)
    {
        boolean status = CommonConstant.FALSE;

        ArrayList<String> data = new ArrayList<>();

        try
        {
            connection = connectionPool.getConnection();

            data.add(id + ", '" + ip + "', '" + packet + "', '" + memory + "', '" + deviceType + "', '" + time.substring(0, 16) + "', '" + ipStatus + "', '" + cpu + "', '" + disk + "'");

            int affectedRows = executeINSERT(connection, CommonConstant.DB_TB_DATADUMP, data.get(0), "");

            if (affectedRows != 0)
            {
                status = CommonConstant.TRUE;
            }
        }
        catch (Exception exception)
        {
            _logger.error("not inserted data into tb_dataDump!", exception);

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
                    if (connectionPool.releaseConnection(connection))
                    {
                        _logger.info("connection successfully released from DELETE query...");
                    }
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
                    if (connectionPool.releaseConnection(connection))
                    {
                        _logger.info("connection successfully released from UPDATE query...");
                    }
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
                    if (connectionPool.releaseConnection(connection))
                    {
                        _logger.info("connection successfully released from INSERT query...");
                    }
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

                        HashMap<String, Object> tableRow = new HashMap<>();

                        ResultSetMetaData metaData = resultSet.getMetaData();

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
                    if (connectionPool.releaseConnection(connection))
                    {
                        _logger.info("connection successfully released from SELECT query...");
                    }
                }

            }
            catch (Exception ignored)
            {

            }
        }

        return tableData;
    }
}
