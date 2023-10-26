package com.rw.machines.hr.dl.dao;

import com.rw.machines.hr.dl.exceptions.*;
import com.rw.machines.hr.dl.interfaces.dao.*;
import com.rw.machines.hr.dl.interfaces.dto.*;
import com.rw.machines.hr.dl.dto.*;
import com.rw.machines.hr.dl.dao.*;
import com.rw.machines.enums.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;

public class EmployeeDAO implements EmployeeDAOInterface
{
private final static String FILE_NAME="employee.data";

public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee is null.");
String employeeId;
String name=employeeDTO.getName();
if(name==null) throw new DAOException("name is null.");
name=name.trim();
if(name.length()==0) throw new DAOException("Length of name is zero.");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("Invalid designation code: "+designationCode);
DesignationDAOInterface designationDAO=new DesignationDAO();
boolean isDesignationCodeValid=designationDAO.codeExists(designationCode);
if(isDesignationCodeValid==false) throw new DAOException("Invalid designation code: "+designationCode);
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) throw new DAOException("Date is null.");
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("Basic salary is null.");
if(basicSalary.signum()==-1) throw new DAOException("Basic salary is negative.");
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null) throw new DAOException("PAN number is null.");
panNumber=panNumber.trim();
if(panNumber.length()==0) throw new DAOException("Length of PAN number is zero.");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) throw new DAOException("Aadhar card number is null.");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) throw new DAOException("Length of Aadhar card number is zero.");
try
{
int lastGeneratedEmployeeId=10000000;
String lastGeneratedEmployeeIdString="";
int recordCount=0;
String recordCountString="";
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
lastGeneratedEmployeeIdString=String.format("%-10s","10000000");
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
recordCountString=String.format("%-10s","0");
randomAccessFile.writeBytes(recordCountString+"\n");
}
else
{
lastGeneratedEmployeeId=Integer.parseInt(randomAccessFile.readLine().trim());
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
}
boolean panNumberExists=false;
boolean aadharCardNumberExists=false;
String fPANNumber, fAadharCardNumber;
int x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(x=1; x<=7; x++) randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(panNumberExists==false && fPANNumber.equalsIgnoreCase(panNumber)) panNumberExists=true;
if(aadharCardNumberExists==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber)) aadharCardNumberExists=true;
if(panNumberExists && aadharCardNumberExists) break;
}
if(panNumberExists && aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("PANNumber("+panNumber+") and AadharCardNumber("+aadharCardNumber+") exists.");
}
if(panNumberExists)
{
randomAccessFile.close();
throw new DAOException("PANNumber("+panNumber+") exists.");
}
if(aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("AadharCardNumber("+aadharCardNumber+") exists.");
}
lastGeneratedEmployeeId++;
employeeId="A"+lastGeneratedEmployeeId;
recordCount++;
randomAccessFile.writeBytes(employeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(designationCode+"\n");
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
randomAccessFile.writeBytes(simpleDateFormat.format(dateOfBirth)+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
randomAccessFile.seek(0);
lastGeneratedEmployeeIdString=String.format("%-10d",lastGeneratedEmployeeId);
recordCountString=String.format("%-10d",recordCount);
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.close();
employeeDTO.setEmployeeId(employeeId);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee is null.");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("EmployeId is null.");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Length of employeeId is zero.");
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Name is null.");
name=name.trim();
if(name.length()==0) throw new DAOException("Length of name is zero.");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("Invalid designation code: "+designationCode);
DesignationDAOInterface designationDAO=new DesignationDAO();
boolean isDesignationCodeValid=designationDAO.codeExists(designationCode);
if(isDesignationCodeValid==false) throw new DAOException("Invalid designation code: "+designationCode);
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) throw new DAOException("Date is null.");
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("Basic salary is null.");
if(basicSalary.signum()==-1) throw new DAOException("Basic salary is negative.");
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null) throw new DAOException("PAN number is null.");
panNumber=panNumber.trim();
if(panNumber.length()==0) throw new DAOException("Length of PAN number is zero.");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) throw new DAOException("Aadhar card number is null.");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) throw new DAOException("Length of Aadhar card number is zero.");
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) throw new DAOException("Invalid EmployeeId.");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("Invalid EmployeeId.");
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
if(recordCount==0) throw new DAOException("Invalid EmployeeId.");
String fEmployeeId, fName, fPANNumber, fAadharCardNumber;
char fGender;
int fDesignationCode,x;
boolean fIsIndian;
BigDecimal fBasicSalary;
Date fDateOfBirth;
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
boolean employeeIdFound=false;
boolean panNumberFound=false;
boolean aadharCardNumberFound=false;
boolean panNumberExists=false;
boolean aadharCardNumberExists=false;
String panNumberFoundAgainstEmployeeId="";
String aadharCardNumberFoundAgainstEmployeeId="";
long foundAt=0;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
if(employeeIdFound==false) foundAt=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
for(x=1;x<=6;x++) randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(employeeIdFound==false && fEmployeeId.equalsIgnoreCase(employeeId)) employeeIdFound=true;
if(panNumberFound==false && fPANNumber.equalsIgnoreCase(panNumber))
{
panNumberFound=true;
panNumberFoundAgainstEmployeeId=fEmployeeId;
}
if(aadharCardNumberFound==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
aadharCardNumberFound=true;
aadharCardNumberFoundAgainstEmployeeId=fEmployeeId;
}
if(employeeIdFound && panNumberFound && aadharCardNumberFound) break;
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid employeeId: "+employeeId);
}
if(panNumberFound && panNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeId)==false) panNumberExists=true;
if(aadharCardNumberFound && aadharCardNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeId)==false) aadharCardNumberExists=true;
if(panNumberExists && aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("PANNumber: "+panNumber+" and Aadhar Card Number: "+aadharCardNumber+" exist.");
}
if(panNumberExists)
{
randomAccessFile.close();
throw new DAOException("PANNumber: "+panNumber+" exists.");
}
if(aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("Aadhar Card Number: "+aadharCardNumber+" exists.");
}
randomAccessFile.seek(foundAt);
for(x=1;x<=9;x++) randomAccessFile.readLine();
File tmpFile=new File("tmp.tmp");
if(file.exists()) file.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
randomAccessFile.seek(foundAt);
randomAccessFile.writeBytes(employeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(designationCode+"\n");
randomAccessFile.writeBytes(simpleDateFormat.format(dateOfBirth)+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}


public void delete(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("EmployeId is null.");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Length of employeeId is zero.");
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) throw new DAOException("Invalid EmployeeId.");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("Invalid EmployeeId.");
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
if(recordCount==0) throw new DAOException("Invalid EmployeeId.");
String fEmployeeId;
int x;
boolean employeeIdFound=false;
long foundAt=0;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
if(employeeIdFound==false) foundAt=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
for(x=1;x<=8;x++) randomAccessFile.readLine();
if(employeeIdFound==false && fEmployeeId.equalsIgnoreCase(employeeId)) employeeIdFound=true;
if(employeeIdFound) break;
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid employeeId: "+employeeId);
}
randomAccessFile.seek(foundAt);
for(x=1;x<=9;x++) randomAccessFile.readLine();
File tmpFile=new File("tmp.tmp");
if(file.exists()) file.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
randomAccessFile.seek(0);
randomAccessFile.readLine();
recordCount--;
String recordCountString=String.format("%-10d",recordCount);
randomAccessFile.writeBytes(recordCountString+"\n");
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public Set<EmployeeDTOInterface> getAll() throws DAOException
{
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) return employees;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return employees;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0) return employees;
EmployeeDTOInterface employeeDTO;
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
char fGender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(randomAccessFile.readLine());
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
try
{
employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
}catch(ParseException parseException)
{
throw new DAOException(parseException.getMessage());
}
fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
else
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
employees.add(employeeDTO);
}
randomAccessFile.close();
return employees;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public Set<EmployeeDTOInterface> getAllByDesignationCode(int designationCode) throws DAOException
{
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) return employees;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return employees;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0) return employees;
EmployeeDTOInterface employeeDTO;
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
String fEmployeeId, fName;
int fDesignationCode,x;
char fGender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
employeeDTO=new EmployeeDTO();
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode)
{
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
try
{
employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
}catch(ParseException parseException)
{
throw new DAOException(parseException.getMessage());
}
fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
else
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
employees.add(employeeDTO);
}
for(x=1;x<=6;x++) randomAccessFile.readLine();
}
randomAccessFile.close();
return employees;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public boolean isDesignationAlloted(int designationCode) throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return false;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
if(recordCount==0) return false;
int fDesignationCode, x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(designationCode==fDesignationCode)
{
randomAccessFile.close();
return true;
}
for(x=1;x<=6;x++) randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Invalid employeeId.");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Invalid employeeId.");
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) throw new DAOException("Invalid employeeId.");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("Invalid employeeId.");
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
if(recordCount==0) throw new DAOException("Invalid employeeId.");
String fEmployeeId="";
int x;
EmployeeDTOInterface employeeDTO;
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
char fGender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
try
{
employeeDTO.setDateOfBirth(simpleDateFormat.parse(randomAccessFile.readLine()));
}catch(ParseException parseException)
{
throw new DAOException(parseException.getMessage());
}
fGender=randomAccessFile.readLine().charAt(0);
if(fGender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
else
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
randomAccessFile.close();
return employeeDTO;
}
for(x=1;x<=8;x++) randomAccessFile.readLine();
}
randomAccessFile.close();
throw new DAOException("Invalid employeeId.");
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
if(panNumber==null) throw new DAOException("Invalid PANNumber.");
panNumber=panNumber.trim();
if(panNumber.length()==0) throw new DAOException("Invalid PANNumber.");
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) throw new DAOException("Invalid PANNumber.");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("Invalid PANNumber.");
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
if(recordCount==0) throw new DAOException("Invalid PANNumber.");
String fEmployeeId, fName, fPANNumber, fAadharCardNumber;
char fGender;
int fDesignationCode;
boolean fIsIndian;
BigDecimal fBasicSalary;
Date fDateOfBirth;
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
EmployeeDTOInterface employeeDTO;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
try
{
fDateOfBirth=simpleDateFormat.parse(randomAccessFile.readLine());
}catch(ParseException parseException)
{
throw new DAOException(parseException.getMessage());
}
fGender=randomAccessFile.readLine().charAt(0);
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fPANNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fPANNumber.equalsIgnoreCase(panNumber))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setDateOfBirth(fDateOfBirth);
if(fGender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
else
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setIsIndian(fIsIndian);
employeeDTO.setPANNumber(fPANNumber);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
randomAccessFile.close();
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid PANNumber.");
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}


