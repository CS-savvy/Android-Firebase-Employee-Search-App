package com.ongc.pro.employdb;

/**
 * Created by Mukul on 6/6/2017.
 **/

public class EmpDT {

    int id;
    String name ,contact_no , designation;

    public EmpDT()
    {
        //empty contructor
    }

    public EmpDT(int id , String name,String contact_no , String designation)
    {
        this.id = id;
        this.name = name;
        this.contact_no = contact_no ;
        this.designation = designation;
    }

    public EmpDT(String name,String contact_no , String designation)
    {
        this.name = name;
        this.contact_no = contact_no ;
        this.designation = designation;
    }

    public int getID()
    {
         return this.id;
    }

    public  void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public  void setName(String name)
    {
        this.name = name;
    }

    public String getContact_no()
    {
        return this.contact_no;
    }

    public  void setContact_no(String contact_no)
    {
        this.contact_no = contact_no;
    }
    public String getDesignation()
    {
        return this.designation;
    }

    public  void setDesignation(String designation)
    {
        this.designation = designation;
    }

}
