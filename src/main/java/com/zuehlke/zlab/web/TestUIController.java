/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.zlab.web;

import com.zuehlke.importer.ImportService;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author user
 */
@RequestScoped
@Named
public class TestUIController {
    
    @Inject
    ImportService iService;
    
    private String testProperty;
    
    @PostConstruct
    public void init(){
        this.testProperty = "Data not imported yet";
    }

    public String getTestProperty() {
        return testProperty;
    }

    public void setTestProperty(String testProperty) {
        this.testProperty = testProperty;
    }
    
    public void importOneProfile(){
        iService.importUserProfiles();
        testProperty = "Data imported";
    }
    
}
