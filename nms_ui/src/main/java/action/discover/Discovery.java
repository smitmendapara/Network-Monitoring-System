package action.discover;

import bean.DiscoverBean;

import com.opensymphony.xwork2.ModelDriven;
import dao.DAO;

import util.CommonUtil;
import service.CommonServiceProvider;

import util.CommonConstantUI;
import util.Logger;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class Discovery implements ModelDriven<DiscoverBean>
{
    private DiscoverBean discoverBean = new DiscoverBean();

    private static final Logger _logger = new Logger();

    // add discover device
    public String insertDevice()
    {
        CommonServiceProvider commonServiceProvider = new CommonServiceProvider();

        try
        {
            if (commonServiceProvider.validIP(discoverBean.getIp()))
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

                discoverBean.setFlag(commonServiceProvider.addDevice(discoverBean.getName(), discoverBean.getIp(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword(), discoverBean.getDeviceType()));
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

        return CommonConstantUI.SUCCESS;
    }

    // execute device discovery
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

            discoverBean.setFlag(CommonUtil.putDiscoverBean(discoverBean));
        }
        catch (Exception exception)
        {
            _logger.error("discovery not executed.", exception);
        }

        return CommonConstantUI.SUCCESS;
    }

    // delete particular device
    public String deleteDiscoverData()
    {
        DAO dao = new DAO();

        try
        {
            discoverBean.setFlag(dao.deleteDiscoverTableData(discoverBean.getId()));
        }
        catch (Exception exception)
        {
            _logger.error("discovery device not deleted.", exception);
        }

        return CommonConstantUI.SUCCESS;
    }

    // get discovered device details
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
            _logger.error("discover table data not fetched.", exception);
        }

        return CommonConstantUI.SUCCESS;
    }

    // set edit device details
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
            _logger.error("edit device details not fetched.", exception);
        }

        return CommonConstantUI.SUCCESS;
    }

    // update device details
    public String updateDeviceData()
    {
        DAO dao = new DAO();

        try
        {
            discoverBean.setFlag(dao.updatedDeviceData(discoverBean.getId(), discoverBean.getIp(), discoverBean.getDeviceType(), discoverBean.getName(), discoverBean.getDiscoveryUsername(), discoverBean.getDiscoveryPassword()));
        }
        catch (Exception exception)
        {
            _logger.error("device not updated.", exception);
        }

        return CommonConstantUI.SUCCESS;
    }

    @Override
    public DiscoverBean getModel()
    {
        return discoverBean;
    }
}
