


package com.rw.machines.network.server;
import com.rw.machines.network.common.*;
import com.rw.machines.network.common.exceptions.*;
import java.net.*;
import java.io.*;

public class RequestProcessor extends Thread
{
private RequestHandlerInterface requestHandler;
private Socket socket;

RequestProcessor(Socket socket,RequestHandlerInterface requestHandler)
{
this.socket=socket;
this.requestHandler=requestHandler;
start();
}
public void run()
{
try
{
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
int bytesToReceive=1024;
byte tmp[]=new byte[1024];
byte header[]=new byte[1024];
int bytesReadCount;
int i,j,k;
i=0;
j=0;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
header[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
int requestLength=0;
i=1;
j=1023;
while(j>=0)
{
requestLength=requestLength+(header[j]*i);
i=i*10;
j--;
}
System.out.println("header has been received: "+requestLength);
byte ack[]=new byte[1];
ack[0]=1;
os.write(ack);
os.flush();
System.out.println("Ack has been sent");
byte request[]=new byte[requestLength];
bytesToReceive=requestLength;
i=0;
j=0;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
System.out.println(bytesReadCount);
if(bytesReadCount==-1) continue;
for(k=0;k<bytesReadCount;k++)
{
request[i]=tmp[k];
i++;
}
System.out.println("request array built");
j=j+bytesReadCount;
System.out.println(j);
}
System.out.println("Request has been received");
ack[0]=1;
os.write(ack);
os.flush();
System.out.println("Ack has been sent");
ByteArrayInputStream bais=new ByteArrayInputStream(request);
ObjectInputStream ois=new ObjectInputStream(bais);
Request requestObject=(Request)ois.readObject();
Response response=requestHandler.process(requestObject);
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(response);
oos.flush();
byte objectByte[]=baos.toByteArray();
int responseLength=objectByte.length;
int x;
i=1023;
x=responseLength;
header=new byte[1024];
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
os.write(header,0,1024);
os.flush();
System.out.println("Response Header has been sent");
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
System.out.println("Ack has been received");
int bytesToSend=responseLength;
int chunkSize=1024;
j=0;
while(j<bytesToSend)
{
if((bytesToSend-j)<chunkSize) chunkSize=(bytesToSend-j);
System.out.println(chunkSize);
os.write(objectByte,j,chunkSize);
os.flush();
j=j+chunkSize;
System.out.println(j);
}
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1) continue;
break;
}
System.out.println("Response has been sent");
socket.close();
}catch(Exception e)
{
System.out.println(e);
}
}
}