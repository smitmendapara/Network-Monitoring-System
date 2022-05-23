package service;

import bean.DiscoverBean;
import dao.ConnectionDAO;
import org.apache.struts2.ServletActionContext;
import util.CommonConstant;
import util.CommonUtil;
import util.Logger;
import util.SSHConnectionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscoveryService
{
    private static final Logger _logger = new Logger();

    public static void addDiscoveryDevice(DiscoverBean discoverBean)
    {
        try
        {
            if (validIP(discoverBean.getIp()))
            {
                if (discoverBean.getDeviceType().equals(CommonConstant.STRING_ZERO))
                {
                    discoverBean.setDeviceType(CommonConstant.PING_DEVICE);
                }
                else
                {
                    discoverBean.setDeviceType(CommonConstant.LINUX_DEVICE);
                }

                discoverBean.setIpValid(CommonConstant.TRUE);

                discoverBean.setFlag(addDevice(discoverBean.getName(), discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType()));
            }
            else
            {
                discoverBean.setIpValid(Boolean.FALSE);
            }
        }
        catch (Exception exception)
        {
            _logger.error("device not added.", exception);
        }
    }

    public static void executeDeviceDiscovery(DiscoverBean discoverBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            List<HashMap<String, Object>> monitorDetailsList = connectionDao.getMonitorDetails(discoverBean.getId());

            HttpServletRequest httpServletRequest = ServletActionContext.getRequest();

            HttpSession session = httpServletRequest.getSession();

            for (HashMap<String, Object> monitorDetails : monitorDetailsList)
            {
                discoverBean.setIp(monitorDetails.get("IP").toString());

                discoverBean.setDiscoveryUsername(monitorDetails.get("Username").toString());

                discoverBean.setDiscoveryPassword(monitorDetails.get("Password").toString());

                discoverBean.setDeviceType(monitorDetails.get("Device").toString());

                discoverBean.setSessionId(session.getAttribute("loginUser").toString());
            }

            discoverBean.setFlag(CommonUtil.putDiscoverBean(discoverBean));
        }
        catch (Exception exception)
        {
            _logger.error("discovery not executed from discovery service", exception);
        }
    }

    public static void deleteDiscoverDevice(DiscoverBean discoverBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            discoverBean.setFlag(connectionDao.deleteDiscoverTableData(discoverBean.getId()));
        }
        catch (Exception exception)
        {
            _logger.error("discover device not deleted.", exception);
        }
    }

    public static void getDiscoverDeviceData(DiscoverBean discoverBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        List<DiscoverBean> beanList = new ArrayList<>();

        try
        {
            List<List<String>> discoverList = connectionDao.getDiscoverData();

            if (discoverList != null)
            {
                for (List<String> subList : discoverList)
                {
                    DiscoverBean newBean = new DiscoverBean();

                    newBean.setId(Integer.parseInt(subList.get(0)));

                    newBean.setName(subList.get(1));

                    newBean.setIp(subList.get(2));

                    newBean.setDeviceType(subList.get(4));

                    beanList.add(newBean);
                }
            }

            discoverBean.setBeanList(beanList);
        }
        catch (Exception exception)
        {
            _logger.error("device details not fetched.", exception);
        }
    }

    public static void editDeviceDetail(DiscoverBean discoverBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            List<String> editDataList = connectionDao.getEditFieldsData(discoverBean.getIp(), discoverBean.getDeviceType());

            if (editDataList != null)
            {
                discoverBean.setName(editDataList.get(1));

                discoverBean.setDiscoveryUsername(editDataList.get(3));

                discoverBean.setDiscoveryPassword(editDataList.get(4));
            }
        }
        catch (Exception exception)
        {
            _logger.error("edit device detail not fetched.", exception);
        }
    }

    public static void updateDevice(DiscoverBean discoverBean)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            discoverBean.setFlag(connectionDao.updatedDeviceData(discoverBean.getId(), discoverBean.getIp(), discoverBean.getDeviceType(), discoverBean.getName(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword()));
        }
        catch (Exception exception)
        {
            _logger.error("device not updated.", exception);
        }
    }

    public static boolean executeDeviceDiscovery(int id, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        boolean status = CommonConstant.TRUE;

        String response, ipStatus;

        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            if (deviceType.equals(CommonConstant.STRING_ZERO) || deviceType.equals(CommonConstant.PING_DEVICE))
            {
                try
                {
                    response = pingDeviceResponse(ip);

                    ipStatus = checkPingIpStatus(response);

                    String timestamp = new Timestamp(System.currentTimeMillis()).toString();

                    if (connectionDao.updateDiscoverData(ip, deviceType, timestamp, ipStatus))
                    {
                        _logger.info("successfully device discovered data inserted...");
                    }
                }
                catch (Exception exception)
                {
                    _logger.error("something went wrong on ping discovery verify side!", exception);

                    status = CommonConstant.FALSE;
                }

                return status;
            }

            if (deviceType.equals(CommonConstant.STRING_ONE) || deviceType.equals(CommonConstant.LINUX_DEVICE))
            {
                String uName;

                SSHConnectionUtil sshConnectionUtil = null;

                try
                {
                    ipStatus = checkPingIpStatus(pingDeviceResponse(ip));

                    if (ipStatus.equals(CommonConstant.DEVICE_UP))
                    {
                        sshConnectionUtil = SSHConnectionUtil.getSSHObject(ip, CommonConstant.SSH_PORT, discoveryUsername, discoveryPassword, CommonConstant.SSH_TIMEOUT);

                        if(sshConnectionUtil != null)
                        {
                            uName = sshConnectionUtil.executeCommand(CommonConstant.U_NAME_COMMAND);

                            if (uName.trim().equals(CommonConstant.LINUX_DEVICE))
                            {
                                ipStatus = CommonConstant.DEVICE_UP;
                            }
                            else
                            {
                                status = CommonConstant.FALSE;
                            }
                        }
                        else
                        {
                            _logger.warn("ssh object is null!");

                            ipStatus = CommonConstant.DEVICE_DOWN;
                        }
                    }
                    else
                    {
                        ipStatus = CommonConstant.DEVICE_DOWN;
                    }

                    String timestamp = new Timestamp(System.currentTimeMillis()).toString();

                    if (connectionDao.updateDiscoverData(ip, deviceType, timestamp, ipStatus))
                    {
                        _logger.info("successfully device discovered data inserted...");
                    }
                }
                catch (Exception exception)
                {
                    _logger.error("something went wrong on linux discovery verify side!", exception);

                    status = CommonConstant.FALSE;
                }
                finally
                {
                    try
                    {
                        if (sshConnectionUtil != null)
                        {
                            sshConnectionUtil.disconnect();
                        }

                    }
                    catch (Exception exception)
                    {
                        _logger.warn("ssh connection is not closed!");
                    }
                }

                return status;
            }
        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on discovery side!", exception);

            status = CommonConstant.FALSE;
        }

        return status;
    }

    public static boolean pollingDevice(int id, String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        boolean status = CommonConstant.TRUE;

        double memory, disk, cpu;

        String packet, uName_M, uName_R, hostName, uName_Output, free_Output, df_Output, ioStat_Output, specificData, uName;

        String response, ipStatus;

        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            if (deviceType.equals(CommonConstant.STRING_ZERO) || deviceType.equals(CommonConstant.PING_DEVICE))
            {
                Timestamp timestamp;

                try
                {
                    timestamp = new Timestamp(System.currentTimeMillis());

                    response = pingDeviceResponse(ip);

                    ipStatus = checkPingIpStatus(response);

                    if (connectionDao.enterReMonitorData(name, ip, discoveryUsername, discoveryPassword, deviceType, response, ipStatus, timestamp.toString()))
                    {
                        packet = getReceivedPacket(response);

                        memory = CommonConstant.DOUBLE_ZERO;

                        disk = CommonConstant.DOUBLE_ZERO;

                        cpu = CommonConstant.DOUBLE_ZERO;

                        if (connectionDao.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus, cpu, disk))
                        {
                            _logger.debug("successfully data re-inserted into tb_monitor, tb_discovery & tb_dataDump table!");
                        }
                        else
                        {
                            _logger.debug("successfully data re-inserted into tb_discovery table!");
                        }
                    }

                }
                catch (Exception exception)
                {
                    _logger.error("something went wrong on ping discovery verify side!", exception);

                    status = CommonConstant.FALSE;
                }

                return status;

            }

            if (deviceType.equals(CommonConstant.STRING_ONE) || deviceType.equals(CommonConstant.LINUX_DEVICE))
            {
                Timestamp timestamp;

                SSHConnectionUtil sshConnectionUtil = null;

                try
                {
                    ipStatus = checkPingIpStatus(pingDeviceResponse(ip));

                    if (ipStatus.equals(CommonConstant.DEVICE_UP))
                    {
                        sshConnectionUtil = SSHConnectionUtil.getSSHObject(ip, CommonConstant.SSH_PORT, discoveryUsername, discoveryPassword, CommonConstant.SSH_TIMEOUT);

                        if(sshConnectionUtil != null)
                        {
                            uName = sshConnectionUtil.executeCommand(CommonConstant.U_NAME_COMMAND);

                            uName_M = sshConnectionUtil.executeCommand(CommonConstant.NAME_M_COMMAND);

                            uName_R = sshConnectionUtil.executeCommand(CommonConstant.NAME_R_COMMAND);

                            hostName = sshConnectionUtil.executeCommand(CommonConstant.HOSTNAME_COMMAND);

                            uName_Output = sshConnectionUtil.executeCommand(CommonConstant.LINUX_U_NAME_COMMAND);

                            free_Output = sshConnectionUtil.executeCommand(CommonConstant.LINUX_FREE_COMMAND);

                            df_Output = sshConnectionUtil.executeCommand(CommonConstant.LINUX_DISK_COMMAND);

                            ioStat_Output = sshConnectionUtil.executeCommand(CommonConstant.LINUX_CPU_COMMAND);

                            ipStatus = CommonConstant.DEVICE_UP;

                            specificData = getSpecificData(uName, uName_M, uName_R, hostName, uName_Output, free_Output, df_Output, ioStat_Output);

                            _logger.info("Command output : " + specificData);
                        }
                        else
                        {
                            _logger.warn("ssh object is null!");

                            ipStatus = CommonConstant.DEVICE_DOWN;

                            specificData = CommonConstant.STRING_NULL;
                        }
                    }
                    else
                    {
                        ipStatus = CommonConstant.DEVICE_DOWN;

                        specificData = CommonConstant.STRING_NULL;
                    }

                    timestamp = new Timestamp(System.currentTimeMillis());

                    if (connectionDao.enterReMonitorData(name, ip, discoveryUsername, discoveryPassword, deviceType, specificData, ipStatus, timestamp.toString()))
                    {
                        packet = CommonConstant.STRING_ZERO;

                        memory = CommonConstant.DOUBLE_ZERO;

                        disk = CommonConstant.DOUBLE_ZERO;

                        cpu = CommonConstant.DOUBLE_ZERO;

                        if (specificData != null && !specificData.equals(CommonConstant.STRING_NULL))
                        {
                            memory = getMemoryPercent(specificData);

                            disk = getUsedDiskPercent(specificData);

                            cpu = getCPUPercent(specificData);
                        }

                        if (connectionDao.enterDataDump(id, ip, packet, memory, deviceType, timestamp.toString(), ipStatus, cpu, disk))
                        {
                            _logger.debug("successfully data re-inserted into tb_monitor, tb_discover & tb_dataDump table!");
                        }
                        else
                        {
                            _logger.debug("successfully data re-inserted into tb_discovery table!");
                        }
                    }
                    else
                    {
                        _logger.warn("still not inserted data into tb_monitor table!");
                    }

                }
                catch (Exception exception)
                {
                    _logger.error("something went wrong on linux discovery verify side!", exception);

                    status = CommonConstant.FALSE;
                }

                finally
                {
                    try
                    {
                        if (sshConnectionUtil != null)
                        {
                            sshConnectionUtil.disconnect();
                        }

                    }
                    catch (Exception exception)
                    {
                        _logger.warn("ssh connection is not closed!");
                    }
                }

                return status;
            }

        }
        catch (Exception exception)
        {
            _logger.error("something went wrong on discovery side!", exception);

            status = CommonConstant.FALSE;
        }

        return status;

    }

    private static Double getMemoryPercent(String linuxResponse)
    {
        double free = CommonConstant.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            double totalMemory = Double.parseDouble(responseData[3].trim());

            double freeMemory = ( Double.parseDouble(responseData[5].trim()) / totalMemory ) * 100;

            free = Double.parseDouble(new DecimalFormat("##.##").format(freeMemory));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting free memory percent!");
        }

        return free;
    }

    private static Double getUsedDiskPercent(String linuxResponse)
    {
        double disk = CommonConstant.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            disk = Double.parseDouble(responseData[11].trim());

            disk = Double.parseDouble(new DecimalFormat("##.##").format(disk));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting free memory percent!");
        }

        return disk;
    }

    private static Double getCPUPercent(String linuxResponse)
    {
        double cpu = CommonConstant.DOUBLE_ZERO;

        try
        {
            String[] responseData = linuxResponse.split(",");

            cpu = Double.parseDouble(responseData[15].trim());

            cpu = Double.parseDouble(new DecimalFormat("##.##").format(cpu));
        }
        catch (Exception exception)
        {
            _logger.warn("error on getting free memory percent!");
        }

        return cpu;
    }

    private static String getSpecificData(String uName, String uName_M, String uName_R, String hostName, String uName_output, String free_output, String df_Output, String ioStat_Output)
    {
        String data = null;

        try
        {
            String linux = uName.trim();

            String ubuntu = hostName.trim();

            String x86 = uName_M.trim();

            String[] freeSplit = free_output.split("\n");

            String totalMemory = freeSplit[1].substring(4, 19).trim();

            String usedMemory = freeSplit[1].substring(19, 31).trim();

            String freeMemory = freeSplit[1].substring(31, 43).trim();

            String osVersion = uName_R.trim();

            String osName = uName_output.substring(uName_output.indexOf("GNU"), uName_output.length()).trim();

            String totalSwap = freeSplit[2].substring(5, 19).trim();

            String usedSwap = freeSplit[2].substring(19, 31).trim();

            String freeSwap = freeSplit[2].substring(31, 43).trim();

            String[] dfSplit = df_Output.split("\n");

            String disk = dfSplit[dfSplit.length-1].substring(dfSplit[dfSplit.length-1].length() - 6, dfSplit[dfSplit.length-1].length()-3).trim();

            String[] ioSplit = ioStat_Output.split("\n");

            String CPU_System = ioSplit[3].substring(23, 31).trim();

            String CPU_User = ioSplit[3].substring(0, 15).trim();

            String sharedMemory = freeSplit[1].substring(43, 55).trim();

            String cacheMemory = freeSplit[1].substring(55, 67).trim();

            String CPU_Idle = ioSplit[3].substring(48, ioSplit[3].length()).trim();

            String []stringArray = {linux, ubuntu, x86, totalMemory, usedMemory, freeMemory, osVersion, osName, totalSwap, usedSwap, freeSwap, disk, CPU_User, CPU_System, sharedMemory, CPU_Idle, cacheMemory};

            data = Arrays.toString(stringArray);

        }
        catch (Exception exception)
        {
            _logger.warn("not get specific data from linux output!");
        }

        return data;
    }

    private static String pingDeviceResponse(String ip)
    {
        String pingResult = "";

        String pingCmd = CommonConstant.PING_COMMAND + ip;

        Runtime runtime;

        Process process = null;

        BufferedReader bufferedInput = null;

        try
        {
            runtime = Runtime.getRuntime();

            process = runtime.exec(pingCmd);

            bufferedInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String inputLine;

            while ((inputLine = bufferedInput.readLine()) != null)
            {
                pingResult = pingResult.concat(inputLine);
            }
        }
        catch (Exception exception)
        {
            _logger.error("ping device response null...", exception);
        }
        finally
        {
            try
            {
                if (bufferedInput != null)
                {
                    bufferedInput.close();
                }
                if (process != null)
                {
                    process.destroy();
                }
            }
            catch (Exception exception)
            {
                _logger.error("buffer input or process not closed...", exception);
            }

        }

        return pingResult.substring(pingResult.indexOf("-"));
    }

    private static String checkStatus(String packet)
    {
        String ipStatus = CommonConstant.DEVICE_UP;
        try
        {
            if (packet.equals(CommonConstant.STRING_ZERO))
            {
                ipStatus = CommonConstant.DEVICE_DOWN;
            }
        }
        catch (Exception exception)
        {
            _logger.warn("doesn't check ping ip status!");
        }

        return ipStatus;
    }

    private static String checkPingIpStatus(String subString)
    {
        String ipStatus = null;

        try
        {
            String packet = subString.substring(subString.indexOf("transmitted") + 13, subString.indexOf("transmitted") + 14);

            ipStatus = checkStatus(packet);

        }
        catch (Exception exception)
        {
            _logger.warn("does not check IP status");
        }

        return ipStatus;
    }

    private static String getReceivedPacket(String subString)
    {
        String receivedPacket = CommonConstant.NULL;

        try
        {
            receivedPacket = subString.substring(subString.indexOf("transmitted") + 13, subString.indexOf("transmitted") + 14);
        }
        catch (Exception exception)
        {
            _logger.warn("packet not receive!");
        }

        return receivedPacket;
    }

    private static boolean validIP(String ipAddress)
    {
        boolean result = CommonConstant.FALSE;

        try
        {
            if (ipAddress == null)
            {
                return result;
            }

            String zeroTo255 = "(\\d{1,2}|(0|1)\\"
                    + "d{2}|2[0-4]\\d|25[0-5])";

            String regex = zeroTo255 + "\\." +
                    zeroTo255 + "\\."
                    + zeroTo255 + "\\."
                    + zeroTo255;

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(ipAddress);

            result = matcher.matches();
        }
        catch (Exception exception)
        {
            _logger.error("Ip not valid!", exception);
        }

        return result;
    }

    private static boolean addDevice(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType)
    {
        boolean status = CommonConstant.TRUE;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try
        {
            if (checkIp(ip, deviceType))
            {
                insertData(name, ip, discoveryUsername, discoveryPassword, deviceType, timestamp);
            }

            else
            {
                return CommonConstant.FALSE;
            }
        }
        catch (Exception exception)
        {
            status = CommonConstant.FALSE;
        }

        return status;
    }

    private static boolean checkIp(String ip, String deviceType)
    {
        boolean status = CommonConstant.TRUE;

        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            if (deviceType.equals(CommonConstant.STRING_ZERO))
            {
                deviceType = CommonConstant.PING_DEVICE;
            }
            if (deviceType.equals(CommonConstant.STRING_ONE))
            {
                deviceType = CommonConstant.LINUX_DEVICE;
            }

            status = connectionDao.getDiscoverTB(ip, deviceType);
        }
        catch (Exception exception)
        {
            _logger.warn("something went wrong on checking existing ip!");
        }

        return status;
    }

    private static void insertData(String name, String ip, String discoveryUsername, String discoveryPassword, String deviceType, Timestamp timestamp)
    {
        ConnectionDAO connectionDao = new ConnectionDAO();

        try
        {
            if (connectionDao.enterDiscoveryData(name, ip, discoveryUsername, discoveryPassword, deviceType, timestamp.toString()))
            {
                _logger.debug("successfully data inserted into tb_discovery table!");
            }
            else
            {
                _logger.warn("still not inserted into tb_discover table!");
            }
        }
        catch (Exception exception)
        {
            _logger.warn("data not inserted properly!");
        }
    }
}
