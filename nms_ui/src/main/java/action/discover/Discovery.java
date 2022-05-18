package action.discover;

import bean.DiscoverBean;

import com.opensymphony.xwork2.ModelDriven;

import service.DiscoveryService;

import util.CommonConstant;
import util.Logger;

public class Discovery implements ModelDriven<DiscoverBean>
{
    private DiscoverBean discoverBean = new DiscoverBean();

    private static final Logger _logger = new Logger();

    // add discover device
    public String insertDevice()
    {
        try
        {
            DiscoveryService.addDiscoveryDevice(discoverBean);
        }
        catch (Exception exception)
        {
            _logger.error("device not added.", exception);
        }

        return CommonConstant.SUCCESS;
    }

    // execute device discovery
    public String executeDiscovery()
    {
        try
        {
            DiscoveryService.executeDeviceDiscovery(discoverBean);
        }
        catch (Exception exception)
        {
            _logger.error("discovery not executed.", exception);
        }

        return CommonConstant.SUCCESS;
    }

    // delete particular device
    public String deleteDiscoverData()
    {
        try
        {
            DiscoveryService.deleteDiscoverDevice(discoverBean);
        }
        catch (Exception exception)
        {
            _logger.error("discovery device not deleted.", exception);
        }

        return CommonConstant.SUCCESS;
    }

    // get discovered device details
    public String getDiscoverData()
    {
        try
        {
            DiscoveryService.getDiscoverDeviceData(discoverBean);
        }
        catch (Exception exception)
        {
            _logger.error("discover table data not fetched.", exception);
        }

        return CommonConstant.SUCCESS;
    }

    // set edit device details
    public String editDevice()
    {
        try
        {
            DiscoveryService.editDeviceDetail(discoverBean);
        }
        catch (Exception exception)
        {
            _logger.error("edit device details not fetched.", exception);
        }

        return CommonConstant.SUCCESS;
    }

    // update device details
    public String updateDeviceData()
    {
        try
        {
            DiscoveryService.updateDevice(discoverBean);
        }
        catch (Exception exception)
        {
            _logger.error("device not updated.", exception);
        }

        return CommonConstant.SUCCESS;
    }

    @Override
    public DiscoverBean getModel()
    {
        return discoverBean;
    }
}
