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

------------------------------------ Server -------------------------

-> 45.116.206.194

-> 122.170.103.210

-> ssh root@172.16.8.182

-> ssh osboxes@172.16.11.100

----------------------------------- error -----------------------------------

-> type cast error can not be converted into ArrayList of HashMap (change data type)

-> data conversion error (sql take string value in single qoute)

-> unsupported mvcc setting in database url (mvcc=false to remove mvcc and clean project -> clean compile package)

-> not print resultSet so use string for that solution

-> channel not connected (timeout : 20) 

-> The Channel.connect() call usually returns in under 100 ms, but when this error ocurrs the call hangs for more than 20 seconds before throwing the exception. (timeout : 30)

------------------------------------------------ SQL Queries --------------------------------------------

-> CREATE TABLE TB_MONITOR(ip varchar(20), response varchar(20), username varchar(20), password varchar(20), device varchar(20));

-> create table TB_DISCOVER(Id int, Name varchar(20), IP varchar(20), Username varchar(20), Password varchar(20), Device varchar(10), Response varchar(300), Status varchar(10));

-> create table TB_MONITOR(Id int, Name varchar(20), IP varchar(20), Profile varchar(20), DeviceType varchar(10), Response varchar(300), Status varchar(10), CurrentTime varchar(40));

-> create table TB_RESULT(Id int, IP varchar(20), Profile varchar(20), DeviceType varchar(20), Response varchar(300))

-> CREATE TABLE TB_DATADUMP(Id int, IP varchar(20), Packet varchar(10), Memory double, Device varchar(20), CurrentTime varchar(40))

-> insert into TB_DISCOVER(Name, IP, Username, Password, Device) values('Test', '127.0.0.1', 'null', 'null', 'Ping'); // for auto increment

-> ALTER TABLE TB_DISCOVER MODIFY COLUMN Id INT auto_increment

-> ALTER TABLE TB_MONITOR ADD Status VARCHAR(10);

-> ALTER TABLE TB_MONITOR ADD CurrentTime DATETIME DEFAULT CURRENT_TIMESTAMP;

-> ALTER TABLE TB_RESULT DROP COLUMN CurrentTime
