package com.motadata.kernel.dao;

import java.util.HashMap;

public class GetQuery
{
    HashMap<Short, String> properties = new HashMap<Short, String>();

    public GetQuery()
    {
        properties.put(CommonConstant_DAO.SELECT, "SELECT");

        properties.put(CommonConstant_DAO.UPDATE, "UPDATE");

        properties.put(CommonConstant_DAO.DELETE, "DELETE");

        properties.put(CommonConstant_DAO.COLUMN_NAME, "*");

        properties.put(CommonConstant_DAO.TABLE_NAME, "TB_USER");

        properties.put(CommonConstant_DAO.SET_CONDITION, "SET USER = 'abc'");

        properties.put(CommonConstant_DAO.WHERE_CONDITION, "WHERE ID = ");
    }


    public String getQuery(int value)
    {
        String query = "";

        try
        {
            HashMap<Short, String> property = new HashMap<Short, String>();

            property.putAll(properties);

            switch (value)
            {
                case 1 : query = property.get(CommonConstant_DAO.SELECT) + " " + property.get(CommonConstant_DAO.COLUMN_NAME) + " from " + property.get(CommonConstant_DAO.TABLE_NAME);

                         break;

                case 2 : query = property.get(CommonConstant_DAO.UPDATE) + " " + property.get(CommonConstant_DAO.TABLE_NAME) + " " + property.get(CommonConstant_DAO.SET_CONDITION) + " " + property.get(CommonConstant_DAO.WHERE_CONDITION) + 1;

                         break;

                case 3 : query = property.get(CommonConstant_DAO.DELETE) + " from " + property.get(CommonConstant_DAO.TABLE_NAME) + " " + property.get(CommonConstant_DAO.WHERE_CONDITION) + 1;

                         break;

                default: query = "stop";

                         break;
                }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            System.out.println("not work!");
        }

        return query;
    }
}
