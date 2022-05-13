package action.discover;

import monitorBean.DiscoverBean;

import com.opensymphony.xwork2.ModelDriven;
import dao.DAO;

import helper.QueueHandler;
import service.ServiceProvider;

import util.CommonConstantUI;
import util.Logger;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class Discovery implements ModelDriven<DiscoverBean>
{
    private DiscoverBean discoverBean = new DiscoverBean();

    private static final Logger _logger = new Logger();

    public String insertDevice()
    {
        ServiceProvider serviceProvider = new ServiceProvider();

        try
        {
            if (serviceProvider.validIP(discoverBean.getIp()))
            {
                if (discoverBean.getDeviceType().equals(CommonConstantUI.STRING_ZERO))
                {
                    discoverBean.setDeviceType(CommonConstantUI.PING_DEVICE);
                }
                else
                {
                    discoverBean.setDeviceType(CommonConstantUI.LINUX_DEVICE);
                }

                discoverBean.setIpValid(Boolean.TRUE);

                discoverBean.setFlag(serviceProvider.addDevice(discoverBean.getName(), discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType()));
            }
            else
            {
                discoverBean.setIpValid(Boolean.FALSE);
            }
        }
        catch (Exception exception)
        {
            _logger.error("device not inserted!", exception);
        }

        return "success";
    }

    public String executeDiscovery()
    {
        DAO dao = new DAO();

        try
        {
            List<HashMap<String, Object>> monitorDetailsList = dao.getMonitorDetails(discoverBean.getId());

            for (HashMap<String, Object> monitorDetails : monitorDetailsList)
            {
                discoverBean.setIp(monitorDetails.get("IP").toString());

                discoverBean.setDiscoveryUsername(monitorDetails.get("Username").toString());

                discoverBean.setDiscoveryPassword(monitorDetails.get("Password").toString());

                discoverBean.setDeviceType(monitorDetails.get("Device").toString());
            }

            discoverBean.setFlag(QueueHandler.putDiscoverBean(discoverBean));
        }
        catch (Exception exception)
        {
            _logger.error("discovery failed!", exception);
        }

        return "success";
    }

    public String deleteDiscoverData()
    {
        DAO dao = new DAO();

        try
        {
            discoverBean.setFlag(dao.deleteDiscoverTableData(discoverBean.getId()));
        }
        catch (Exception exception)
        {
            _logger.error("discovery device not deleted...", exception);
        }

        return "success";
    }

    public String getDiscoverData()
    {
        DAO dao = new DAO();

        List<DiscoverBean> beanList = new ArrayList<>();

        try
        {
            List<List<String>> discoverList = dao.getDiscoverData();

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
            _logger.warn("getting data error from DiscoverBean!");
        }

        return "success";
    }

    public String editDevice()
    {
        DAO dao = new DAO();

        try
        {
            List<String> editDataList = dao.getEditFieldsData(discoverBean.getIp(), discoverBean.getDeviceType());

            if (editDataList != null)
            {
                discoverBean.setName(editDataList.get(1));

                discoverBean.setDiscoveryUsername(editDataList.get(3));

                discoverBean.setDiscoveryPassword(editDataList.get(4));
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
        DAO dao = new DAO();

        try
        {
            discoverBean.setFlag(dao.insertUpdatedDeviceData(discoverBean.getId(), discoverBean.getIp(), discoverBean.getDeviceType(), discoverBean.getName(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword()));
        }
        catch (Exception exception)
        {
            _logger.error("device not updated!", exception);
        }

        return "success";
    }

    @Override
    public DiscoverBean getModel()
    {
        return discoverBean;
    }
}
