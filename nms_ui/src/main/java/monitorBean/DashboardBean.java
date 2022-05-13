package monitorBean;

public class DashboardBean
{
    private int Id;

    private String IP;

    private String Name;

    private String Username;

    private String Password;

    private String Device;

    private String[] Response;

    private String Status;

    private String[] CurrentTime;

    private float[] dataPoints;

    private int[] percent;

    private long totalDevice;

    private long pingDevice;

    private long linuxDevice;

    private long upDevice;

    private long downDevice;

    private String[] topMemory;

    private String[] topCPU;

    private String[] topDisk;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float[] getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(float[] dataPoints) {
        this.dataPoints = dataPoints;
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

    public long getTotalDevice() {
        return totalDevice;
    }

    public void setTotalDevice(long totalDevice) {
        this.totalDevice = totalDevice;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String[] getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String[] currentTime) {
        CurrentTime = currentTime;
    }

    public String[] getResponse() {
        return Response;
    }

    public void setResponse(String[] response) {
        Response = response;
    }

    public int[] getPercent() {
        return percent;
    }

    public void setPercent(int[] percent) {
        this.percent = percent;
    }
}
