package com.zuehlke.lab.web.jsf.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.zuehlke.lab.web.jsf.model.Cloud;
import com.zuehlke.lab.service.CloudService;
import com.zuehlke.lab.service.SearchAttribute;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
@ManagedBean
@RequestScoped
public class CloudManager implements Serializable {
 
    private static final long serialVersionUID = 1L;
    
    @EJB
    CloudService cloudService;
    
    @PersistenceContext(unitName="cloudCompPU")
    EntityManager em;
        
    Cloud cloud;
	
    public CloudManager(){
        cloud = new Cloud("Company",0,null);
        Cloud jCloud = new Cloud("Java",20,null);
        Cloud jeeCloud = new Cloud("JEE",20,null);
        jeeCloud.addElement("Glassfish",20);
        jeeCloud.addElement("JBOSS",10);
        jeeCloud.addElement("Tomcat",15);
        jeeCloud.addElement("EJB 3.0",10);
        jeeCloud.addElement("EJB 2.1",5);
        jCloud.addElement(jeeCloud);
        jCloud.addElement("JDBC",10);
        jCloud.addElement("JNDI",5);
        jCloud.addElement("Spring",15);
        cloud.addElement(jCloud);
        cloud.addElement(".Net",15);
        cloud.addElement("VB",1);
        cloud.addElement("C++",5);
    }

    public Cloud getCloud() {
        
//        if(cloud == null){
          return  cloud = cloudService.getRecursiveCloud(Arrays.asList(new SearchAttribute[]{SearchAttribute.TECHNOLOGY,SearchAttribute.UNIT,SearchAttribute.PERSON}));
//        }
        //return cloud;
        
        //return cloudService.getCloud(em.find(Course.class, 6L)); 
        //return cloudService.getCloud(em.find(Person.class, 1L));
        //return cloud;
        //return cloudService.getCloud();
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }
}