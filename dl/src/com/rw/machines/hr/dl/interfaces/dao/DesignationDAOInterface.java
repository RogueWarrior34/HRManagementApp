package com.rw.machines.hr.dl.interfaces.dao;

import com.rw.machines.hr.dl.interfaces.dto.*;
import com.rw.machines.hr.dl.exceptions.*;
import java.util.*;

public interface DesignationDAOInterface
{
public void add(DesignationDTOInterface designationDTO) throws DAOException;
public void update(DesignationDTOInterface designationDTO) throws DAOException;
public void delete(int designationCode) throws DAOException;
public Set<DesignationDTOInterface> getAll() throws DAOException;
public DesignationDTOInterface getDesignationByCode(int code) throws DAOException;
public DesignationDTOInterface getDesignationByTitle(String title) throws DAOException;
public boolean codeExists(int code) throws DAOException;
public boolean titleExists(String title) throws DAOException;
public int getCount() throws DAOException;
}