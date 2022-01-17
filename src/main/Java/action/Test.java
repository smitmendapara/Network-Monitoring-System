package action;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by smit on 12/1/22.
 */
public class Test
{
    public static void main(String[] args) throws ParseException {

        System.out.println(System.getProperty("user.dir") + "/log/");

        String s = "\n" +
                "3 packets transmitted, 3 received, 0% packet loss, time 2039ms\n" +
                "rtt min/avg/max/mdev = 0.066/0.115/0.215/0.071 ms";

        System.out.println(s.length());

        JSONParser parser = new JSONParser();

        JSONObject jsonObject = (JSONObject) parser.parse(s);

        System.out.println(jsonObject);



    }
}
