package com.rw.machines.hr.dl.dao;

import com.rw.machines.hr.dl.exceptions.*;
import com.rw.machines.hr.dl.interfaces.dto.*;
import com.rw.machines.hr.dl.interfaces.dao.*;
import com.rw.machines.hr.dl.dto.*;
import com.rw.machines.hr.dl.dao.*;
import java.util.*;
import java.io.*;

public class DesignationDAO implements DesignationDAOInterface
{
private final static String FILE_NAME="designation.data";

public void add(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation is null.");
String title= designationDTO.getTitle();
if(title==null) throw new DAOException("Designation is null.");
title=title.trim();
if(title.length()==0) throw new DAOException("Length of title is zero.");
try
{
File file= new File(FILE_NAME);
RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
int lastGeneratedCode=0;
int recordCount=0;
String lastGeneratedCodeString="";
String recordCountString="";
if(randomAccessFile.length()==0)
{
lastGeneratedCodeString="0";
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString="0";
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.writeBytes(lastGeneratedCodeString+"\n");
randomAccessFile.writeBytes(recordCountString+"\n");
}
else
{
lastGeneratedCodeString=randomAccessFile.readLine().trim();
recordCountString=randomAccessFile.readLine().trim();
lastGeneratedCode=Integer.parseInt(lastGeneratedCodeString);
recordCount=Integer.parseInt(recordCountString);
}
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
throw new DAOException("Designation :"+title+" exists");
}
}
int code=lastGeneratedCode+1;
randomAccessFile.writeBytes(String.valueOf(code)+"\n");
randomAccessFile.writeBytes(title+"\n");
designationDTO.setCode(code);
lastGeneratedCode++;
recordCount++;
lastGeneratedCodeString=String.valueOf(lastGeneratedCode);
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.seek(0);
randomAccessFile.writeBytes(lastGeneratedCodeString+"\n");
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public void update(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation is null.");
int code= designationDTO.getCode();
String title= designationDTO.getTitle();
if(code<=0) throw new DAOException("Invalid code: "+code);
if(title==null) throw new DAOException("Designation is null.");
title=title.trim();
if(title.length()==0) throw new DAOException("Length of title is zero.");
try
{
File file= new File(FILE_NAME);
if(!(file.exists())) throw new DAOException("Invalid code: "+code);
RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code: "+code);
}
int fCode;
String fTitle;
randomAccessFile.readLine();
randomAccessFile.readLine();
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
if(fCode==code)
{
found=true;
break;
}
randomAccessFile.readLine();
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid code: "+code);
}
randomAccessFile.seek(0);
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode!=code && fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
throw new DAOException("Designation: "+title+" exists.");
}
}
File tmpFile= new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
randomAccessFile.seek(0);
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode!=code)
{
tmpRandomAccessFile.writeBytes(String.valueOf(fCode)+"\n");
tmpRandomAccessFile.writeBytes(fTitle+"\n");
}
else
{
tmpRandomAccessFile.writeBytes(String.valueOf(code)+"\n");
tmpRandomAccessFile.writeBytes(title+"\n");
}
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public void delete(int designationCode) throws DAOException
{
int code= designationCode;
if(code<=0) throw new DAOException("Invalid code: "+code);
try
{
File file= new File(FILE_NAME);
if(!(file.exists())) throw new DAOException("Invalid code: "+code);
RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code: "+code);
}
int fCode;
String fTitle;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
if(fCode==code)
{
found=true;
break;
}
randomAccessFile.readLine();
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid code: "+code);
}
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
if(employeeDAO.isDesignationAlloted(designationCode))
{
randomAccessFile.close();
throw new DAOException("Designation: "+designationCode+" is alloted to employees, alloted designation cannot be deleted.");
}
recordCount--;
File tmpFile= new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
randomAccessFile.seek(0);
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
String recordCountString="";
recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";
tmpRandomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode!=code)
{
tmpRandomAccessFile.writeBytes(String.valueOf(fCode)+"\n");
tmpRandomAccessFile.writeBytes(fTitle+"\n");
}
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public Set<DesignationDTOInterface> getAll() throws DAOException
{
Set<DesignationDTOInterface> designations=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) return designations;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return designations;
int recordCount=0;
randomAccessFile.readLine();
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0) return designations;
int fCode;
String fTitle;
DesignationDTOInterface designationDTO;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
designationDTO=new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
designations.add(designationDTO);
}
randomAccessFile.close();
return designations;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public DesignationDTOInterface getDesignationByCode(int designationCode) throws DAOException
{
int code=designationCode;
try
{
File file= new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid code: "+code);
RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code: "+code);
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code: "+code);
}
DesignationDTOInterface designationDTO;
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode==code)
{
designationDTO= new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
randomAccessFile.close();
return designationDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid code: "+code);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public DesignationDTOInterface getDesignationByTitle(String title) throws DAOException
{
title=title.trim();
try
{
File file= new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid designation: "+title);
RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid designation: "+title);
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid designation: "+title);
}
DesignationDTOInterface designationDTO;
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
designationDTO= new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
randomAccessFile.close();
return designationDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid designation: "+title);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}


public boolean codeExists(int designationCode) throws DAOException
{
int code=designationCode;
try
{
File file= new File(FILE_NAME);
if(file.exists()==false) return false;
RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode==code)
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}


public boolean titleExists(String title) throws DAOException
{
try
{
File file= new File(FILE_NAME);
if(file.exists()==false) return false;
RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public int getCount() throws DAOException
{
try
{
File file= new File(FILE_NAME);
if(file.exists()==false) return 0;
RandomAccessFile randomAccessFile= new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return 0;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
randomAccessFile.close();
return recordCount;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
}
