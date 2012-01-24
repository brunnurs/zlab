/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service.importer;

import au.com.bytecode.opencsv.CSVReader;
import com.zuehlke.analysis.NLPService;
import com.zuehlke.lab.entity.Document;
import com.zuehlke.lab.entity.DocumentSource;
import com.zuehlke.lab.entity.Keyword;
import com.zuehlke.lab.entity.Person;
import com.zuehlke.lab.entity.Unit;
import com.zuehlke.lab.service.RelevanceService;
import com.zuehlke.lab.service.PersonService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
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
    
    @PersistenceContext(name="cloudCompPU")
    EntityManager em;
    @Inject
    NLPService nlpService;
    @Inject
    PersonService personService;
    @EJB
    RelevanceService relevanceService;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void importWord(String rawData, String firstname, String lastname){
               Document doc = new Document();
               doc.setRawData(rawData);
               doc.setSource(DocumentSource.PERSONAL_PROFILE);
               personService.saveDocumentToUser(doc, firstname, lastname);
    }
    
    private void analyseDocument(Document doc){
                LinkedList<Keyword> keywords = getKeywords(nlpService.extractTerms(doc.getRawData()));
                for(Keyword keyword : relevanceService.removeWithListedWords(keywords)){
                    keyword.setDocument(doc);
                    doc.addKeyword(keyword);
                    em.persist(keyword);
                }
                relevanceService.removeBlackListedWords(keywords);
                relevanceService.setAutoWithlisted(keywords);
                for(Keyword keyword : keywords){
                    keyword.setDocument(doc);
                    doc.addKeyword(keyword);
                    em.persist(keyword);
                }
                
    }
    
    @Asynchronous
    public void analyseDocuments(){
        System.out.print("Analyse documents");
        for(Document doc : em.createNamedQuery("Document.findAll", Document.class).getResultList()){
            analyseDocument(doc);
            System.out.print(".");
        }
        System.out.println("finish!");
    }
    
    

    @Asynchronous
    public void importWordBundle(byte[] data) throws IOException {
        ZipInputStream wordFilesAsStream = new ZipInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream outputStream = null;
        
        ZipEntry entry = null;
        while ((entry = wordFilesAsStream.getNextEntry()) != null) {
            outputStream = new ByteArrayOutputStream();
            IOUtils.copy(wordFilesAsStream, outputStream);
            try {
                
                String [] nameSurnamePrefix  = new String[3];

                try {
                    nameSurnamePrefix = filterFirstnameLastNameByFilename(entry.getName());
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                }
                
                String rawData = "";
                if(nameSurnamePrefix[2].equals("doc")){
                    rawData = getWordDocContentByRawData(outputStream.toByteArray());
                }else if(nameSurnamePrefix[2].equals("docx")){
                    rawData = getWordDocxContentByRawData(outputStream.toByteArray());
                }else{
                    throw new IllegalArgumentException("Unknown file-prefix ["+nameSurnamePrefix[2]+"] !");
                }

                importWord(rawData,nameSurnamePrefix[1], nameSurnamePrefix[0]);
 
                System.out.println("Data for Person ["+nameSurnamePrefix[1] +" "+nameSurnamePrefix[0] +"] successfully imported");
            } catch (Exception ex) {
                System.out.println("Could not get rawdata for [" + entry.getName() + "]. Reason was:" + ex.getStackTrace());
            }
        }
        System.out.println("Update relevance word count.");
        relevanceService.updateCount();
        System.out.println("Successfull imported");
    }

    private String getWordDocxContentByRawData(byte[] data) throws IOException {
        XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(data));
        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
        return extractor.getText();
    }

    private String getWordDocContentByRawData(byte[] data) throws IOException {
        HWPFDocument doc = new HWPFDocument(new ByteArrayInputStream(data));
        WordExtractor extractor = new WordExtractor(doc);
        return extractor.getText();
    }

    /**
     * 
     * @param filename
     * @return a string array with element 0 = lastname, element 1 = firstname, element 2 = file-prefix
     * @throws IllegalArgumentException 
     */
    private String[] filterFirstnameLastNameByFilename(String filename) throws IllegalArgumentException {
            String[] lastnameFirstName = filename.split("(_|\\.)");
            if(lastnameFirstName.length == 3){
                return lastnameFirstName;
            }else{
                throw new IllegalArgumentException("filename [" + filename + "] seems not to contain a vaild name.");
            }
    }
    
    private LinkedList<Keyword> getKeywords(Map<String,MutableInt> nouns){
       LinkedList<Keyword> retVal =  new LinkedList<Keyword>();
       for(Map.Entry<String,MutableInt> entry : nouns.entrySet()){
          retVal.add(new Keyword(entry.getKey(),entry.getValue().intValue()));
       }
       return retVal;
    }
    
    
    public void updatePerson() throws FileNotFoundException, IOException{
        Map<String,Unit> units = new HashMap<String, Unit>();
        
        for(Unit unit : em.createNamedQuery("Unit.findAll", Unit.class).getResultList()){
            units.put(unit.getName(), unit);
        }
        
        InputStream data = this.getClass().getClassLoader().getResourceAsStream("persons.csv");
        CSVReader reader = new CSVReader(new InputStreamReader(data),',');
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            int i = nextLine[0].lastIndexOf(' ');
            String fname = nextLine[0].substring(i+1), lname = nextLine[0].substring(0, i);
            Person p = new Person(fname, lname);
            em.persist(p);
            Unit unit = units.get(nextLine[1]);
            if(unit == null){
                unit = new Unit(nextLine[1]);
                em.persist(unit);
            }
            units.put(unit.getName(),unit);
            unit.addPerson(p);
        }
    }
}
