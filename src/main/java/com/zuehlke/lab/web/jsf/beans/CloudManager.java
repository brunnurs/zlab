package com.zuehlke.lab.web.jsf.beans;


import java.io.Serializable;
import java.util.Arrays;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.zuehlke.lab.web.jsf.model.Cloud;
import com.zuehlke.lab.service.CloudService;
import com.zuehlke.lab.service.SearchAttribute;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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
        
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

        List<String> paras = new ArrayList<String>();
        if(req.getParameter("first") != null) paras.add(req.getParameter("first") );
        if(req.getParameter("second") != null) paras.add(req.getParameter("second") );
        if(req.getParameter("third") != null) paras.add(req.getParameter("third") );
        
        if(!paras.isEmpty()){
            SearchAttribute[] searchAttributes = new SearchAttribute[paras.size()];
            int i = 0;
            for(String para : paras){
                searchAttributes[i] = SearchAttribute.fromString(para);
                i++;
            }
            return cloudService.getRecursiveCloud(Arrays.asList(searchAttributes));
        }else{
            return cloudService.getCloudAggregated();
        }
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }
}