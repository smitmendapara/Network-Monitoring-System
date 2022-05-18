package bean;

import java.util.List;

public class DashboardBean
{
    private int id;

    private String ip;

    private String name;

    private String discoveryUsername;

    private String discoveryPassword;

    private String deviceType;

    private String[] response;

    private String status;

    private String[] currentTime;

    private float[] dataPoints;

    private int[] percent;

    private long totalDevice;

    private long pingDevice;

    private long linuxDevice;

    private long upDevice;

    private long downDevice;

    private long unknownDevice;

    private String[] topMemory;

    private String[] topCPU;

    private String[] topDisk;

    private String title;

    private List<DashboardBean> beanList = null;

    public List<DashboardBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DashboardBean> beanList) {
        this.beanList = beanList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscoveryUsername() {
        return discoveryUsername;
    }

    public void setDiscoveryUsername(String discoveryUsername) {
        this.discoveryUsername = discoveryUsername;
    }

    public String getDiscoveryPassword() {
        return discoveryPassword;
    }

    public void setDiscoveryPassword(String discoveryPassword) {
        this.discoveryPassword = discoveryPassword;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String[] getResponse() {
        return response;
    }

    public void setResponse(String[] response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String[] currentTime) {
        this.currentTime = currentTime;
    }

    public float[] getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(float[] dataPoints) {
        this.dataPoints = dataPoints;
    }

    public int[] getPercent() {
        return percent;
    }

    public void setPercent(int[] percent) {
        this.percent = percent;
    }

    public long getTotalDevice() {
        return totalDevice;
    }

    public void setTotalDevice(long totalDevice) {
        this.totalDevice = totalDevice;
    }

    public long getPingDevice() {
        return pingDevice;
    }

    public void setPingDevice(long pingDevice) {
        this.pingDevice = pingDevice;
    }

    public long getLinuxDevice() {
        return linuxDevice;
    }

    public void setLinuxDevice(long linuxDevice) {
        this.linuxDevice = linuxDevice;
    }

    public long getUpDevice() {
        return upDevice;
    }

    public void setUpDevice(long upDevice) {
        this.upDevice = upDevice;
    }

    public long getDownDevice() {
        return downDevice;
    }

    public void setDownDevice(long downDevice) {
        this.downDevice = downDevice;
    }

    public String[] getTopMemory() {
        return topMemory;
    }

    public void setTopMemory(String[] topMemory) {
        this.topMemory = topMemory;
    }

    public String[] getTopCPU() {
        return topCPU;
    }

    public void setTopCPU(String[] topCPU) {
        this.topCPU = topCPU;
    }

    public String[] getTopDisk() {
        return topDisk;
    }

    public void setTopDisk(String[] topDisk) {
        this.topDisk = topDisk;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUnknownDevice() {
        return unknownDevice;
    }

    public void setUnknownDevice(long unknownDevice) {
        this.unknownDevice = unknownDevice;
    }
}
