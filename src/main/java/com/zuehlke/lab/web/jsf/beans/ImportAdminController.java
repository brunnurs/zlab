/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.web.jsf.beans;

import com.zuehlke.analysis.DocumentAnalysisService;
import com.zuehlke.lab.service.RelevanceService;
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
    DocumentAnalysisService documentAnalysisService;
    
    @EJB
    RelevanceService relevanceService;
    
    @EJB
    YammerImporterService yammerImporterService;
    
    
    public void uploadWordBundle(FileUploadEvent event) throws IOException{
        importService.importWordBundle(event.getFile().getContents());
    }
    
    public void importYammerUsers(){
        yammerImporterService.insertYammerIds();
    }
    
    public void importYammerPostsByUser(){
        yammerImporterService.importYammerPostsByUser();
    }
    
    public void importYammerPostsByNetwork(){
        yammerImporterService.importYammerPostsByNetwork(21);
    }
    
    
    public void analyse(){
        documentAnalysisService.analyseDocuments();
    }
    
    public void updateBlacklist(){
        relevanceService.updateCount();
    }
    
}
