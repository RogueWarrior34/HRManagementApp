package com.rw.machines.hr.dl.dao;

import com.rw.machines.hr.dl.exceptions.*;
import com.rw.machines.hr.dl.interfaces.dao.*;
import com.rw.machines.hr.dl.interfaces.dto.*;
import com.rw.machines.hr.dl.dto.*;
import com.rw.machines.hr.dl.dao.*;
import com.rw.machines.enums.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.sql.*;


public class EmployeeDAO implements EmployeeDAOInterface
{
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
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
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
Connection connection=DAOConnection.getConnection();
boolean panNumberExists=false;
boolean aadharCardNumberExists=false;
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
panNumberExists=true;
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select gender from employee where aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
aadharCardNumberExists=true;
}
if(panNumberExists && aadharCardNumberExists)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("PANNumber("+panNumber+") and AadharCardNumber("+aadharCardNumber+") exists.");
}
if(panNumberExists)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("PANNumber("+panNumber+") exists.");
}
if(aadharCardNumberExists)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("AadharCardNumber("+aadharCardNumber+") exists.");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("insert into employee(name,designation_code,date_of_birth,basic_salary,gender,is_indian,pan_number,aadhar_card_number) values(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date sqlDateOfBirth=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,sqlDateOfBirth);
preparedStatement.setBigDecimal(4,basicSalary);
preparedStatement.setString(5,String.valueOf(gender));
preparedStatement.setBoolean(6,isIndian);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.executeUpdate();
resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
int generatedEmployeeId=resultSet.getInt(1);
resultSet.close();
preparedStatement.close();
connection.close();
employeeDTO.setEmployeeId("A"+(10000000+generatedEmployeeId));
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee is null.");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("EmployeId is null.");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Length of employeeId is zero.");
int actualEmployeeId=0;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-10000000;
}catch(Exception exception)
{
throw new DAOException("Invalid employee id");
}
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Name is null.");
name=name.trim();
if(name.length()==0) throw new DAOException("Length of name is zero.");
int designationCode=employeeDTO.getDesignationCode();
if(designationCode<=0) throw new DAOException("Invalid designation code: "+designationCode);
DesignationDAOInterface designationDAO=new DesignationDAO();
boolean isDesignationCodeValid=designationDAO.codeExists(designationCode);
if(isDesignationCodeValid==false) throw new DAOException("Invalid designation code: "+designationCode);
java.util.Date dateOfBirth=employeeDTO.getDateOfBirth();
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
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid EmployeeId: "+employeeId);
}
resultSet.close();
preparedStatement.close();
boolean panNumberExists=false;
boolean aadharCardNumberExists=false;
preparedStatement=connection.prepareStatement("select gender from employee where pan_number=? and employee_id<>?");
preparedStatement.setString(1,panNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
panNumberExists=true;
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("select gender from employee where aadhar_card_number=? and employee_id<>?");
preparedStatement.setString(1,aadharCardNumber);
preparedStatement.setInt(2,actualEmployeeId);
resultSet=preparedStatement.executeQuery();
if(resultSet.next())
{
aadharCardNumberExists=true;
}
if(panNumberExists && aadharCardNumberExists)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("PANNumber("+panNumber+") and AadharCardNumber("+aadharCardNumber+") exists.");
}
if(panNumberExists)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("PANNumber("+panNumber+") exists.");
}
if(aadharCardNumberExists)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("AadharCardNumber("+aadharCardNumber+") exists.");
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("update employee where name=?,designation_code=?,date_of_birth=?,basic_salary=?,gender=?,is_indian=?,pan_number=?,aadhar_card_number=? where employee_id=?");
preparedStatement.setString(1,name);
preparedStatement.setInt(2,designationCode);
java.sql.Date sqlDateOfBirth=new java.sql.Date(dateOfBirth.getYear(),dateOfBirth.getMonth(),dateOfBirth.getDate());
preparedStatement.setDate(3,sqlDateOfBirth);
preparedStatement.setBigDecimal(4,basicSalary);
preparedStatement.setString(5,String.valueOf(gender));
preparedStatement.setBoolean(6,isIndian);
preparedStatement.setString(7,panNumber);
preparedStatement.setString(8,aadharCardNumber);
preparedStatement.setInt(9,actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public void delete(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("EmployeId is null.");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Length of employeeId is zero.");
int actualEmployeeId=0;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-10000000;
}catch(Exception exception)
{
throw new DAOException("Invalid employee id");
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
throw new DAOException("Invalid EmployeeId: "+employeeId);
}
resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("delete from emmployee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public Set<EmployeeDTOInterface> getAll() throws DAOException
{
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
Connection connection=DAOConnection.getConnection();
Statement statement=connection.createStatement();
ResultSet resultSet=statement.executeQuery("select * from employee");
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDateOfBirth;
java.util.Date utilDateOfBirth;
String gender;
while(resultSet.next())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(utilDateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=resultSet.getString("gender");
employeeDTO.setGender((gender.equals("M"))?GENDER.MALE:GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
employees.add(employeeDTO);
}
resultSet.close();
statement.close();
connection.close();
return employees;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public Set<EmployeeDTOInterface> getAllByDesignationCode(int designationCode) throws DAOException
{
Set<EmployeeDTOInterface> employees=new TreeSet<>();
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select * from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
ResultSet resultSet=preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDateOfBirth;
java.util.Date utilDateOfBirth;
String gender;
while(resultSet.next())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(utilDateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=resultSet.getString("gender");
employeeDTO.setGender((gender.equals("M"))?GENDER.MALE:GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
employees.add(employeeDTO);
}
resultSet.close();
preparedStatement.close();
connection.close();
return employees;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public boolean isDesignationAlloted(int designationCode) throws DAOException
{
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false) 
{
resultSet.close();
preparedStatement.close();
connection.close();
return false;
}
resultSet.close();
preparedStatement.close();
connection.close();
return false;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("EmployeId is null.");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Length of employeeId is zero.");
int actualEmployeeId=0;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-10000000;
}catch(Exception exception)
{
throw new DAOException("Invalid employee id");
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select * from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDateOfBirth;
java.util.Date utilDateOfBirth;
String gender;
resultSet.next();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(utilDateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=resultSet.getString("gender");
employeeDTO.setGender((gender.equals("M"))?GENDER.MALE:GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
if(panNumber==null) throw new DAOException("Invalid PANNumber.");
panNumber=panNumber.trim();
if(panNumber.length()==0) throw new DAOException("Invalid PANNumber.");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select * from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet=preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDateOfBirth;
java.util.Date utilDateOfBirth;
String gender;
resultSet.next();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(utilDateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=resultSet.getString("gender");
employeeDTO.setGender((gender.equals("M"))?GENDER.MALE:GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null) throw new DAOException("Invalid AadharCardNumber.");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) throw new DAOException("Invalid AadharCardNumber.");
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select * from employee where aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
ResultSet resultSet=preparedStatement.executeQuery();
EmployeeDTOInterface employeeDTO;
java.sql.Date sqlDateOfBirth;
java.util.Date utilDateOfBirth;
String gender;
resultSet.next();
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId("A"+(1000000+resultSet.getInt("employee_id")));
employeeDTO.setName(resultSet.getString("name").trim());
employeeDTO.setDesignationCode(resultSet.getInt("designation_code"));
sqlDateOfBirth=resultSet.getDate("date_of_birth");
utilDateOfBirth=new java.util.Date(sqlDateOfBirth.getYear(),sqlDateOfBirth.getMonth(),sqlDateOfBirth.getDate());
employeeDTO.setDateOfBirth(utilDateOfBirth);
employeeDTO.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
gender=resultSet.getString("gender");
employeeDTO.setGender((gender.equals("M"))?GENDER.MALE:GENDER.FEMALE);
employeeDTO.setIsIndian(resultSet.getBoolean("is_indian"));
employeeDTO.setPANNumber(resultSet.getString("pan_number").trim());
employeeDTO.setAadharCardNumber(resultSet.getString("aadhar_card_number").trim());
resultSet.close();
preparedStatement.close();
connection.close();
return employeeDTO;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public boolean employeeIdExists(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("EmployeId is null.");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Length of employeeId is zero.");
int actualEmployeeId=0;
try
{
actualEmployeeId=Integer.parseInt(employeeId.substring(1))-10000000;
}catch(Exception exception)
{
throw new DAOException("Invalid employee id");
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where employee_id=?");
preparedStatement.setInt(1,actualEmployeeId);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
return false;
}
resultSet.close();
preparedStatement.close();
connection.close();
return false;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}

public boolean panNumberExists(String panNumber) throws DAOException
{
if(panNumber==null) return false;
panNumber=panNumber.trim();
if(panNumber.length()==0) return false;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where pan_number=?");
preparedStatement.setString(1,panNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
return false;
}
resultSet.close();
preparedStatement.close();
connection.close();
return false;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null) return false;
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) return false;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select gender from employee where aadhar_card_number=?");
preparedStatement.setString(1,aadharCardNumber);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
return false;
}
resultSet.close();
preparedStatement.close();
connection.close();
return false;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public int getCount() throws DAOException
{
try
{
Connection connection=DAOConnection.getConnection();
Statement statement=connection.createStatement();
ResultSet resultSet=statement.executeQuery("select count(*) as count from employee");
resultSet.next();
int count=resultSet.getInt("count");
resultSet.close();
statement.close();
connection.close();
return count;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public int getCountByDesignationCode(int designationCode) throws DAOException
{
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement=connection.prepareStatement("select count(*) as count from employee where designation_code=?");
preparedStatement.setInt(1,designationCode);
ResultSet resultSet=preparedStatement.executeQuery();
resultSet.next();
int count=resultSet.getInt("count");
resultSet.close();
preparedStatement.close();
connection.close();
return count;
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
}