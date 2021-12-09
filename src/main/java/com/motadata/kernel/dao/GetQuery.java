package com.motadata.kernel.dao;

import java.util.HashMap;

public class GetQuery
{
    HashMap<Short, String> properties = new HashMap<Short, String>();

    public GetQuery()
    {
        properties.put(CommonConstant.SELECT, "SELECT");

        properties.put(CommonConstant.UPDATE, "UPDATE");

        properties.put(CommonConstant.DELETE, "DELETE");

        properties.put(CommonConstant.COLUMN_NAME, "*");

        properties.put(CommonConstant.TABLE_NAME, "TB_USER");

        properties.put(CommonConstant.SET_CONDITION, "SET USER = 'abc'");

        properties.put(CommonConstant.WHERE_CONDITION, "WHERE ID = ");
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
                case 1 : query = property.get(CommonConstant.SELECT) + " " + property.get(CommonConstant.COLUMN_NAME) + " from " + property.get(CommonConstant.TABLE_NAME);

                         break;

                case 2 : query = property.get(CommonConstant.UPDATE) + " " + property.get(CommonConstant.TABLE_NAME) + " " + property.get(CommonConstant.SET_CONDITION) + " " + property.get(CommonConstant.WHERE_CONDITION) + 1;

                         break;

                case 3 : query = property.get(CommonConstant.DELETE) + " from " + property.get(CommonConstant.TABLE_NAME) + " " + property.get(CommonConstant.WHERE_CONDITION) + 1;

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
