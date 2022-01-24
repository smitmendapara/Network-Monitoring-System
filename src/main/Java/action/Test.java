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

        String str = "--- 172.161.11.112 ping statistics ---4 packets transmitted, 0 received, 100% packet loss, time 3061ms";
        String str1= "--- 127.0.0.1 ping statistics ---4 packets transmitted, 4 received, 0% packet loss, time 3051msrtt min/avg/max/mdev = 0.020/0.053/0.067/0.021 ms";

        String str2 = "172.000.00.100";

        System.out.println(str2.length());

        System.out.println(str.substring(61, 62));


//        String str = "Linux ubuntu 4.4.0-131-generic #157-Ubuntu SMP Thu Jul 12 15:51:36 UTC 2018 x86_64 x86_64 x86_64 GNU/Linux";
//
//        System.out.println(str.length());

//        String str = "--- 127.0.0.1 ping statistics ---4 packets transmitted, 4 received, 0% packet loss, time 3051msrtt min/avg/max/mdev = 0.020/0.053/0.067/0.021 ms";
//
//        System.out.println(str.substring(33, 34)); // sent packet
//
//        System.out.println(str.substring(56, 57)); // received packet
//
//        System.out.println(str.substring(68, 69)); // packet loss
//
//        System.out.println(str.substring(118, 119));  // rtt

//        String commandOutput = null;
//
//        Logger _logger = new Logger();
//
//        SSHConnectionUtil sshConnectionUtil = SSHConnectionUtil.getNewSSHObject("172.16.8.182", 22, "root", "motadata", 30);
//
//        if(sshConnectionUtil != null)
//        {
//            commandOutput = sshConnectionUtil.executeCommand("uname -a");
//
//            _logger.info("Command output : " + commandOutput);
//        }
//        else {
//            _logger.warn("ssh object is null!");
//
//        }

    }
}
