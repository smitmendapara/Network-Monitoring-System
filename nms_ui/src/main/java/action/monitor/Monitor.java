package action.monitor;

import bean.MonitorBean;

import com.opensymphony.xwork2.ModelDriven;

import service.MonitorService;
import util.CommonConstant;
import util.Logger;

public class Monitor implements ModelDriven<MonitorBean>
{
    private MonitorBean monitorBean = new MonitorBean();

    private static final Logger _logger = new Logger();

    // monitor form for provision
    public String loadMonitorData()
    {
        try
        {
            MonitorService.loadMonitorDeviceData(monitorBean);
        }
        catch (Exception exception)
        {
            _logger.warn("monitor data not fetched.");
        }

        return CommonConstant.SUCCESS;
    }

    // provision device
    public String provisionMonitor()
    {
        try
        {
            MonitorService.provisionMonitorDevice(monitorBean);
        }
        catch (Exception exception)
        {
            _logger.error("device not monitored.", exception);
        }

        return CommonConstant.SUCCESS;
    }

    // get monitor table data
    public String getMonitorData()
    {
        try
        {
            MonitorService.getMonitorDeviceData(monitorBean);
        }
        catch (Exception exception)
        {
            _logger.warn("monitor table data not fetched.");
        }

        return CommonConstant.SUCCESS;
    }

    // delete monitor device
    public String deleteMonitorData()
    {
        try
        {
            MonitorService.deleteMonitorDevice(monitorBean);
        }
        catch (Exception exception)
        {
            _logger.error("monitor device not deleted.", exception);
        }

        return CommonConstant.SUCCESS;
    }

    @Override
    public MonitorBean getModel() {
        return monitorBean;
    }
}
