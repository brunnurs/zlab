/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;

/**
 *
 * @author user
 */
@Entity
public class Person extends Owner implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstname;
    private String lastname;
    @Lob
    private String rawdata;

    public Person() {
        super();
    }
    
    public Person(String firstname, String lastname) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public int getFactor(){
        return 1;
    }
    
    @PrePersist
    private void setFactor(){
        factor = 1;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRawdata() {
        return rawdata;
    }

    public void setRawdata(String rawdata) {
        this.rawdata = rawdata;
    }
    

}
