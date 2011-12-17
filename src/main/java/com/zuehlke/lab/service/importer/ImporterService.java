/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service.importer;

import com.zuehlke.lab.entity.Person;
import com.zuehlke.lab.service.PersonService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.io.IOUtils;
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

    @PersistenceContext(name = "cloudCompPU")
    EntityManager em;
    @Inject
    PersonService personService;

    public void importWordBundle(String filename, byte[] data) throws IOException {
        List<Person> personsWithRawData = getZipEntriesAsTextByPerson(data);
        personService.storeNewOrExistingUserWithRawData(personsWithRawData);
        System.out.println("Successfull imported");
    }

    private List<Person> getZipEntriesAsTextByPerson(byte[] data) throws IOException {
        ZipInputStream wordFilesAsStream = new ZipInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream outputStream = null;
        List<Person> personWithRawData = new LinkedList<Person>();
        ZipEntry entry = null;
        while ((entry = wordFilesAsStream.getNextEntry()) != null) {
            outputStream = new ByteArrayOutputStream();
            IOUtils.copy(wordFilesAsStream, outputStream);
            try {
                
                Person p = new Person();
                String filePrefix = "";

                try {
                    filePrefix = filterFirstnameLastNameByFilename(entry.getName(), p);
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                }
                
                String rawData = "";
                if(filePrefix.equals("doc"))
                    rawData = getWordDocContentByRawData(outputStream.toByteArray());
                else if(filePrefix.equals("docx"))
                    rawData = getWordDocxContentByRawData(outputStream.toByteArray());
                else
                    throw new IllegalArgumentException("Unknown file-prefix ["+filePrefix+"] !");
                
                p.setRawdata(rawData);

                personWithRawData.add(p);
                
                System.out.println("Data for Person ["+p.getLastname() +" "+p.getFirstname()+"] successfully imported");
            } catch (Exception ex) {
                System.out.println("Could not get rawdata for [" + entry.getName() + "]. Reason was:" + ex.getStackTrace());
            }



        }

        return personWithRawData;
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
     * @param storeInThis
     * @return the file prefix (doc or docx)
     * @throws IllegalArgumentException 
     */
    private String filterFirstnameLastNameByFilename(String filename, Person storeInThis) throws IllegalArgumentException {

        try {
            String[] lastnameFirstName = filename.split("(_|\\.)");
            storeInThis.setLastname(lastnameFirstName[0]);
            storeInThis.setFirstname(lastnameFirstName[1]);
            return lastnameFirstName[2];
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("filename [" + filename + "] seems not to contain a vaild name.", e);
        }
    }
}
