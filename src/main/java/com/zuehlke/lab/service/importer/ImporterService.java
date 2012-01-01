/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service.importer;

import com.zuehlke.analysis.NLPService;
import com.zuehlke.lab.entity.Document;
import com.zuehlke.lab.entity.DocumentSource;
import com.zuehlke.lab.entity.Keyword;
import com.zuehlke.lab.entity.Person;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
/**
 * @author user
 */
@Stateless
@LocalBean
public class ImporterService {
    
    
    @EJB
    NLPService nlpService;
    
    
    @PersistenceContext(name="cloudCompPU")
    EntityManager em;

    public void importWordBundle(String filename, byte[] data) throws IOException {
        List<String> texts = getZipEntriesAsText(data);
        for(String text : texts){
            try{
               importWord(text);
            }catch(Exception e){
                System.out.println("Not successfull imported");
                continue;
            }
            System.out.println("Successfull imported");
        }
       
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void importWord(String text){
        Person p = new Person("test", "test");
        em.persist(p);
        Document doc = new Document();
        doc.setOwner(p);
        //doc.setRawData(text);
        //doc.setSource(DocumentSource.PERSONAL_PROFILE);
        addKeywords(nlpService.extractNouns(text),doc);
        em.persist(doc);
    }

    private List<String> getZipEntriesAsText(byte[] data) throws IOException {
        ZipInputStream wordFilesAsStream = new ZipInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream outputStream = null;
        List<String> entriesAsString = new LinkedList<String>();
        
        while ((wordFilesAsStream.getNextEntry()) != null) {
            outputStream = new ByteArrayOutputStream();
            IOUtils.copy(wordFilesAsStream, outputStream);
            entriesAsString.add(getWordDocContentByRawData(outputStream.toByteArray()));
        }
        
        return entriesAsString;
    }

    private String getWordDocContentByRawData(byte[] data) throws IOException {
        XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(data));
        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
        return extractor.getText();
    }
    
    private void storeRawDataInDB(List<String> rawTexts){
       // for()
    }
    
    
    private void addKeywords(Map<String,MutableInt> nouns, Document doc){
       for(Map.Entry<String,MutableInt> entry : nouns.entrySet()){
          new Keyword(doc,entry.getKey(), entry.getValue().intValue());
       }
    }
}
