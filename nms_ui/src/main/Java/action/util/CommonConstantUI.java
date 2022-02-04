package action.util;

import java.io.File;

public class CommonConstantUI
{
    public static final String CURRENT_DIR = System.getProperty("user.dir");

    public static final String PATH_SEPARATOR = File.separator;

    public static final String DIRECTORY_NAME = "log";

    public static final String NULL = null;

    public static final String NEW_LINE = System.lineSeparator();

    public static final String LOGGER_DATE_FORMAT = "dd-MMMM-yyyy";

    public static final String DATABASE_USERNAME = "motadata";

    public static final String DATABASE_PASSWORD = "motadata";

    public static final Double DOUBLE_ZERO = 0.0;

    public static final int SSH_PORT = 22;

    public static final int SSH_TIMEOUT = 30;

    public static final String STRING_NULL = "null";

    public static final String STRING_ZERO = "0";

    public static final String STRING_ONE = "1";

    public static final String STRING_TWO = "2";

    public static final String STRING_THREE = "3";

    public static final String STRING_FOUR = "4";

    public static final String STRING_HUNDRED = "100";

    public static final String STRING_SEVENTY_FIVE = "75";

    public static final String STRING_FIFTY = "50";

    public static final String STRING_TWENTY_FIVE = "25";

    public static final String PING_DEVICE = "Ping";

    public static final String LINUX_DEVICE = "Linux";

    public static final String PING_COMMAND = "ping -c 4 ";

    public static final String LINUX_U_NAME_COMMAND = "uname -a";

    public static final String LINUX_FREE_COMMAND = "free";

    public static final String LINUX_DISK_COMMAND = "df -h";

    public static final String LINUX_CPU_COMMAND = "iostat";

    public static final String DEVICE_UP = "Up";

    public static final String DEVICE_DOWN = "Down";

    public static final String DATABASE_URL = "jdbc:h2:tcp://localhost/configdb;" +
                                              "DATABASE_TO_UPPER=false;IGNORECASE=TRUE;" +
                                              "MODE=PostgreSQL;AUTO_RECONNECT=TRUE;" +
                                              "AUTO_SERVER=TRUE;MV_STORE=FALSE";;
}