public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null) throw new DAOException("Invalid AadharCardNumber.");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) throw new DAOException("Invalid AadharCardNumber.");
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) throw new DAOException("Invalid AadharCardNumber.");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("Invalid AadharCardNumber.");
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
if(recordCount==0) throw new DAOException("Invalid AadharCardNumber.");
String fEmployeeId, fName, fPANNumber, fAadharCardNumber;
char fGender;
int fDesignationCode;
boolean fIsIndian;
BigDecimal fBasicSalary;
Date fDateOfBirth;
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
EmployeeDTOInterface employeeDTO;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
try
{
fDateOfBirth=simpleDateFormat.parse(randomAccessFile.readLine());
}catch(ParseException parseException)
{
throw new DAOException(parseException.getMessage());
}
fGender=randomAccessFile.readLine().charAt(0);
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fPANNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setDateOfBirth(fDateOfBirth);
if(fGender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
else
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setIsIndian(fIsIndian);
employeeDTO.setPANNumber(fPANNumber);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
randomAccessFile.close();
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid AadharCardNumber.");
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}


public boolean employeeIdExists(String employeeId) throws DAOException
{
if(employeeId==null) return false;
employeeId=employeeId.trim();
if(employeeId.length()==0) return false;
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return false;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
if(recordCount==0) throw new DAOException("Invalid employeeId.");
String fEmployeeId="";
int x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
randomAccessFile.close();
return true;
}
for(x=1;x<=8;x++) randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

public boolean panNumberExists(String panNumber) throws DAOException
{
if(panNumber==null) return false;
panNumber=panNumber.trim();
if(panNumber.length()==0) return false;
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return false;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
if(recordCount==0) return false;
String fPANNumber;
int x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(x=1;x<=7;x++) randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
randomAccessFile.readLine();
if(fPANNumber.equalsIgnoreCase(panNumber))
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


public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null) return false;
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) return false;
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return false;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
if(recordCount==0) return false;
String fAadharCardNumber;
int x;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(x=1;x<=8;x++) randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
randomAccessFile.readLine();
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
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
File file=new File(FILE_NAME);
if(!(file.exists())) return 0;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return 0;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine());
randomAccessFile.close();
return recordCount;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}


public int getCountByDesignationCode(int designationCode) throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!(file.exists())) return 0;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return 0;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0) return 0;
int fDesignationCode,x;
int count=0;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode)
{
count++;
}
for(x=1;x<=6;x++) randomAccessFile.readLine();
}
randomAccessFile.close();
return count;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
}