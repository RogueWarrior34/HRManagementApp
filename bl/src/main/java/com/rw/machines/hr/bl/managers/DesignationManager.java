package com.rw.machines.hr.bl.managers;
import com.rw.machines.hr.bl.interfaces.pojo.*;
import com.rw.machines.hr.bl.interfaces.managers.*;
import com.rw.machines.hr.bl.exceptions.*;
import com.rw.machines.hr.bl.pojo.*;

import com.rw.machines.hr.dl.interfaces.dto.*;
import com.rw.machines.hr.dl.interfaces.dao.*;
import com.rw.machines.hr.dl.exceptions.*;
import com.rw.machines.hr.dl.dto.*;
import com.rw.machines.hr.dl.dao.*;
import java.util.*;


public class DesignationManager implements DesignationManagerInterface
{
private Map<Integer, DesignationInterface> codeWiseDesignationsMap;
private Map<String, DesignationInterface> titleWiseDesignationsMap;
private Set<DesignationInterface> designationsSet;
private static DesignationManager designationManager=null;

private DesignationManager() throws BLException
{
populateDataStructures();
}

private void populateDataStructures() throws BLException
{
this.codeWiseDesignationsMap= new HashMap<>();
this.titleWiseDesignationsMap= new HashMap<>();
this.designationsSet= new TreeSet<>();
try
{
Set <DesignationDTOInterface> dlDesignations;
dlDesignations= new DesignationDAO().getAll();
DesignationInterface designation;
for(DesignationDTOInterface dlDesignation : dlDesignations)
{
designation= new Designation();
designation.setCode(dlDesignation.getCode());
designation.setTitle(dlDesignation.getTitle());
this.codeWiseDesignationsMap.put(designation.getCode(), designation);
this.titleWiseDesignationsMap.put(designation.getTitle(), designation);
this.designationsSet.add(designation);
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public static DesignationManager getDesignationManager() throws BLException
{
if(designationManager==null) designationManager= new DesignationManager();
return designationManager;
}
   
public void addDesignation(DesignationInterface designation) throws BLException
{
BLException blException= new BLException();
if(designation==null)
{
blException.setGenericException("Designation required.");
throw blException;
}
int code= designation.getCode();
String title= designation.getTitle();
if(code!=0)
{
blException.addException("code","Code should be zero.");
}
if(title==null)
{
blException.addException("title","Title required.");
title="";
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title required.");
}
}
if(title.length()>0)
{
if(this.titleWiseDesignationsMap.containsKey(title.toUpperCase()))
{
blException.addException("title","Designation: "+title+" exists");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationDTOInterface designationDTO;
designationDTO= new DesignationDTO();
designationDTO.setTitle(title);
DesignationDAOInterface designationDAO;
designationDAO= new DesignationDAO();
designationDAO.add(designationDTO);
code=designationDTO.getCode();
DesignationInterface dsDesignation;
dsDesignation=new Designation();
designation.setCode(code);
dsDesignation.setCode(code);
dsDesignation.setTitle(title);
this.codeWiseDesignationsMap.put(code, dsDesignation);
this.titleWiseDesignationsMap.put(title.toUpperCase(), dsDesignation);
this.designationsSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public void updateDesignation(DesignationInterface designation) throws BLException
{
BLException blException= new BLException();
if(designation==null)
{
blException.setGenericException("Designation required.");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code<=0)
{
blException.addException("code","Invalid code :"+code);
}
if(code>0)
{
if(this.codeWiseDesignationsMap.containsKey(code)==false)
{
blException.addException("code","Invalid code :"+code);
throw blException;
}
}
if(title==null)
{
blException.addException("title","Title required.");
title="";
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title required.");
}
}
if(title.length()>0)
{
DesignationInterface d;
d=this.titleWiseDesignationsMap.get(title.toUpperCase());
if(d!=null && d.getCode()!=code)
{
blException.addException("title","Designation: "+title+" exists.");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationInterface dsDesignation=this.codeWiseDesignationsMap.get(code);
DesignationDTOInterface dlDesignation=new DesignationDTO();
dlDesignation.setCode(code);
dlDesignation.setTitle(title);
new DesignationDAO().update(dlDesignation);
this.codeWiseDesignationsMap.remove(dsDesignation.getCode());
this.titleWiseDesignationsMap.remove(dsDesignation.getTitle().toUpperCase());
this.designationsSet.remove(dsDesignation);
dsDesignation.setTitle(title);
this.codeWiseDesignationsMap.put(code,dsDesignation);
this.titleWiseDesignationsMap.put(title,dsDesignation);
this.designationsSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public void removeDesignation(int code) throws BLException
{
BLException blException= new BLException();
if(code<=0)
{
blException.addException("code","Invalid code :"+code);
throw blException;
}
if(code>0)
{
if(this.codeWiseDesignationsMap.containsKey(code)==false)
{
blException.addException("code","Invalid code :"+code);
throw blException;
}
}
try
{
DesignationInterface dsDesignation=this.codeWiseDesignationsMap.get(code);
new DesignationDAO().delete(code);
this.codeWiseDesignationsMap.remove(dsDesignation.getCode());
this.titleWiseDesignationsMap.remove(dsDesignation.getTitle().toUpperCase());
this.designationsSet.remove(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public DesignationInterface getDesignationByCode(int code) throws BLException
{
DesignationInterface dsDesignation=this.codeWiseDesignationsMap.get(code);
if(dsDesignation==null)
{
BLException blException= new BLException();
blException.addException("code","Invalid code: "+code);
throw blException;
}
DesignationInterface d=new Designation();
d.setCode(dsDesignation.getCode());
d.setTitle(dsDesignation.getTitle());
return d;
}

public DesignationInterface getDesignationByTitle(String title) throws BLException
{
DesignationInterface dsDesignation=this.titleWiseDesignationsMap.get(title.toUpperCase());
if(dsDesignation==null)
{
BLException blException= new BLException();
blException.addException("title","Invalid title: "+title);
throw blException;
}
DesignationInterface d=new Designation();
d.setCode(dsDesignation.getCode());
d.setTitle(dsDesignation.getTitle());
return d;
}

public int getDesignationCount()
{
return this.designationsSet.size();
}

public boolean designationCodeExists(int code)
{
return this.codeWiseDesignationsMap.containsKey(code);
}

public boolean designationTitleExists(String title)
{
return this.titleWiseDesignationsMap.containsKey(title.toUpperCase());
}

public Set<DesignationInterface> getAllDesignations()
{
Set<DesignationInterface> designations;
designations=new TreeSet<>();
this.designationsSet.forEach((designation)->{
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
designations.add(d);
});
return designations;
}

DesignationInterface getDSDesignationByCode(int code)
{
DesignationInterface designation;
designation=this.codeWiseDesignationsMap.get(code);
return designation;
}

}