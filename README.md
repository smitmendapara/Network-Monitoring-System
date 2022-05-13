---------------------------------------------------------------- Work Flow -----------------------------------------------------------------------

-> one ui & maven project

-> In the ui project I created one jsp file and also web.xml and struts-congif.xml file for mapping with jetty or tomcat configuration.

-> requirements for maven project : jdk, h2 dependency, h2 jar, jetty dependency, jetty jar-download
 
-> In the maven project I created kernel and logger file. 

-> Logger file for handle problems through the log file. 

-> start h2 database -> verify h2 database -> start jetty server -> load h2 database configuration -> start nsq server -> init nsq consumer

-> start scheduler and polling

-> for start from scratch for ui project maven project with archtype.webapp -> make src/main/java (source root) and /resources (resources root)

-> Apache Tomcat Server → [https://tomcat.apache.org/download-90.cgi](https://tomcat.apache.org/download-90.cgi) 

-> Apache SubnetUtils → [https://jar-download.com/artifacts/commons-net/commons-net](https://jar-download.com/artifacts/commons-net/commons-net)

-> Struts → [http://www.java2s.com/Code/Jar/s/Downloadstrutscore135jar.htm](http://www.java2s.com/Code/Jar/s/Downloadstrutscore135jar.htm)

-> NSQ → [https://nsq.io/deployment/installing.html](https://nsq.io/deployment/installing.html) 

-> Jetty Server → [https://www.eclipse.org/jetty/download.php](https://www.eclipse.org/jetty/download.php)

-> move struts.xml file in resources and all action java file in java folder

-> web.xml file in wen-inf folder

-> all jsp file in webapp folder

-> pom.xml dependencies -> struts2-json-plugin, struts2-core, javax.servlet-api, h2 (for database), commons-io (IOUtils)

-> jar file -> h2, google-http-client

-> IOUtils provide utility methods for reading, writing and copying files. The methods work with InputStream, OutputStream, Reader and Writer.

-> Date and Calender class used for time in time series bar char

-> TimeStamp class is used for getting current time at a discovery and monitoring.   

------------------------------------------------------- Server -------------------------------------------------------

-> 45.116.206.194

-> 122.170.103.210

-> ssh root@172.16.8.182

-> ssh osboxes@172.16.11.100

-------------------------------------------------------- Linux Command ------------------------------------------------------

-> uname

-> uname -a

-> uname -m

-> uname -r

-> free - memory data

-> iostat - cpu data

-> df -h - disk data

-> top -H - thread data

---------------------------------------------------------- Error ------------------------------------------------------------------

-> type cast error can not be converted into ArrayList of HashMap (change data type)

-> data conversion error (sql take string value in single qoute)

-> unsupported mvcc setting in database url (mvcc=false to remove mvcc and clean project -> clean compile package)

-> not print resultSet so use string for that solution

-> channel not connected (timeout : 20) 

-> The Channel.connect() call usually returns in under 100 ms, but when this error ocurrs the call hangs for more than 20 seconds before throwing the exception. (timeout : 30)

-> socket not established (vpn is not connected either ssh is now working in terminal)

-> null pointer exception (method parameter take null value at a calling time)

-> horg.h2.jdbc.JdbcSQLException: Connection is broken: "java.net.ConnectException: Connection refused (Connection refused): localhost"

-> Unable to load configuration. - action - file:/home/smit/IdeaProjects/nms_ui/target/nms_ui/WEB-INF/classes/struts.xml:87:90

-> Unsupported connection setting &quot;MVCC&quot; [90113-200] 90113/90113 (check your jar path in project structure)

-> com.jcraft.jsch.JSchException: UnknownHostKey: 172.16.8.68. RSA key fingerprint is f0:5b:86:bb:91:66:ec:90:f7:d5:48:80:ce:3a:10:e9

------------------------------------------------------------------ SQL Queries --------------------------------------------------------

-> CREATE TABLE TB_MONITOR(ip varchar(20), response varchar(20), username varchar(20), password varchar(20), device varchar(20));

-> create table TB_DISCOVER(Id int, Name varchar(20), IP varchar(20), Username varchar(20), Password varchar(20), Device varchar(10), Response varchar(300), Status varchar(10));

-> create table TB_MONITOR(Id int, Name varchar(20), IP varchar(20), Profile varchar(20), DeviceType varchar(10), Response varchar(300), Status varchar(10), CurrentTime varchar(40));

-> create table TB_RESULT(Id int, IP varchar(20), Profile varchar(20), DeviceType varchar(20), Response varchar(300));

-> INSERT INTO TB_MONITOR VALUES (30, 'Trial', '127.0.0.1234', 'admin', 'admin', 'Linux', '[Linux, ubuntu, x86_64, 8174812, 4935904, 1205740, 4.4.0-131-generic, GNU/Linux, 7999484, 109220, 7890264, 31, 

   1.70, 1.87]', 'Up', '2022-02-02 19:05');

-> INSERT INTO TB_RESULT VALUES (30, '127.0.0.1234', 'admin', 'Linux', '[Linux, ubuntu, x86_64, 8174812, 4935904, 1205740, 4.4.0-131-generic, GNU/Linux, 7999484, 109220, 7890264, 31, 1.70, 1.87]', 'Up', 

   '2022-02-02 19:05');

-> CREATE TABLE TB_DATADUMP(Id int, IP varchar(20), Packet varchar(10), Memory double, Device varchar(20), CurrentTime varchar(40));

-> insert into TB_DISCOVER(Name, IP, Username, Password, Device) values('Test', '127.0.0.1', 'null', 'null', 'Ping'); // for auto increment

-> ALTER TABLE TB_DISCOVER MODIFY COLUMN Id INT auto_increment;

-> ALTER TABLE TB_MONITOR ADD Status VARCHAR(10);

-> ALTER TABLE TB_MONITOR ADD CurrentTime DATETIME DEFAULT CURRENT_TIMESTAMP;

-> ALTER TABLE TB_RESULT DROP COLUMN CurrentTime;

-> DELETE FROM TB_DATADUMP LIMIT 500;

-> DELETE FROM TB_DATADUMP LIMIT SELECT COUNT(ID)/2 FROM TB_DATADUMP WHERE STATUS = 'UP'

-> DELETE FROM TB_DATADUMP LIMIT SELECT COUNT(ID)/2 FROM TB_DATADUMP WHERE STATUS = 'DOWN'

-> SELECT COUNT(IP) FROM TB_DATADUMP WHERE IP = '127.0.0.5' AND DEVICE = 'Ping' AND STATUS = 'Down'

-> SELECT COUNT(*) FROM TB_DATADUMP WHERE IP = '127.0.0.5' AND DEVICE = 'Ping'

-> SELECT STATUS, COUNT(IP) AS Frequency  FROM TB_DATADUMP WHERE IP = '172.16.8.182' GROUP BY STATUS ORDER BY STATUS DESC

-> SELECT * FROM TB_DATADUMP WHERE IP='172.16.8.182' BETWEEN '2022-02-21 09:00' AND '2022-02-22 22:41'

-> SELECT STATUS, COUNT(IP) FROM TB_DATADUMP WHERE IP = '127.0.0.5' AND DEVICE = 'Ping' AND CURRENTTIME BETWEEN '2022-02-22 17:18' AND '2022-02-23 17:18' GROUP BY STATUS ORDER BY STATUS DESC

-> ALTER TABLE TB_DATADUMP DROP RESPONSE

-------------------------------------------- SQL Injection ------------------------------------------

-> SELECT * FROM Users WHERE UserId = 105 OR 1=1;

-> SELECT * FROM Users WHERE Name ="" or ""="" AND Pass ="" or ""=""

-------------------------------------------- Top Device Details Query ------------------------------------------------

-> SELECT MAX(MEMORY) AS MAXPERCENT, IP FROM TB_DATADUMP WHERE CURRENTTIME > '2022-05-10 14:12' GROUP BY IP OERDER BY MAXPERCENT DESC LIMIT 3;

-> SELECT MAX(DISK) AS MAXPERCENT, IP FROM TB_DATADUMP WHERE CURRENTTIME > '2022-05-10 14:12' GROUP BY IP OERDER BY MAXPERCENT DESC LIMIT 3;

-> SELECT MAX(CPU) AS MAXPERCENT, IP FROM TB_DATADUMP WHERE CURRENTTIME > '2022-05-10 14:12' GROUP BY IP OERDER BY MAXPERCENT DESC LIMIT 3;

-> ALTER TABLE TB_MONITOR MODIFY Status varchar(30);


------------------------- Http to Https ---------------------------------

-> java -jar start.jar --add-to-startd=ssl

-> jetty.ssl.port=8443

-> --module=https

-> Plain Text : Hello World!

-> t8Fw6T8UV81pQfyhDkhebbz7+oiwldr1j2gHBB3L3RFTRsQCpaSnSBZ78Vme+DpDVJPvZdZUZHpzbbcqmSW1+3xXGsERHg9YDmpYk0VVDiRvw1H5miNieJeJ/FNUjgH0BmVRWII6+T4MnDwmCMZUI/orxP3HGwYCSIvyzS3MpmmSe4iaWKCOHQ==
