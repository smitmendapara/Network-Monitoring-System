package action.dao;

import java.sql.*;

/**
 * Created by smit on 31/12/21.
 */
public class UserDAO
{
    static String URL = "jdbc:h2:tcp://localhost/configdb;" +
                        "DATABASE_TO_UPPER=false;IGNORECASE=TRUE;" +
                        "MODE=PostgreSQL;AUTO_RECONNECT=TRUE;" +
                        "AUTO_SERVER=TRUE;MV_STORE=FALSE";

    static String USER = "motadata";

    static String PASS = "motadata";

    static String select_query = "SELECT * FROM TB_USER WHERE USER = ? AND PASSWORD = ?";

    static String insert_query = "INSERT INTO TB_USER(USER, PASSWORD) VALUES(?, ?)";

    public static boolean check(String username, String password)
    {
        boolean status = true;

        PreparedStatement statement = null;

        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            statement = connection.prepareStatement(select_query);

            statement.setString(1, username);

            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                return true;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            status = false;
        }
        finally
        {
            try
            {
                if (statement != null && !statement.isClosed())
                {
                    statement.close();
                }
            }
            catch (Exception ignored)
            {

            }
        }
        return status;
    }

    public static boolean enter(String username, String password)
    {
        boolean status = true;

        try
        {
            Class.forName("org.h2.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASS);

            PreparedStatement statement = connection.prepareStatement(insert_query);

            statement.setString(1, username);

            statement.setString(2, password);

            if (statement.execute())
            {
                return true;
            }

            statement.close();

        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            status = false;
        }
        return status;
    }
}
