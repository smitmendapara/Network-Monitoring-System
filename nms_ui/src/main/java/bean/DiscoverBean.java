package bean;

import javax.websocket.Session;
import java.util.List;

public class DiscoverBean
{
    private int id;

    private String ip;

    private String name;

    private String discoveryUsername;

    private String discoveryPassword;

    private String deviceType;

    private boolean flag;

    private boolean ipValid;

    private Session session;

    private List<DiscoverBean> beanList;

    public List<DiscoverBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DiscoverBean> beanList) {
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isIpValid() {
        return ipValid;
    }

    public void setIpValid(boolean ipValid) {
        this.ipValid = ipValid;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
