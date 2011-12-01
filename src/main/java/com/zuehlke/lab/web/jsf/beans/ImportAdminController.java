/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.web.jsf.beans;

import com.zuehlke.lab.service.importer.ImporterService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author user
 */
@Named
@RequestScoped
public class ImportAdminController {
    
    @Inject
    ImporterService importService;
    
    public void uploadWordBundle(FileUploadEvent event){
        importService.importWordBundle(event.getFile().getFileName(), event.getFile().getContents());
    }
}
