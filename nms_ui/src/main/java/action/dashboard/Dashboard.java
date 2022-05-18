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
        DashboardService dashboardService = new DashboardService();

        try
        {
            dashboardService.evaluateDeviceDashboardData(dashboardBean);
        }
        catch (Exception exception)
        {
            _logger.warn("particular device data not fetched.");
        }

        return CommonConstant.SUCCESS;
    }

    //TODO - Dao can not be the part of action class.
    // get home dashboard matrix data
    public String getHomeDashboardData()
    {
        DashboardService dashboardService = new DashboardService();

        try
        {
           dashboardService.evaluateHomeDashboardData(dashboardBean);
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
