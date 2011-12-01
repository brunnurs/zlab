/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zuehlke.lab.service.importer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class ImporterService {

    public void importWordBundle(String filename, byte[] data){
        System.out.println("Successfull imported");
    }
    
    public List<ZipEntry> getZipEntries(byte[] data) throws IOException{
        ZipInputStream wordFilesAsStream = new ZipInputStream(new ByteArrayInputStream(data));
        List<ZipEntry> entries = new LinkedList<ZipEntry>();
        ZipEntry entry = null;
     
        while((entry = wordFilesAsStream.getNextEntry()) != null){
            entries.add(entry);
        }
        
        
        
        return entries;
    }
    
    public 
    
}
