package action.discover;

import bean.DiscoverBean;

import dao.DAO;

import action.helper.ServiceProvider;

import util.Logger;

import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;

import java.util.List;

public class Discovery extends ActionSupport
{
    private int id;

    private String name;

    private String ip;

    private String discoveryUsername;

    private String discoveryPassword;

    private String deviceType;

    private Object result;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    private DiscoverBean bean = null;

    private List<DiscoverBean> beanList = null;

    public List<DiscoverBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DiscoverBean> beanList) {
        this.beanList = beanList;
    }

    private static final Logger _logger = new Logger();

    public String executeDiscovery()
    {
        beanList = new ArrayList<>();

        bean = new DiscoverBean();

        ServiceProvider serviceProvider = new ServiceProvider();

        try
        {
            if (serviceProvider.addDevice(name, ip, discoveryUsername, discoveryPassword, deviceType))
            {
                bean.setFlag(Boolean.TRUE);

                beanList.add(bean);

                return "success";
            }
            else
            {
                bean.setFlag(Boolean.FALSE);

                beanList.add(bean);

                return  "error";
            }

        }
        catch (Exception exception)
        {
            _logger.error("discovery failed!", exception);
        }

        return null;
    }

    public String deleteDiscoverData()
    {
        DAO dao = new DAO();

        try
        {
            if (dao.deleteDiscoverTableData(id))
            {
                return "success";
            }
        }
        catch (Exception exception)
        {
            _logger.error("discovery device not deleted...", exception);
        }

        return "error";
    }

    public String getDiscoverData()
    {
        List<List<String>> discoverList;

        DAO dao = new DAO();

        try
        {
            beanList = new ArrayList<>();

            discoverList = dao.getDiscoverData();

            if (discoverList != null)
            {
                for (List<String> subList : discoverList)
                {
                    bean = new DiscoverBean();

                    setBeanData(subList, bean);

                    beanList.add(bean);
                }
            }
        }
        catch (Exception exception)
        {
            _logger.warn("getting data error from DiscoverBean!");
        }

        return "success";
    }

    public String editDevice()
    {
        List<String> editDataList;

        DAO dao = new DAO();

        try
        {
            beanList = new ArrayList<>();

            editDataList = dao.getEditFieldsData(ip, deviceType);

            if (editDataList != null)
            {
                bean = new DiscoverBean();

                bean.setId(Integer.parseInt(editDataList.get(0)));

                bean.setName(editDataList.get(1));

                bean.setIP(editDataList.get(2));

                bean.setUsername(editDataList.get(3));

                bean.setPassword(editDataList.get(4));

                bean.setDevice(editDataList.get(5));

                beanList.add(bean);
            }
        }
        catch (Exception exception)
        {
            _logger.error("edited data not set into the beanList...", exception);
        }

        return "success";
    }

    public String updateDeviceData()
    {
        beanList = new ArrayList<>();

        bean = new DiscoverBean();

        DAO dao = new DAO();

        try
        {
            if (dao.insertUpdatedDeviceData(id, ip, deviceType, name, discoveryUsername, discoveryPassword))
            {
                bean.setFlag(Boolean.TRUE);

                beanList.add(bean);

                return "success";
            }

            bean.setFlag(Boolean.FALSE);

            beanList.add(bean);
        }
        catch (Exception exception)
        {
            _logger.error("device not updated!", exception);
        }

        return "success";
    }

    private void setBeanData(List<String> beanDataList, DiscoverBean bean)
    {
        try
        {
            bean.setId(Integer.parseInt(beanDataList.get(0)));

            bean.setName(beanDataList.get(1));

            bean.setIP(beanDataList.get(2));

            bean.setDevice(beanDataList.get(4));
        }
        catch (Exception exception)
        {
            _logger.warn("bean list not set...");
        }
    }

}
