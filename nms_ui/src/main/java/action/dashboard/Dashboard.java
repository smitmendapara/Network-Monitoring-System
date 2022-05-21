package action.dashboard;

import com.opensymphony.xwork2.ModelDriven;

import bean.DashboardBean;

import service.DashboardService;
import util.CommonConstant;

import util.Logger;

public class Dashboard implements ModelDriven<DashboardBean>
{
    private DashboardBean dashboardBean = new DashboardBean();

    private static final Logger _logger = new Logger();

    // get particular device dashboard data
    public String getDeviceDashboardData()
    {
        try
        {
            DashboardService.evaluateDeviceDashboardData(dashboardBean);
        }
        catch (Exception exception)
        {
            _logger.warn("particular device data not fetched.");
        }

        return CommonConstant.SUCCESS;
    }

    // get home dashboard matrix data
    public String getHomeDashboardData()
    {
        try
        {
            DashboardService.evaluateHomeDashboardData(dashboardBean);
        }
        catch (Exception exception)
        {
            _logger.error("home dashboard data not fetched.", exception);
        }

        return CommonConstant.SUCCESS;
    }

    @Override
    public DashboardBean getModel()
    {
        return dashboardBean;
    }
}
