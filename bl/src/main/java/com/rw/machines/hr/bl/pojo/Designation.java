package com.rw.machines.hr.bl.pojo;

import com.rw.machines.hr.bl.interfaces.pojo.*;

public class Designation implements DesignationInterface
{
private int code;
private String title;
public Designation()
{
this.code=0;
this.title="";
}
public void setCode(int code)
{
this.code=code;
}
public int getCode()
{
return this.code;
}
public void setTitle(String title)
{
this.title=title;
}
public String getTitle()
{
return this.title;
}
public int hashCode()
{
return this.code;
}
public boolean equals(Object other)
{
if(!(other instanceof Designation)) return false;
DesignationInterface designation=(Designation)other;
return this.code==designation.getCode();
}
public int compareTo(DesignationInterface designation)
{
return this.code-designation.getCode();
}
}