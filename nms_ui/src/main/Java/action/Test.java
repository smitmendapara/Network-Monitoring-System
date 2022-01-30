package action;

import action.dao.UserDAO;
import action.util.CommonConstantUI;
import action.util.Logger;
import action.util.SSHConnectionUtil;

import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by smit on 18/1/22.
 */
public class Test
{
    public static void main(String[] args) {


        String string = "[Linux, ubuntu, x86_64, 8174812, 5872428, 139788, 4.4.0-131-generic, GNU/Linux, 7999484, 116808, 7882676, 33, 1.67, 1.80]";

        String str = "           1.67";

        System.out.println(str.length());

        String[] split = string.split(",");

        StringBuffer CPU_System = new StringBuffer(split[13].trim());

        CPU_System = CPU_System.deleteCharAt(CPU_System.length() - 1);

        System.out.println(split[12].trim());


//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//
//        System.out.println(timestamp.toString());

//        String str = "--- 172.161.11.112 ping statistics ---4 packets transmitted, 0 received, 100% packet loss, time 3061ms";
//        String str1= "--- 127.0.0.1 ping statistics ---4 packets transmitted, 4 received, 0% packet loss, time 3051msrtt min/avg/max/mdev = 0.020/0.053/0.067/0.021 ms";
//
//        String str2 = "172.000.00.100";
//
//
//        System.out.println(str1.substring(str1.indexOf("rtt") + 23, str1.indexOf("rtt") + 24));

//        System.out.println(str.substring(str.indexOf("received") + 10, str.indexOf("received") + 11));

//        System.out.println(str1.substring(str1.indexOf("statistics") + 14, str1.indexOf("statistics") + 15));


//        System.out.println(str.substring(str.indexOf("transmitted") + 13, str.indexOf("transmitted") + 14));


//        System.out.println(str2.length());
//
//        System.out.println(str.substring(61, 62));


//        String str = "Linux ubuntu 4.4.0-131-generic #157-Ubuntu SMP Thu Jul 12 15:51:36 UTC 2018 x86_64 x86_64 x86_64 GNU/Linux";
//
//
//        System.out.println(str.substring(str.indexOf("ubuntu") + 7, 30));
//
////        System.out.println(str.indexOf("GNU"));
//
//        System.out.println(str.substring(str.indexOf("GNU"), 106));

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
//            System.out.println(commandOutput);

//            System.out.println(commandOutput);
//
//            String[] split = commandOutput.split("\n");
//
//            System.out.println(split[1].replaceAll(" ", ","));
//
//            String trim = split[1].substring(4, 19).trim();
//
//            String trim1 = split[1].substring(19, 31).trim();
//
//            String trim2 = split[1].substring(31, 43).trim();
//
//            String []array = {trim, trim1, trim2};
//
//            System.out.println(Arrays.asList(array));

//            String string = Arrays.toString(array);
//
//            System.out.println(string);

//            String string = "[Linux, ubuntu, x86_64, 8174812, 5507900, 222352]";
//
//            String[] responseData = string.split(",");
//
//        StringBuffer stringBuffer = new StringBuffer(responseData[5].trim());
//
//        stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
//
//        double totalMemory = Double.parseDouble(responseData[3].trim());
//
//        double usedMemory = Double.parseDouble(responseData[4].trim());
//
//        System.out.println(totalMemory);
//
//        double freeMemory = Double.parseDouble(String.valueOf(stringBuffer));
//
//        double used = (usedMemory / totalMemory) * 100;
//
//        double free = (freeMemory / totalMemory) * 100;
//
//        used = Double.parseDouble(new DecimalFormat("##.##").format(used));
//
//        free = Double.parseDouble(new DecimalFormat("##.##").format(free));
//
//        System.out.println(used + " " + free);


//            System.out.println(Integer.parseInt(split[1].substring(12, 19)) - Integer.parseInt(split[1].substring(19, 31).trim()));

//            System.out.println(commandOutput.substring(commandOutput.indexOf("Linux"), 5));
//
//            System.out.println(commandOutput.substring(commandOutput.indexOf("ubuntu"), 13));
//
//            System.out.println(commandOutput.indexOf("x86"));
//
//            System.out.println(commandOutput.substring(commandOutput.indexOf("x86"), 82));

//            _logger.info("Command output : " + commandOutput);

//        }
//        else {
//            _logger.warn("ssh object is null!");
//
//        }

    }
}
