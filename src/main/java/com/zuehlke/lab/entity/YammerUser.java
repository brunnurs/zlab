/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class YammerUser implements Serializable {
    @Id
    private Long id;
    @XmlElement(name="full_name")
    private String name;
    @XmlElement(name="id")
    private String yammerId;
    private String department;
    @XmlElement(name="job_title")
    private String jobTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYammerId() {
        return yammerId;
    }

    public void setYammerId(String yammerId) {
        this.yammerId = yammerId;
    }
}
