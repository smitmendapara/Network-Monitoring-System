package action;

import action.dao.UserDAO;
import action.util.CommonConstantUI;
import action.util.Logger;
import action.util.SSHConnectionUtil;

import java.io.File;

/**
 * Created by smit on 18/1/22.
 */
public class Test
{
    public static void main(String[] args) {

        String commandOutput = null;

        Logger _logger = new Logger();

        SSHConnectionUtil sshConnectionUtil = SSHConnectionUtil.getNewSSHObject("172.16.8.182", 22, "root", "motadata", 30);

        if(sshConnectionUtil != null)
        {
            commandOutput = sshConnectionUtil.executeCommand("uname -a");

            _logger.info("Command output : " + commandOutput);
        }
        else {
            _logger.warn("ssh object is null!");

        }

    }
}
