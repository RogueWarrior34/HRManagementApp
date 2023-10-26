package com.rw.machines.hr.server;
import com.rw.machines.network.server.*;
import com.rw.machines.network.common.*;
import com.rw.machines.hr.bl.exceptions.*;
import com.rw.machines.hr.bl.interfaces.managers.*;
import com.rw.machines.hr.bl.interfaces.pojo.*;
import com.rw.machines.hr.bl.managers.*;


public class RequestHandler implements RequestHandlerInterface
{
private DesignationManagerInterface designationManager;
public RequestHandler()
{
try
{
designationManager=DesignationManager.getDesignationManager();
}catch(BLException blException)
{
//do nothing
}
}

public Response process(Request request)
{
if(designationManager==null)
{
//later
}
String manager=request.getManager();
String action=request.getAction();
Object [] arguments=request.getArguments();
Response response=new Response();
if(manager.equals("DesignationManager"))
{
DesignationInterface designation=null;
if(action.equals("getAllDesignations"))
{
Object result=designationManager.getAllDesignations();
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}//getAll ends
if(action.equals("addDesignation"))
{
designation=(DesignationInterface)arguments[0];
try
{
designationManager.addDesignation(designation);
response.setSuccess(true);
response.setResult(designation);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
}
}//add ends
if(action.equals("updateDesignation"))
{
designation=(DesignationInterface)arguments[0];
try
{
designationManager.updateDesignation(designation);
response.setSuccess(true);
response.setResult(null);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
}
}//update ends
if(action.equals("removeDesignation"))
{
int code=(int)arguments[0];
try
{
designationManager.removeDesignation(code);
response.setSuccess(true);
response.setResult(null);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
}
}//remove ends
if(action.equals("getDesignationByCode"))
{
int code=(int)arguments[0];
try
{
Object result=designationManager.getDesignationByCode(code);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
}
}//getDesignationByCode ends
if(action.equals("getDesignationByTitle"))
{
String title=(String)arguments[0];
try
{
Object result=designationManager.getDesignationByTitle(title);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}catch(BLException blException)
{
response.setSuccess(false);
response.setResult(null);
response.setException(blException);
}
}//getDesignationByTitle ends
if(action.equals("getDesignationCount"))
{
Object result=designationManager.getDesignationCount();
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}//getDesignationCount ends
if(action.equals("designationCodeExists"))
{
int code=(int)arguments[0];
Object result=designationManager.designationCodeExists(code);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}//designationCodeExists ends
if(action.equals("designationTitleExists"))
{
String title=(String)arguments[0];
Object result=designationManager.designationTitleExists(title);
response.setSuccess(true);
response.setResult(result);
response.setException(null);
}//designationTitleExists ends
}//DesignationManager ends
return response;
}
}