/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author user
 */
@Entity
public class Event extends Owner implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private EventType type;
    
    @ManyToMany(cascade= CascadeType.ALL)
    private List<Person> persons;

    
    public Event(String name) {
        this();
        this.name = name;
    }
    
    public Event(){
        persons = new ArrayList<Person>();
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
        this.factor = persons.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
