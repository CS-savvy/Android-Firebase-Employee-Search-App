package com.ongc.pro.employdb;

/**
 * Created by Mukul on 6/29/2017.
 */

public class Fav {


    String name;
    String cif;
    String age;
    String designation;
    String pic;
    String contact_no;

    public Fav()
    {

    }
    public Fav(String name, String cif, String age, String designation, String pic ,String contact_no) {
        this.name = name;
        this.cif = cif;
        this.age = age;
        this.designation = designation;
        this.pic = pic;
        this.contact_no = contact_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }
}
