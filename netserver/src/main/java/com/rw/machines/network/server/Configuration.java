package com.rw.machines.network.server;
import com.rw.machines.network.common.exceptions.*;
import org.xml.sax.*;
import javax.xml.xpath.*;
import java.io.*;

class Configuration
{
private static int port=-1;
private static boolean malformed=false;
private static boolean fileMissing=false;
static
{
try
{
File file=new File("server.xml");
if(file.exists())
{
InputSource inputSource=new InputSource("server.xml");
XPath xPath=XPathFactory.newInstance().newXPath();
String port=xPath.evaluate("//server/@port",inputSource);
Configuration.port=Integer.parseInt(port);
}
else
{
fileMissing=true;
}
}catch(Exception exception)
{
malformed=true;
}
}
public static int getPort() throws NetworkException
{
if(fileMissing) throw new NetworkException("server.xml is missing.");
if(malformed) throw new NetworkException("server.xml is not configured correctly.");
if(port<0 || port>49151) throw new NetworkException("server.xml contains invalid port number.");
return port;
}
}