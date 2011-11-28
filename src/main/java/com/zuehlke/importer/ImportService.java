/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.importer;

import com.zuehlke.persistance.entities.RawData;
import com.zuehlke.persistance.facade.RawDataFacade;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class ImportService {
@Inject
private RawDataFacade dataFacade;
    
    public void importUserProfiles() {
        RawData data = new RawData();
        data.setInsertDate(new Date());
        data.setRawValue("I'm an Information");
        dataFacade.create(data);
        
        System.out.println("Files imported");
    }
    
}
