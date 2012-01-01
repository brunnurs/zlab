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
    }

    public Cloud getCloud() {
        
//        if(cloud == null){
          return  cloud = cloudService.getCloud();//getRecursiveCloud(Arrays.asList(new SearchAttribute[]{SearchAttribute.TECHNOLOGY}));
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