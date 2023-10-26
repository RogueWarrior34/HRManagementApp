package com.rw.machines.hr.server.main;
import com.rw.machines.hr.server.*;
import com.rw.machines.network.common.exceptions.*;
import com.rw.machines.network.server.*;
public class Main
{
public static void main(String gg[])
{
try
{
RequestHandler requestHandler=new RequestHandler();
NetworkServer networkServer;
networkServer=new NetworkServer(requestHandler);
networkServer.start();
}catch(NetworkException networkException)
{
System.out.println(networkException);
}
}
}