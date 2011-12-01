/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service.importer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
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
    
    @PersistenceContext(name="cloudCompPU")
    EntityManager em;

    public void importWordBundle(String filename, byte[] data) throws IOException {
        List<String> texts = getZipEntriesAsText(data);
        System.out.println("Successfull imported");
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
        for()
    }
}
