package com.rw.machines.hr.bl.managers;

import com.rw.machines.hr.bl.interfaces.pojo.*;
import com.rw.machines.hr.bl.interfaces.managers.*;
import com.rw.machines.hr.bl.exceptions.*;
import com.rw.machines.hr.bl.pojo.*;
import com.rw.machines.hr.bl.managers.*;
import com.rw.machines.hr.dl.interfaces.dto.*;
import com.rw.machines.hr.dl.interfaces.dao.*;
import com.rw.machines.hr.dl.exceptions.*;
import com.rw.machines.hr.dl.dto.*;
import com.rw.machines.hr.dl.dao.*;
import com.rw.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;

public class EmployeeManager implements EmployeeManagerInterface
{
private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
private Map<String,EmployeeInterface> panNumberWiseEmployeesMap;
private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeesMap;
private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap; 
private Set<EmployeeInterface> employeesSet;
private static EmployeeManager employeeManager=null;

private EmployeeManager() throws BLException
{
populateDataStructures();
}
private void populateDataStructures() throws BLException
{
this.employeeIdWiseEmployeesMap=new HashMap<>();
this.panNumberWiseEmployeesMap=new HashMap<>();
this.aadharCardNumberWiseEmployeesMap=new HashMap<>();
this.designationCodeWiseEmployeesMap=new HashMap<>();
this.employeesSet=new TreeSet<>();
try
{
Set<EmployeeDTOInterface> dlEmployees;
dlEmployees=new EmployeeDAO().getAll();
EmployeeInterface employee;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
DesignationInterface designation;
Set<EmployeeInterface> ets;
for(EmployeeDTOInterface dlEmployee : dlEmployees)
{
employee=new Employee();
employee.setEmployeeId(dlEmployee.getEmployeeId());
employee.setName(dlEmployee.getName());
designation=designationManager.getDesignationByCode(dlEmployee.getDesignationCode());
employee.setDesignation(designation);
employee.setDateOfBirth(dlEmployee.getDateOfBirth());
if(dlEmployee.getGender()=='M')
{
employee.setGender(GENDER.MALE);
}
else
{
employee.setGender(GENDER.FEMALE);
}
employee.setIsIndian(dlEmployee.getIsIndian());
employee.setBasicSalary(dlEmployee.getBasicSalary());
employee.setPANNumber(dlEmployee.getPANNumber());
employee.setAadharCardNumber(dlEmployee.getAadharCardNumber());
this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
this.panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
this.aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(),employee);
this.employeesSet.add(employee);
ets=this.designationCodeWiseEmployeesMap.get(designation.getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(employee);
this.designationCodeWiseEmployeesMap.put(designation.getCode(),ets);
}
else
{
ets.add(employee);
}
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public static EmployeeManagerInterface getEmployeeManager() throws BLException
{
if(employeeManager==null) employeeManager=new EmployeeManager();
return employeeManager;
}

public void addEmployee(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
int designationCode=0;
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();
if(employeeId!=null)
{
employeeId=employeeId.trim();
if(employeeId.length()>0)
{
blException.addException("employeeId","EmployeeId should be nil.");
}
}
if(name==null)
{
blException.addException("name","Name required.");
}
else
{
name=name.trim();
if(name.length()==0)
{
blException.addException("name","Name required.");
}
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designation==null)
{
blException.addException("designation","Designation required.");
}
else
{
designationCode=designation.getCode();
if(designationManager.designationCodeExists(designationCode)==false)
{
blException.addException("designation","Invalid designation code"+designationCode);
}
}
if(dateOfBirth==null)
{
blException.addException("dob","DOB required.");
}
if(gender==' ')
{
blException.addException("gender","Gender not assigned.");
}
if(basicSalary==null)
{
blException.addException("basic salary","Salary required.");
}
else
{
if(basicSalary.signum()==-1)
{
blException.addException("basic salary","Salary can't be negative.");
}
}
if(panNumber==null)
{
blException.addException("PAN number","PAN Number required.");
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
blException.addException("PAN Number","PAN Number required.");
}
}
if(aadharCardNumber==null)
{
blException.addException("Aadhar Card Number","Aadhar Card Number required.");
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
blException.addException("Aadhar Card Number","Aadhar Card Number required.");
}
}
if(panNumber!=null && panNumber.length()>0)
{
if(this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase()))
{
blException.addException("PAN Number","PAN Number already exists.");
}
}
if(aadharCardNumber!=null && aadharCardNumber.length()>0)
{
if(this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
{
blException.addException("Aadhar Card Number","Aadhar Card Number already exists.");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
Set<EmployeeInterface> ets;
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designation.getCode());
dlEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dlEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharCardNumber(aadharCardNumber);
employeeDAO.add(dlEmployee);
employee.setEmployeeId(dlEmployee.getEmployeeId());
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(name);
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dsEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(isIndian);
dsEmployee.setBasicSalary(basicSalary);
dsEmployee.setPANNumber(panNumber);
dsEmployee.setAadharCardNumber(aadharCardNumber);
this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),dsEmployee);
this.panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
this.employeesSet.add(dsEmployee);
ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
this.designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(),ets);
}
else
{
ets.add(dsEmployee);
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public void updateEmployee(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
int designationCode=0;
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();
if(employeeId==null)
{
blException.addException("employeeId","EmployeeId required.");
throw blException;
}
else
{
employeeId=employeeId.trim();
if(employeeId.length()==0)
{
blException.addException("employeeId","EmployeeId required.");
throw blException;
}
}
if(employeeId!=null && employeeId.length()>0)
{
if(this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false)
{
blException.addException("employeeId","Invalid employeeId :"+employeeId);
throw blException;
}
}
if(name==null)
{
blException.addException("name","Name required.");
}
else
{
name=name.trim();
if(name.length()==0)
{
blException.addException("name","Name required.");
}
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designation==null)
{
blException.addException("designation","Designation required.");
}
else
{
designationCode=designation.getCode();
if(designationManager.designationCodeExists(designationCode)==false)
{
blException.addException("designation","Invalid designation code"+designationCode);
}
}
if(dateOfBirth==null)
{
blException.addException("dob","DOB required.");
}
if(gender==' ')
{
blException.addException("gender","Gender not assigned.");
}
if(basicSalary==null)
{
blException.addException("basic salary","Salary required.");
}
else
{
if(basicSalary.signum()==-1)
{
blException.addException("basic salary","Salary can't be negative.");
}
}
if(panNumber==null)
{
blException.addException("PAN number","PAN Number required.");
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
blException.addException("PAN Number","PAN Number required.");
}
}
if(aadharCardNumber==null)
{
blException.addException("Aadhar Card Number","Aadhar Card Number required.");
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
blException.addException("Aadhar Card Number","Aadhar Card Number required.");
}
}
if(panNumber!=null && panNumber.length()>0)
{
EmployeeInterface ee=this.panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
if(ee!=null && (ee.getEmployeeId().equalsIgnoreCase(employeeId)==false))
{
blException.addException("PAN Number","PAN Number already exists.");
}
}
if(aadharCardNumber!=null && aadharCardNumber.length()>0)
{
EmployeeInterface ee=this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber);
if(ee!=null && (ee.getEmployeeId().equalsIgnoreCase(employeeId)==false))
{
blException.addException("Aadhar Card Number","Aadhar Card Number already exists.");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
EmployeeInterface dsEmployee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
String oldPANNumber=dsEmployee.getPANNumber();
String oldAadharCardNumber=dsEmployee.getAadharCardNumber();
int oldDesignationCode=dsEmployee.getDesignation().getCode();
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designation.getCode());
dlEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dlEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharCardNumber(aadharCardNumber);
employeeDAO.update(dlEmployee);
dsEmployee.setName(name);
dsEmployee.setDesignation(((DesignationManager)designationManager).getDesignationByCode(designation.getCode()));
dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dsEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(isIndian);
dsEmployee.setBasicSalary(basicSalary);
dsEmployee.setPANNumber(panNumber);
dsEmployee.setAadharCardNumber(aadharCardNumber);
this.employeeIdWiseEmployeesMap.remove(dsEmployee.getEmployeeId().toUpperCase());
this.panNumberWiseEmployeesMap.remove(oldPANNumber.toUpperCase());
this.aadharCardNumberWiseEmployeesMap.remove(oldAadharCardNumber.toUpperCase());
this.employeesSet.remove(dsEmployee);
Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(oldDesignationCode);
ets.remove(dsEmployee);
this.employeesSet.add(dsEmployee);
this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
this.panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
this.designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(),ets);
}
else
{
ets.add(dsEmployee);
}
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public void removeEmployee(String employeeId) throws BLException
{
BLException blException=new BLException();
if(employeeId==null)
{
blException.addException("employeeId","EmployeeId required.");
throw blException;
}
else
{
employeeId=employeeId.trim();
if(employeeId.length()==0)
{
blException.addException("employeeId","EmployeeId required.");
throw blException;
}
}
if(employeeId!=null && employeeId.length()>0)
{
if(this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())==false)
{
blException.addException("employeeId","Invalid employeeId :"+employeeId);
throw blException;
}
}
try
{
EmployeeInterface dsEmployee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.delete(dsEmployee.getEmployeeId());
this.employeeIdWiseEmployeesMap.remove(dsEmployee.getEmployeeId().toUpperCase());
this.panNumberWiseEmployeesMap.remove(dsEmployee.getPANNumber().toUpperCase());
this.aadharCardNumberWiseEmployeesMap.remove(dsEmployee.getAadharCardNumber().toUpperCase());
this.employeesSet.remove(dsEmployee);
Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
ets.remove(dsEmployee);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException
{
EmployeeInterface employee=this.employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
if(employee==null)
{
BLException blException=new BLException();
blException.addException("employee id","Invalid employeeId: "+employeeId);
throw blException;
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
e.setDesignation(((DesignationManager)designationManager).getDesignationByCode(employee.getDesignation().getCode()));
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
e.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
return e;
}

public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException
{
EmployeeInterface employee=this.panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
if(employee==null)
{
BLException blException=new BLException();
blException.addException("pan number","Invalid panNumber: "+panNumber);
throw blException;
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
e.setDesignation(((DesignationManager)designationManager).getDesignationByCode(employee.getDesignation().getCode()));
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
e.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
return e;
}

public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException
{
EmployeeInterface employee=this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
if(employee==null)
{
BLException blException=new BLException();
blException.addException("aadhar card number","Invalid aadharCardNumber: "+aadharCardNumber);
throw blException;
}
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
EmployeeInterface e=new Employee();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
e.setDesignation(((DesignationManager)designationManager).getDesignationByCode(employee.getDesignation().getCode()));
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
e.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
return e;
}

public int getEmployeeCount()
{
return this.employeesSet.size();
}

public int getEmployeeCountByDesignationCode(int designationCode)
{
Set<EmployeeInterface> ets=this.designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null)
{
return 0;
}
return ets.size();
}

public boolean employeeIdExists(String employeeId)
{
return this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
}

public boolean employeePANNumberExists(String panNumber)
{
return this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase());
}

public boolean employeeAadharCardNumberExists(String aadharCardNumber)
{
return this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
}

public boolean isDesignationAlloted(int designationCode)
{
return this.designationCodeWiseEmployeesMap.containsKey(designationCode);
}

public Set<EmployeeInterface> getAllEmployees() throws BLException
{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
Set<EmployeeInterface> employees=new TreeSet<>();
DesignationInterface dsDesignation, designation;
for(EmployeeInterface employee:this.employeesSet)
{
EmployeeInterface e=new Employee();
designation=new Designation();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
dsDesignation=employee.getDesignation();
designation.setCode(dsDesignation.getCode());
designation.setTitle(dsDesignation.getTitle());
e.setDesignation(designation);
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
e.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
employees.add(e);
}
return employees;
}

public Set<EmployeeInterface> getAllEmployeesByDesignationCode(int designationCode) throws BLException
{
Set<EmployeeInterface> employees=new TreeSet<>();
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designationManager.designationCodeExists(designationCode)==false)
{
BLException blException=new BLException();
blException.setGenericException("Invalid designation code: "+designationCode);
throw blException;
}
Set<EmployeeInterface> ets=this.designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null)
{
return employees;
}
DesignationInterface dsDesignation;
DesignationInterface designation;
for(EmployeeInterface employee:ets)
{
EmployeeInterface e=new Employee();
designation=new Designation();
e.setEmployeeId(employee.getEmployeeId());
e.setName(employee.getName());
dsDesignation=employee.getDesignation();
designation.setCode(dsDesignation.getCode());
designation.setTitle(dsDesignation.getTitle());
e.setDesignation(designation);
e.setDateOfBirth((Date)employee.getDateOfBirth().clone());
e.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
e.setIsIndian(employee.getIsIndian());
e.setBasicSalary(employee.getBasicSalary());
e.setPANNumber(employee.getPANNumber());
e.setAadharCardNumber(employee.getAadharCardNumber());
employees.add(e);
}
return employees;
}

}