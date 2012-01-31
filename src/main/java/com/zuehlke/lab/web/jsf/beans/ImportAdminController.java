/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.web.jsf.beans;

import com.zuehlke.analysis.DocumentAnalysisService;
import com.zuehlke.lab.service.importer.ImporterService;
import com.zuehlke.lab.service.importer.YammerImporterService;
import java.io.IOException;
import javax.ejb.EJB;
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
    
    @Inject
    YammerImporterService yammerImporterService;
    
    @EJB
    DocumentAnalysisService documentAnalysisService;
    
    
    
    
    public void uploadWordBundle(FileUploadEvent event) throws IOException{
        importService.importWordBundle(event.getFile().getContents());
    }
    
    public void importYammerUsers(){
        yammerImporterService.insertYammerIds();
    }
    
    
    public void analyseDocuments(){
        documentAnalysisService.analyseDocuments();
    }
}
