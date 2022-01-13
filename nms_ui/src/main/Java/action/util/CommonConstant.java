package action.util;

import java.io.File;

/**
 * Created by smit on 3/1/22.
 */
public class CommonConstant
{
    public static final String CURRENT_DIR = System.getProperty("user.dir");

    public static final String PATH_SEPARATOR = File.separator;

    public static final String NULL = null;

    public static final String DATABASE_URL = "jdbc:h2:tcp://localhost/configdb;" +
                                              "DATABASE_TO_UPPER=false;IGNORECASE=TRUE;" +
                                              "MODE=PostgreSQL;AUTO_RECONNECT=TRUE;" +
                                              "AUTO_SERVER=TRUE;MV_STORE=FALSE";;
}
