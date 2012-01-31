/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;

/**
 *
 * @author user
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Person.findByName",query="SELECT p FROM Person p WHERE p.firstname =  :firstname AND p.lastname = :lastname"),
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
})
public class Person extends Owner implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstname;
    private String lastname;
    private Long yammerId; 
    


    public Person() {
        super();
    }
    
    public Person(String firstname, String lastname) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Long getYammerId() {
        return yammerId;
    }

    public void setYammerId(Long yammerId) {
        this.yammerId = yammerId;
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
}
