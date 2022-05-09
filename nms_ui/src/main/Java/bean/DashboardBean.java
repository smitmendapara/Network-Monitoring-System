package bean;

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

    private int[] Packet;

    private float[] Memory;

    private int[] percent;

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

    public int[] getPacket() {
        return Packet;
    }

    public void setPacket(int[] packet) {
        Packet = packet;
    }

    public float[] getMemory() {
        return Memory;
    }

    public void setMemory(float[] memory) {
        Memory = memory;
    }

    public int[] getPercent() {
        return percent;
    }

    public void setPercent(int[] percent) {
        this.percent = percent;
    }
}
